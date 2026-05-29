package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
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
        if (context.getPlayer() != null)
            Trigger.CREATE_PATH_BLOCK.appendWithDimension(context.getPlayer(), context.getItemInHand());
        else
            Trigger.CREATE_PATH_BLOCK.append(context.getLevel().registryAccess(), context.getItemInHand());
    }

    @Inject(at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/block/CampfireBlock;dowse(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"),
        method = "useOn")
    public void putOutCampfire(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "blockState") BlockState blockState) {
        Trigger.PUT_OUT_CAMPFIRE.appendWithData(context.getLevel().registryAccess(), context.getItemInHand(), 1, blockState.typeHolder().unwrapKey().orElseThrow().identifier());
    }
}
