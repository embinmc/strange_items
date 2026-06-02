package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public class BrushMixin {
    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
    public void brushMixin(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
        Trigger.ATTEMPT_BRUSH_BLOCK.appendWithDimension(user, stack);
    }
}
