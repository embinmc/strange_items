package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public class ShovelMixin {
    @Inject(at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"),
        method = "useOn")
    public void pathCreationMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Trackers.PATHS_CREATED.appendTracker(context.getItemInHand());
    }

    @Inject(at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/block/CampfireBlock;dowse(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"),
        method = "useOn")
    public void putOutCampfire(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Trackers.CAMPFIRES_PUT_OUT.appendTracker(context.getItemInHand());
    }
}
