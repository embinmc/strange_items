package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public abstract class ShearsMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"),
        method = "useOn")
    public void shearMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Identifier trimmedPlantId = context.getLevel().getBlockState(context.getClickedPos()).typeHolder()
                .unwrapKey()
                .orElseThrow()
                .identifier();
        Trigger.TRIM_PLANT.appendWithData(context.getLevel().registryAccess(), context.getItemInHand(), 1, trimmedPlantId);
    }
}
