package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AxeItem.class)
public class AxeMixin {
    @Inject(at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"),
        method = "useOn")
    public void blockStripMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Identifier strippedBlockId = context.getLevel().getBlockState(context.getClickedPos()).typeHolder().unwrapKey().orElseThrow().identifier();
        Trigger.STRIP_BLOCK_WITH_AXE.appendWithData(context.getLevel().registryAccess(), context.getItemInHand(), 1, strippedBlockId);
    }
}
