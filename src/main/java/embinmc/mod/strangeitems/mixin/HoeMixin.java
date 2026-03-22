package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HoeItem.class)
public class HoeMixin {
    @Inject(at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"),
        locals = LocalCapture.CAPTURE_FAILHARD,
        method = "useOn")
    public void tillDirtMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Trackers.DIRT_TILLED.appendTracker(context.getItemInHand());
    }
}
