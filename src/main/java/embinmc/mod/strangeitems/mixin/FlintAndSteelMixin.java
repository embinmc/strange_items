package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FlintAndSteelItem.class)
public abstract class FlintAndSteelMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", ordinal = 1),
        method = "useOn", locals = LocalCapture.CAPTURE_FAILHARD)
    public void igniteFireMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, Player playerEntity, Level world, BlockPos blockPos, BlockState blockState) {
        Trackers.FIRES_LIT.appendTracker(context.getItemInHand());
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", ordinal = 0),
        method = "useOn", locals = LocalCapture.CAPTURE_FAILHARD)
    public void igniteCampfireMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, Player playerEntity, Level world, BlockPos blockPos, BlockState blockState) {
        Trackers.CAMPFIRES_LIT.appendTracker(context.getItemInHand());
    }
}
