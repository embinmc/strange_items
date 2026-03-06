package embinmc.mod.strangeitems.tracker;

import embinmc.mod.strangeitems.client.StrangeOptions;
import embinmc.mod.strangeitems.client.config.StrangeConfig;
import embinmc.mod.strangeitems.util.StrangeUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

public class TimestampTracker extends Tracker {
    public String map_id;
    public TimestampTracker(String id) {
        super(id, TrackerItemTags.CAN_TRACK_STATS);
        this.map_id = this.getId().toString() + "_map";
    }
    public TimestampTracker(String id, TagKey<Item> tag) {
        super(id, tag);
        this.map_id = this.getId().toString() + "_map";
    }

    @Override
    public void appendTracker(ItemStack stack) {
        super.appendTracker(stack, 1);
        if (this.shouldTrack(stack) || this.stackHasTracker(stack)) {
            if (StrangeConfig.in_depth_tracking) {
                int base_value = this.getTrackerValueInt(stack);
                CompoundTag nbt = this.getTrackerValueNbt(stack).copy();
                nbt.putLong(String.valueOf(base_value), Instant.now().getEpochSecond());
                this.setTrackerValueNbt(stack, nbt);
            }
        }
    }

    @Override
    public void setTrackerValueNbt(ItemStack stack, Tag value) {
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, comp -> comp.update(currentnbt -> currentnbt.put(this.map_id, value)));
    }

    public void append_tooltip_map(ItemStack stack, Consumer<Component> tooltip, CallbackInfo ci, TooltipFlag type) {
        if (this.shouldTrack(stack) && should_show_tooltip(stack)) {
            this.appendTooltipNoSpace(stack, tooltip, type);
            int size = this.getTrackerValueInt(stack) - 1;
            for (int i = size; i >= 0; i--) {
                String key = String.valueOf(i + 1);
                if ((size - StrangeOptions.mapElementLimit()) <= i || StrangeUtil.isTooltipScrollInstalled()) {
                    Component stat_text = Component.translatable("tooltip.strangeitems.unknown_value").withStyle(ChatFormatting.DARK_GRAY);
                    if (this.getTrackerValueNbt(stack).contains(key)) {
                        long tracker_value = this.getTrackerValueNbt(stack).getLong(key).orElse(0L);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss");
                        Instant time = Instant.ofEpochSecond(tracker_value);
                        stat_text = Component.literal(format.format(Date.from(time))).withStyle(ChatFormatting.GRAY);
                    }
                    MutableComponent tooltip_text = Component.literal(key).append(": ").withStyle(ChatFormatting.YELLOW);
                    tooltip.accept(Component.literal(" ").append(tooltip_text).append(stat_text));
                }
            }
            if (size >= (StrangeOptions.mapElementLimit() + 1) && !StrangeUtil.isTooltipScrollInstalled()) {
                tooltip.accept(Component.translatable("tooltip.strangeitems.map_cutoff", size - StrangeOptions.mapElementLimit()).withStyle(ChatFormatting.ITALIC));
            }
            StrangeUtil.addItemIdToTooltip(stack, tooltip, type);
            ci.cancel();
        }
    }

    public boolean should_show_tooltip(ItemStack stack) {
        return this.stackHasTracker(stack) && StrangeUtil.isKeyDown(this.get_key()) && StrangeConfig.in_depth_tracking && this.stack_has_map_tracker(stack);
    }

    @Override
    public CompoundTag getTrackerValueNbt(ItemStack stack) {
        if (!this.stackHasTracker(stack)) {
            return new CompoundTag();
        }
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(this.map_id).orElse(new CompoundTag());
    }

    public boolean stack_has_map_tracker(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(this.map_id);
    }

    @Override
    public void appendTooltip(ItemStack stack, Consumer<Component> tooltip) {
        if (this.shouldTrack(stack)) {
            if (this.stackHasTracker(stack) && StrangeConfig.in_depth_tracking && this.stack_has_map_tracker(stack)) {
                Component stat_text = Component.literal(this.getFormattedTrackerValue(stack)).withStyle(ChatFormatting.YELLOW);
                Component tooltip_text = this.getNameForTooltip().append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                Component control_text = Component.literal(" [").append(this.get_key().getTranslatedKeyMessage()).append("]").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
                tooltip.accept(Component.literal(" ").append(tooltip_text).append(stat_text).append(control_text));
            } else {
                super.appendTooltip(stack, tooltip);
            }
        }
    }

    public KeyMapping get_key() {
        return TrackerKeybindings.get_timestamp_keybind(this);
    }
}
