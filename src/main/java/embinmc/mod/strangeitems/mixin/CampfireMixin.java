package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CampfireBlock.class)
public abstract class CampfireMixin {
    @WrapOperation(method = "useItemOn", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/CampfireBlock;douse(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
    ))
    private void strangeitems$doodoo(
            Entity source, LevelAccessor level, BlockPos pos, BlockState state, Operation<Void> original,
            @Local(argsOnly = true, name = "itemStack") ItemStack itemStack
    ) {
        original.call(source, level, pos, state);
        if (!level.isClientSide()) {
            Identifier id = state.typeHolder().unwrapKey().orElseThrow().identifier();
            Trigger.PUT_OUT_CAMPFIRE.appendWithData(level.registryAccess(), itemStack, 1, id);
        }
    }
}
