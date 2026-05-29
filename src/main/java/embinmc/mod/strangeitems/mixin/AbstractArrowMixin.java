package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @Unique private ItemStack weaponNonCopy;

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;DDDLnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V", at = @At("RETURN"))
    private void addNonCopy(
            EntityType<? extends AbstractArrow> type, double x, double y, double z,
            Level level, ItemStack pickupItemStack, ItemStack firedFromWeapon, CallbackInfo ci
    ) {
        this.weaponNonCopy = firedFromWeapon;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;ceil(D)I", ordinal = 0), method = "onHitEntity")
    public void onHitMixin(EntityHitResult entityHitResult, CallbackInfo ci) {
        AbstractArrow ppe = (AbstractArrow)(Object) this;
        if (ppe.getOwner() != null) {
            if (ppe.getOwner().getWeaponItem() != null) {
                Identifier hitEntityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityHitResult.getEntity().getType());
                Trigger.ARROW_HIT_MOB.appendWithData(ppe.getOwner().registryAccess(), this.weaponNonCopy, 1, hitEntityId);
            }
        }
    }
}
