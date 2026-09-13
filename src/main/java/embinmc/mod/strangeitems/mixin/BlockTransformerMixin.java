package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockTransformer.class)
public abstract class BlockTransformerMixin {
    @WrapOperation(method = "transformBlock", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancements/triggers/ItemUsedOnLocationTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemInstance;)V"
    ))
    private void doodoo(
            ItemUsedOnLocationTrigger instance, ServerPlayer player,
            BlockPos pos, ItemInstance tool, Operation<Void> original,
            @Local(name = "updatedShape") BlockState updatedShape,
            @Local(name = "level") Level level,
            @Local(name = "itemInHand") ItemStack itemInHand
            ) {
        original.call(instance, player, pos, tool);
        Holder<BlockTransformer> holder = tool.get(DataComponents.BLOCK_TRANSFORMER);
        if (holder == null)
            return;
        if (holder.is(BlockTransformers.AXE)) {
            Identifier oldId = level.getBlockState(pos).typeHolder().unwrapKey().orElseThrow().identifier();
            Trigger.STRIP_BLOCK_WITH_AXE.appendWithData(level.registryAccess(), itemInHand, 1, oldId);
        } else if (holder.is(BlockTransformers.SHOVEL)) {
            if (updatedShape.is(BlockItemIds.DIRT_PATH.block()))
                Trigger.CREATE_PATH_BLOCK.appendWithDimension(player, itemInHand);
        } else if (holder.is(BlockTransformers.HOE)) {
            Trigger.TILL_DIRT.appendWithDimension(player, itemInHand);
        }
    }
}
