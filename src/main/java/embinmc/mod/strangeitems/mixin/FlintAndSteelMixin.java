package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public abstract class FlintAndSteelMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", ordinal = 1),
        method = "useOn")
    public void igniteFireMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getPlayer() != null)
            Trigger.IGNITE_FIRE.appendWithDimension(context.getPlayer(), context.getItemInHand());
        else
            Trigger.IGNITE_FIRE.append(context.getLevel().registryAccess(), context.getItemInHand());
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V", ordinal = 0),
        method = "useOn")
    public void igniteCampfireMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "state") BlockState blockState) {
        Trigger.RELIGHT_CAMPFIRE.appendWithData(context.getLevel().registryAccess(), context.getItemInHand(), 1, blockState.typeHolder().unwrapKey().orElseThrow().identifier());
    }
}
