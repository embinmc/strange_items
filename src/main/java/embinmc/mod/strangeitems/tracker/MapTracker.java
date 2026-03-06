package embinmc.mod.strangeitems.tracker;

import embinmc.mod.strangeitems.client.StrangeItemsClient;
import embinmc.mod.strangeitems.client.StrangeOptions;
import embinmc.mod.strangeitems.client.config.StrangeConfig;
import embinmc.mod.strangeitems.util.StrangeUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.component.DataComponents;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

public class MapTracker extends Tracker {
    public String map_id;
    public String translation_prefix;

    public MapTracker(String id, String translate_prefix, TagKey<Item> tag, StatFormatter stat_formatter) {
        super(id, tag);
        this.map_id = this.getId().toString() + "_map";
        this.translation_prefix = translate_prefix;
        this.stat_formatter = stat_formatter;
    }

    public MapTracker(String id, String translate_prefix, TagKey<Item> tag) {
        super(id, tag);
        this.map_id = this.getId().toString() + "_map";
        this.translation_prefix = translate_prefix;
    }

    public MapTracker(String id, String translate_prefix) {
        super(id, TrackerItemTags.CAN_TRACK_STATS);
        this.map_id = this.getId().toString() + "_map";
        this.translation_prefix = translate_prefix;
    }

    public void appendTracker(ItemStack stack, String key) {
        super.appendTracker(stack, 1);
        if (this.shouldTrack(stack) || this.stackHasTracker(stack)) {
            if (StrangeConfig.in_depth_tracking) {
                int tracker_count = this.getTrackerValueNbt(stack).getInt(key).orElse(0) + 1;
                CompoundTag nbt = this.getTrackerValueNbt(stack).copy();
                nbt.putInt(key, tracker_count);
                this.setTrackerValueNbt(stack, nbt);
            }
        }
    }

    @Override
    public void setTrackerValueNbt(ItemStack stack, Tag value) {
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, comp -> comp.update(currentnbt -> currentnbt.put(this.map_id, value)));
    }

    @Override
    public CompoundTag getTrackerValueNbt(ItemStack stack) {
        if (!this.stackHasTracker(stack)) {
            return new CompoundTag();
        }
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(this.map_id).orElse(new CompoundTag());
    }

    public void appendTooltipMap(ItemStack stack, Consumer<Component> tooltip, CallbackInfo ci, TooltipFlag type) {
        if (this.shouldTrack(stack) && shouldShowTooltip(stack)) {
            this.appendTooltipNoSpace(stack, tooltip, type);
            CompoundTag nbtCompound = this.getTrackerValueNbt(stack);
            int index = 1;
            for (String key : StrangeUtil.getSortedKeys(nbtCompound)) {
                if (index <= StrangeOptions.mapElementLimit() || StrangeUtil.isTooltipScrollInstalled()) {
                    String translation_key = Identifier.parse(key).toLanguageKey(this.translation_prefix);
                    Component stat_text = Component.literal(this.getFormattedTrackerValueNbt(stack, key)).withStyle(ChatFormatting.YELLOW);
                    MutableComponent tooltip_text = Component.literal(key);
                    if (Language.getInstance().has(translation_key) && !StrangeUtil.isKeyDown(StrangeItemsClient.show_tracker_ids)) {
                        tooltip_text = Component.translatable(translation_key).withStyle(ChatFormatting.GRAY);
                    }
                    if (StrangeUtil.isKeyDown(StrangeItemsClient.show_tracker_ids)) {
                        tooltip_text.withStyle(ChatFormatting.DARK_GRAY);
                    }
                    tooltip_text.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                    tooltip.accept(Component.literal(" ").append(tooltip_text).append(stat_text));
                }
                index++;
            }
            if (index > (StrangeOptions.mapElementLimit() + 1) && !StrangeUtil.isTooltipScrollInstalled()) {
                tooltip.accept(Component.translatable("tooltip.strangeitems.map_cutoff", index - (StrangeOptions.mapElementLimit() + 1)).withStyle(ChatFormatting.ITALIC));
            }
            StrangeUtil.addItemIdToTooltip(stack, tooltip, type);
            ci.cancel();
        }
    }

    public String getFormattedTrackerValueNbt(ItemStack stack, String key) {
        return this.getStatFormatter().format(this.getTrackerValueNbt(stack).getInt(key).orElse(0) * this.formatted_value_multiplier);
    }

    public boolean shouldShowTooltip(ItemStack stack) {
        return this.stackHasTracker(stack) && StrangeUtil.isKeyDown(this.getKeybinding()) && StrangeConfig.in_depth_tracking && this.stackHasMapTracker(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        if (this.shouldTrack(stack)) {
            if (this.stackHasTracker(stack) && StrangeConfig.in_depth_tracking && this.stackHasMapTracker(stack)) {
                Component stat_text = Component.literal(this.getFormattedTrackerValue(stack)).withStyle(ChatFormatting.YELLOW);
                Component tooltip_text = this.getNameForTooltip().append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                Component control_text = Component.literal(" [").append(this.getKeybinding().getTranslatedKeyMessage()).append("]").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
                tooltip.accept(Component.literal(" ").append(tooltip_text).append(stat_text).append(control_text));
            } else {
                super.appendTooltip(stack, tooltip);
            }
        }
    }

    public boolean stackHasMapTracker(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(this.map_id);
    }

    public KeyMapping getKeybinding() {
        return TrackerKeybindings.get_map_keybind(this);
    }
}
