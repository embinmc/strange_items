package embin.strangeitems.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import embin.strangeitems.StrangeItemsComponents;
import embin.strangeitems.StrangeRegistryKeys;
import embin.strangeitems.client.config.StrangeConfig;
import embin.strangeitems.tracker.*;
import embin.strangeitems.util.StrangeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.ItemEnchantments;

@Mixin(ItemStack.class)
public abstract class ItemMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
        ordinal = 0, shift = At.Shift.AFTER), method = "addDetailsToTooltip", cancellable = true)
    public void appendTooltipMixin(Item.TooltipContext context, TooltipDisplay displayComponent, Player player, TooltipFlag type, Consumer<Component> list, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object) this;
        if (stack.is(TrackerItemTags.CAN_TRACK_STATS) || StrangeUtil.hasAllTrackers(stack)) {
            for (Holder<Tracker> registryEntry : StrangeUtil.getTooltipOrder(context.registries(), StrangeRegistryKeys.TRACKER, TrackerTags.HAS_SPECIAL_TOOLTIP)) {
                if (StrangeConfig.HIDDEN_TRACKERS.shouldShowForItem(stack.getItem().builtInRegistryHolder(), registryEntry)) {
                    if (registryEntry.value() instanceof MapTracker mapTracker) {
                        if (mapTracker.shouldShowTooltip(stack)) {
                            mapTracker.appendTooltipMap(stack, list, ci, type);
                            return;
                        }
                    }
                    if (registryEntry.value() instanceof TimestampTracker tsTracker) {
                        if (tsTracker.should_show_tooltip(stack)) {
                            tsTracker.append_tooltip_map(stack, list, ci, type);
                            return;
                        }
                    }
                }
            }
            StrangeUtil.addAllTrackerTooltips(context, list, stack);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER), method = "getTooltipLines")
    public void nameColorMixin(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        ItemStack stack = (ItemStack)(Object) this;
        if (StrangeUtil.isCollectors(stack)) {
            list.removeLast();
            MutableComponent item_name = (MutableComponent) stack.getHoverName();
            MutableComponent name = Component.empty();
            name.append(item_name);
            if (stack.has(DataComponents.CUSTOM_NAME)) {
                name.withStyle(ChatFormatting.ITALIC);
            }
            name.withStyle(ChatFormatting.DARK_RED);
            list.add(name);
            if (stack.has(DataComponents.CUSTOM_NAME)) {
                ItemStack stack2 = stack.copy();
                stack2.remove(DataComponents.CUSTOM_NAME);

                MutableComponent name2 = Component.empty().append(stack2.getHoverName());
                name2.withStyle(ChatFormatting.DARK_RED);
                list.add(name2);
            }
        } /*else {
            if (stack.getComponents().toString().contains("strangeitems:")) {
                list.removeLast();
                Text item_name = stack.getName();
                MutableText name = Text.empty();
                if (stack.contains(DataComponentTypes.CUSTOM_NAME)) {
                    name.append(item_name);
                    name.formatted(Formatting.ITALIC);
                } else {
                    name.append(Text.translatable("tooltip.strangeitems.strange")).append(" ").append(item_name);
                }
                name.withColor(13593138);
                list.add(name);
                if (stack.contains(DataComponentTypes.CUSTOM_NAME)) {
                    MutableText name2 = Text.empty();
                    name2.append(Text.translatable("tooltip.strangeitems.strange")).append(" ");
                    name2.append(Text.translatable(stack.getTranslationKey()));
                    name2.withColor(13593138);
                    list.add(name2);
                }
            }
        }
        */
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            ordinal = 15, shift = At.Shift.BEFORE), method = "addDetailsToTooltip")
    public void enchantTooltipMixin(Item.TooltipContext context, TooltipDisplay displayComponent, Player player, TooltipFlag type, Consumer<Component> textConsumer, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object) this;
        if (stack.isEnchanted() || !stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty()) {
            textConsumer.accept(Component.translatable("tooltip.strangeitems.enchantments").append(":").withStyle(ChatFormatting.GRAY));
        }
    }

    /**
     * @author Embin
     * @reason bleh
     */
    @Overwrite()
    public Component getHoverName() {
        ItemStack stack = (ItemStack)(Object) this;

        Component text = stack.getCustomName();
        if (text != null) {
            return text;
        }
        if (StrangeUtil.isCollectors(stack)) {
            return Component.translatable("tooltip.strangeitems.collectors_item.item_name", stack.getItemName());
        }
        return stack.getItemName();
    }
}
