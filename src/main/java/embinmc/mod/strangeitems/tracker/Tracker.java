package embinmc.mod.strangeitems.tracker;

import com.mojang.datafixers.util.Function6;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import embinmc.mod.strangeitems.StrangeItems;
import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.client.StatShowcaseScreen;
import embinmc.mod.strangeitems.client.StrangeItemsClient;
import embinmc.mod.strangeitems.client.StrangeOptions;
import embinmc.mod.strangeitems.client.config.StrangeConfig;
import embinmc.mod.strangeitems.event.TrackerEvents;
import embinmc.mod.strangeitems.util.StatFormatters;
import embinmc.mod.strangeitems.util.StrangeUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class Tracker {
    protected final Component description;
    protected final Optional<Component> altDescription;
    protected final StatFormatter statFormatter;
    protected final Identifier saveId;
    protected final Trigger trigger;
    protected final HolderSet<Item> itemsToTrack;

    public static final Codec<Tracker> CODEC = StrangeRegistries.TRACKER_TYPE.byNameCodec().dispatch(Tracker::getType, TrackerType::codec);
    protected static final Component NO_REGISTRIES = Component.translatable("tooltip.strangeitems.no_registries").withStyle(ChatFormatting.RED);

    protected Tracker(Trigger trigger, Component description, Optional<Component> altDescription, StatFormatter statFormatter, Identifier saveId, HolderSet<Item> itemsToTrack) {
        this.description = description;
        this.altDescription = altDescription;
        this.statFormatter = statFormatter;
        this.saveId = saveId;
        this.trigger = trigger;
        this.itemsToTrack = itemsToTrack;
    }

    public abstract TrackerType<?> getType();

    public void writeToNbt(final CompoundTag nbt, final int count, final @Nullable Identifier data) {
        int prevVal = nbt.getIntOr(this.saveId.toString(), 0);
        nbt.putInt(this.saveId.toString(), prevVal + count);
    }

    @Environment(EnvType.CLIENT)
    public List<Component> getTooltip(final HolderLookup.@Nullable Provider provider, final ItemStack itemStack) {
        CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = customData.copyTag();
        //if (!nbt.contains(this.saveId.toString()))
        //    return List.of();
        int value = nbt.getIntOr(this.saveId.toString(), 0);
        MutableComponent formattedValue = Component.literal(this.getFormattedValue(value)).withStyle(ChatFormatting.YELLOW);
        MutableComponent desc;
        if (StrangeUtil.isKeyDown(StrangeItemsClient.show_tracker_ids)) {
            desc = Component.literal(this.getId(provider).toString()).withStyle(ChatFormatting.GRAY);
        } else {
            desc = ComponentUtils.mergeStyles(this.getRelevantDescription(), Style.EMPTY.withColor(ChatFormatting.GRAY));
        }
        Component line = Component.translatable(this.trackerWithValueTranslationKey(), desc, formattedValue).withStyle(ChatFormatting.GRAY);
        return List.of(Component.literal(" ").append(line));
    }

    public void addToShowcaseText(final Consumer<Component> consumer, final HolderLookup.Provider provider, final ItemStack itemStack) {
        this.getTooltip(provider, itemStack).forEach(consumer);
    }

    /// Even if the translation key `tooltip.strangeitems.tracker_with_value` is non-existent, it'll still format correctly.
    protected final String trackerWithValueTranslationKey() {
        String key = "tooltip.strangeitems.tracker_with_value";
        return Language.getInstance().has(key) ? key : "%s: %s";
    }

    public String getFormattedValue(final int value) {
        return this.statFormatter.format(value);
    }

    public Identifier getSaveId() {
        return this.saveId;
    }

    public void appendWithData(final RegistryAccess registryAccess, final ItemStack itemStack, final int count, final @Nullable Identifier data) {
        itemStack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, customData -> {
            if (TrackerEvents.ON_APPEND.invoker().onAppend(registryAccess, this, itemStack, count, data))
                return customData.update(nbt -> {
                    if (TrackerEvents.WRITE_NBT.invoker().writeNbt(registryAccess, this, itemStack, nbt))
                        this.writeToNbt(nbt, count, data);
                });
            return customData;
        });
    }

    public void append(final RegistryAccess registryAccess, final ItemStack itemStack) {
        this.appendWithData(registryAccess, itemStack, 1, null);
    }

    public Identifier getId(final HolderLookup.@Nullable Provider provider) {
        if (provider == null)
            return Identifier.withDefaultNamespace("unregistered");
        HolderLookup.RegistryLookup<Tracker> lookup = provider.lookupOrThrow(StrangeRegistryKeys.TRACKER_NEW);
        return lookup.listElements()
                .filter(t -> t.value() == this)
                .findFirst()
                .map(t -> t.key().identifier())
                .orElseThrow();
    }

    public Trigger getTrigger() {
        return this.trigger;
    }

    public HolderSet<Item> getTrackingItems() {
        return this.itemsToTrack;
    }

    @Environment(EnvType.CLIENT)
    protected MutableComponent getRelevantDescription() {
        if (Minecraft.getInstance().hasAltDown())
            return this.altDescription.map(Component::copy).orElse(this.description.copy());
        return this.description.copy();
    }

    protected static <T extends Tracker> RecordCodecBuilder<T, StatFormatter> formatterCodec() {
        return StrangeRegistries.STAT_FORMATTER.byNameCodec().optionalFieldOf("value_formatter", StatFormatters.DEFAULT).forGetter(a -> a.statFormatter);
    }

    protected static <T extends Tracker> RecordCodecBuilder<T, Identifier> saveIdCodec() {
        return Identifier.CODEC.validate(Tracker::validateSaveId).fieldOf("save_id").forGetter(a -> a.saveId);
    }

    protected static <T extends Tracker> RecordCodecBuilder<T, Component> descriptionCodec() {
        return ComponentSerialization.CODEC.fieldOf("description").forGetter(a -> a.description);
    }

    protected static <T extends Tracker> RecordCodecBuilder<T, Optional<Component>> altDescriptionCodec() {
        return ComponentSerialization.CODEC.optionalFieldOf("alt_description").forGetter(a -> a.altDescription);
    }

    protected static <T extends Tracker> RecordCodecBuilder<T, Trigger> triggerCodec() {
        return StrangeRegistries.TRIGGER.byNameCodec().fieldOf("trigger").forGetter(Tracker::getTrigger);
    }

    protected static <T extends Tracker> RecordCodecBuilder<T, HolderSet<Item>> itemsToTrackCodec() {
        return RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items_to_track").forGetter(a -> a.itemsToTrack);
    }

    protected static <T extends Tracker> MapCodec<T> noAdditionalArgsCodec(Function6<Trigger, Component, Optional<Component>, StatFormatter, Identifier, HolderSet<Item>, T> constructor) {
        return RecordCodecBuilder.mapCodec(a -> a.group(
                triggerCodec(),
                descriptionCodec(),
                altDescriptionCodec(),
                formatterCodec(),
                saveIdCodec(),
                itemsToTrackCodec()
        ).apply(a, constructor));
    }

    protected static DataResult<Identifier> validateSaveId(final Identifier identifier) {
        var dataVersion = validateIdNotThis(identifier, StrangeUtil.DATA_VERSION);
        var hasAllTrack = validateIdNotThis(identifier, StrangeUtil.HAS_ALL_TRACKERS);
        var isCollector = validateIdNotThis(identifier, StrangeUtil.COLLECTORS_ITEM);
        if (dataVersion.isEmpty() && hasAllTrack.isEmpty() && isCollector.isEmpty())
            return DataResult.success(identifier);
        return dataVersion.orElse(
                hasAllTrack.orElse(
                        isCollector.orElse(
                                DataResult.error(() -> "Tracker save id is set to not allowed value: " + identifier)
                        )
                )
        );
    }

    private static Optional<DataResult<Identifier>> validateIdNotThis(final Identifier checkingId, final Identifier idNotToBe) {
        if (idNotToBe.equals(checkingId))
            return Optional.of(DataResult.error(() -> "Tracker save id can't be " + idNotToBe));
        return Optional.empty();
    }

    @Environment(EnvType.CLIENT)
    public static void itemTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> list) {
        int[] trackerAddIndex = {1};
        if (StrangeUtil.isCollectors(itemStack)) {
            MutableComponent itemName = itemStack.getHoverName().copy();
            MutableComponent name = Component.empty();
            name.append(itemName);
            if (itemStack.has(DataComponents.CUSTOM_NAME)) {
                name.withStyle(ChatFormatting.ITALIC);
            }
            name.withStyle(ChatFormatting.DARK_RED);
            list.set(0, name);
            if (itemStack.has(DataComponents.CUSTOM_NAME)) {
                MutableComponent name2 = Component.translatable("tooltip.strangeitems.collectors_item.item_name", itemStack.getItemName());
                name2.withStyle(ChatFormatting.DARK_RED);
                list.add(1, name2);
                trackerAddIndex[0] += 1;
            }
        }
        if (!itemStack.has(DataComponents.CUSTOM_DATA))
            return;
        if (StrangeUtil.isKeyDown(StrangeItemsClient.SHOW_TRACKER_SCREEN))
            Minecraft.getInstance().setScreenAndShow(new StatShowcaseScreen(itemStack));
        if (!StrangeOptions.showTrackersInTooltip())
            return;

        List<Holder<Tracker>> trackersToShow = StrangeUtil.getTrackersForItem(tooltipContext.registries(), itemStack, StrangeOptions.showTrackerIfZero());
        trackersToShow = trackersToShow.stream()
                .filter(trackerHolder -> StrangeConfig.HIDDEN_TRACKERS.shouldShowForItem(itemStack.typeHolder(), trackerHolder))
                .toList();
        if (trackersToShow.isEmpty())
            return; // ignore further logic if no trackers.

        CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int dataVersion = nbt.getIntOr(StrangeUtil.DATA_VERSION_TAG, 0);
        list.add(trackerAddIndex[0], Component.translatable("tooltip.strangeitems.strange_trackers").append(":").withStyle(ChatFormatting.GRAY)); // header
        trackerAddIndex[0] += 1;
        if (dataVersion < StrangeItems.DATA_VERSION) { // old data version warning
            List<FormattedText> lines = Minecraft.getInstance().font.getSplitter().splitLines(Component.translatable("tooltip.strangeitems.old_data_version"), 140, Style.EMPTY);
            lines.forEach(line -> {
                list.add(trackerAddIndex[0], Component.literal(line.getString()).withStyle(ChatFormatting.RED));
                trackerAddIndex[0] += 1;
            });
        }
        Consumer<Holder<Tracker>> addToTooltip = trackerHolder -> {
            Tracker tracker = trackerHolder.value();
            list.addAll(trackerAddIndex[0], tracker.getTooltip(tooltipContext.registries(), itemStack));
            trackerAddIndex[0]++;
        };
        HolderSet<Tracker> tooltipOrder = StrangeUtil.getTooltipOrder(tooltipContext.registries(), TrackerTags.TOOLTIP_ORDER);
        for (Holder<Tracker> trackerHolder : tooltipOrder) {
            if (trackersToShow.contains(trackerHolder))
                addToTooltip.accept(trackerHolder);
        }
        for (Holder<Tracker> trackerHolder : trackersToShow) {
            if (!tooltipOrder.contains(trackerHolder))
                addToTooltip.accept(trackerHolder);
        }
        MutableComponent keyText = StrangeItemsClient.SHOW_TRACKER_SCREEN.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.GRAY);
        MutableComponent showMoreText = Component.empty().append(" ").append(Component.translatable("tooltip.strangeitems.show_more", keyText));
        list.add(trackerAddIndex[0], showMoreText.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)); // show more
        trackerAddIndex[0] += 1;
    }
}
