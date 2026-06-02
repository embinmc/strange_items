package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxeItem.class)
public class AxeMixin {
    @WrapOperation(at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"),
        method = "useOn")
    public boolean blockStripMixin(Level instance, BlockPos pos, BlockState blockState, int updateFlags, Operation<Boolean> original, @Local(argsOnly = true) UseOnContext context) {
        Identifier strippedBlockId = context.getLevel().getBlockState(context.getClickedPos()).typeHolder().unwrapKey().orElseThrow().identifier();
        Trigger.STRIP_BLOCK_WITH_AXE.appendWithData(context.getLevel().registryAccess(), context.getItemInHand(), 1, strippedBlockId);
        return original.call(instance, pos, blockState, updateFlags);
    }
}
