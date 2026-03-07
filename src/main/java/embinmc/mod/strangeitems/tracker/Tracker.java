package embinmc.mod.strangeitems.tracker;

import embinmc.mod.strangeitems.StrangeItemsComponents;
import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.client.StrangeItemsClient;
import embinmc.mod.strangeitems.event.TrackerEvents;
import embinmc.mod.strangeitems.util.Id;
import embinmc.mod.strangeitems.util.StrangeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Base Tracker class
 */
public class Tracker {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tracker.class);
    public String id;

    /**
     * The StatFormatter used when displaying a tracker's value in its tooltip.
     * @see StatFormatter
     */
    public StatFormatter stat_formatter = StatFormatter.DEFAULT;

    /**
     * The multiplier applied to the value shown on the tracker tooltip.
     */
    public int formatted_value_multiplier = 1;
    public int default_value = 0;

    /**
     * The item tag that controls whether an item should have a certain tracker.
     */
    public TagKey<Item> item_tag;

    public Tracker(String id, TagKey<Item> tag, StatFormatter stat_formatter, int formatted_value_multiplier) {
        this.stat_formatter = stat_formatter;
        this.formatted_value_multiplier = formatted_value_multiplier;
        this.item_tag = tag;
        this.id = Id.of(id).toString();
    }
    public Tracker(String id, TagKey<Item> tag) {
        this.item_tag = tag;
        this.id = Id.of(id).toString();
    }

    public void setTrackerValueInt(ItemStack stack, int value) {
        if (!TrackerEvents.WRITE_INT.invoker().writeInt(this, stack, value)) return;
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, comp -> comp.update(currentnbt -> currentnbt.putInt(this.toString(), value)));
    }

    public void setTrackerValueNbt(ItemStack stack, Tag value) {
        if (!TrackerEvents.WRITE_NBT.invoker().writeNbt(this, stack, value)) return;
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, comp -> comp.update(currentnbt -> currentnbt.put(this.toString(), value)));
    }

    public StatFormatter getStatFormatter() {
        return this.stat_formatter;
    }

    public String toString() {
        return this.getId().toString();
    }

    @Deprecated(forRemoval = true)
    public String to_string() {
        return this.toString();
    }

    public Identifier getId() {
        Identifier id = StrangeRegistries.TRACKER.getKey(this);
        if (id != null) return id;
        return Id.of(this.id);
    }

    public String getTranslationKey() {
        return Util.makeDescriptionId("tracker", this.getId());
    }

    /**
     * Checks if the given stack has any tracker data on it.
     * @param stack The item stack to check for.
     * @return <code>true</code> if the stack has the tracker;
     * <code>false</code> if it doesn't
     */
    public boolean stackHasTracker(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(this.toString());
    }

    public int getTrackerValueInt(ItemStack stack) {
        if (!this.stackHasTracker(stack)) {
            return this.default_value;
        }
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(this.toString()).orElse(0);
    }

    public CompoundTag getTrackerValueNbt(ItemStack stack) {
        if (!this.stackHasTracker(stack)) {
            return new CompoundTag();
        }
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(this.toString()).orElse(new CompoundTag());
    }

    /**
     * Increments the tracker for an item stack by a specified amount when called.
     * @param stack Item stack to increment the tracker on.
     * @param add_amount Amount to add.
     */
    public void appendTracker(ItemStack stack, int add_amount) {
        if (this.shouldTrack(stack)) {
            if (TrackerEvents.ON_APPEND.invoker().onAppend(this, stack, add_amount)) {
                int tracker_count = this.getTrackerValueInt(stack) + add_amount;
                this.setTrackerValueInt(stack, tracker_count);
            }
        }
    }

    /**
     * Increments the tracker for an item stack by 1 when called.
     * @param stack Item stack to increment the tracker on.
     */
    public void appendTracker(ItemStack stack) {
        this.appendTracker(stack, 1);
    }

    public String getFormattedTrackerValue(ItemStack stack) {
        return this.getStatFormatter().format(this.getTrackerValueInt(stack) * this.formatted_value_multiplier);
    }

    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        if (this.shouldTrack(stack)) {
            Component stat_text = Component.literal(this.getFormattedTrackerValue(stack)).withStyle(ChatFormatting.YELLOW);
            Component tooltip_text = this.getNameForTooltip().append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
            tooltip.accept(Component.literal(" ").append(tooltip_text).append(stat_text));
        }
    }

    protected MutableComponent getNameForTooltip() {
        if (StrangeUtil.isKeyDown(StrangeItemsClient.show_tracker_ids)) {
            Identifier id = StrangeRegistries.TRACKER.getKey(this);
            if (id != null) {
                return Component.literal(id.toString()).withStyle(ChatFormatting.DARK_GRAY);
            } else {
                return Component.translatable(this.getTranslationKey()).withStyle(ChatFormatting.GRAY);
            }
        } else {
            return Component.translatable(this.getTranslationKey()).withStyle(ChatFormatting.GRAY);
        }
    }

    public void appendTooltipNoSpace(ItemStack stack, Consumer<Component> tooltip, TooltipFlag type) {
        if (this.shouldTrack(stack)) {
            Component stat_text = Component.literal(this.getFormattedTrackerValue(stack)).withStyle(ChatFormatting.YELLOW);
            MutableComponent tooltip_text = Component.translatable(this.getTranslationKey()).append(": ").withStyle(ChatFormatting.GRAY);
            //MutableText tooltip_text = this.get_name_for_tooltip().append(Text.literal(": ").formatted(Formatting.GRAY));
            tooltip.accept(tooltip_text.append(stat_text));
        }
    }

    @Deprecated(forRemoval = true)
    public void convert_legacy_tracker(ItemStack stack, DataComponentType<Integer> legacy_component, boolean rarity_fix) {
        if (stack.has(legacy_component)) {
            if (rarity_fix) {
                stack.set(DataComponents.RARITY, stack.getItem().components().get(DataComponents.RARITY));
            }
            int legacy_data = stack.getOrDefault(legacy_component, 0);
            this.setTrackerValueInt(stack, this.getTrackerValueInt(stack) + legacy_data);
            stack.remove(legacy_component);
        }
    }

    /**
     * Converts the data of a specified legacy tracker component to the new data format, if the specified stack has legacy data.
     * @param stack Item stack to check for.
     * @param legacy_component The tracker component to convert.
     */
    @Deprecated(forRemoval = true)
    public void convert_legacy_tracker(ItemStack stack, DataComponentType<Integer> legacy_component) {
        this.convert_legacy_tracker(stack, legacy_component, false);
    }

    public boolean shouldTrack(ItemStack stack) {
        return stack.is(this.item_tag) || this.stackHasTracker(stack) || stack.has(StrangeItemsComponents.HAS_ALL_TRACKERS);
    }

    public boolean isIn(TagKey<Tracker> tag) {
        return StrangeRegistries.TRACKER.wrapAsHolder(this).is(tag);
    }
}
