package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trackers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"),
        method = "updateFallFlying", locals = LocalCapture.CAPTURE_FAILHARD)
    public void elytraMixin(CallbackInfo ci, int i, int j, List<EquipmentSlot> list, EquipmentSlot equipmentSlot) {
        LivingEntity livingentity = (LivingEntity)(Object) this;
        Trackers.TIME_FLOWN_WITH_ELYTRA.appendTracker(livingentity.getItemBySlot(equipmentSlot));
    }

    //@Inject(method = "onEquipStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FFJ)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void equipMixin(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo ci) {
        Trackers.TIMES_EQUIPPED.appendTracker(newStack, 1);
    }
}
