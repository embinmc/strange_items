package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;ceil(D)I", ordinal = 0), method = "onHitEntity")
    public void onHitMixin(EntityHitResult entityHitResult, CallbackInfo ci) {
        AbstractArrow ppe = (AbstractArrow)(Object) this;
        if (ppe.getOwner() != null) {
            if (ppe.getOwner().getWeaponItem() != null) {
                Identifier hitEntityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityHitResult.getEntity().getType());
                Trackers.SHOTS_HIT.appendTracker(ppe.getOwner().getWeaponItem(), hitEntityId.toString());
            }
        }
    }
}
