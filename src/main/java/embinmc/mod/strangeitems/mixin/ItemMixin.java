package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.StrangeItems;
import embinmc.mod.strangeitems.StrangeItemsComponents;
import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.client.StrangeOptions;
import embinmc.mod.strangeitems.client.config.StrangeConfig;
import embinmc.mod.strangeitems.tracker.*;
import embinmc.mod.strangeitems.util.StrangeUtil;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
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

@Mixin(ItemStack.class)
public abstract class ItemMixin {

    @Inject(at = @At(value = "TAIL"), method = "inventoryTick")
    public void fixTick(Level level, Entity owner, EquipmentSlot slot, CallbackInfo ci) {
        if (!level.isClientSide()) {
            ItemStack stack = (ItemStack)(Object) this;
            if (stack.has(StrangeItemsComponents.COLLECTORS_ITEM)) {
                stack.remove(StrangeItemsComponents.COLLECTORS_ITEM);
                stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, cd -> cd.update(nbt -> nbt.putBoolean(StrangeUtil.COLLECTORS_ITEM_TAG, true)));
                StrangeItems.LOGGER.info("Fixed collector's status of {}", stack);
            }
            if (stack.has(StrangeItemsComponents.HAS_ALL_TRACKERS)) {
                stack.remove(StrangeItemsComponents.HAS_ALL_TRACKERS);
                stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, cd -> cd.update(nbt -> nbt.putBoolean(StrangeUtil.HAS_ALL_TRACKERS_TAG, true)));
                StrangeItems.LOGGER.info("Fixed full tracking status of {}", stack);
            }
        }
    }

    @Inject(method = "getHoverName", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void collectorsName(CallbackInfoReturnable<Component> cir) {
        ItemStack stack = (ItemStack)(Object) this;
        if (StrangeUtil.isCollectors(stack)) {
            cir.setReturnValue(Component.translatable("tooltip.strangeitems.collectors_item.item_name", cir.getReturnValue()));
        }
    }
}
