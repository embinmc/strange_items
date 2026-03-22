package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Armadillo.class)
public class ArmadilloMixin {
    @Inject(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void armadilloBrush(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, ItemStack itemStack) {
        Trackers.ARMADILLOS_BRUSHED.appendTracker(itemStack);
    }
}
