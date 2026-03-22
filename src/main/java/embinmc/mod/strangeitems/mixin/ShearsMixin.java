package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ShearsItem.class)
public abstract class ShearsMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"),
        method = "useOn", locals = LocalCapture.CAPTURE_FAILHARD)
    public void shearMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, Level world, BlockPos blockPos, BlockState blockState, Block block, GrowingPlantHeadBlock abstractPlantStemBlock, Player playerEntity, ItemStack itemStack, BlockState blockState2) {
        Trackers.PLANTS_TRIMMED.appendTracker(itemStack);
    }
}
