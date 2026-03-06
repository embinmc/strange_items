package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow @NotNull public abstract ItemStack getWeaponItem();

    @Inject(method = "damageStatsAndHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/Identifier;I)V"))
    public void hitMobMixin(Entity target, float oldLivingEntityHealth, CallbackInfo ci, @Local(ordinal = 1) float actualDamage) {
        Trackers.MOBS_HIT.appendTracker(this.getWeaponItem());
        Trackers.DAMAGE_DEALT.appendTracker(this.getWeaponItem(), Math.round(actualDamage * 10.0F));
    }

    @Inject(method = "killedEntity", at = @At(value = "HEAD"))
    public void killOtherMobMixin(ServerLevel world, LivingEntity other, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Trackers.MOBS_KILLED.appendTracker(this.getWeaponItem(), BuiltInRegistries.ENTITY_TYPE.getKey(other.getType()).toString());
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/Identifier;I)V", ordinal = 1))
    public void damageMixin(ServerLevel world, DamageSource source, float amount, CallbackInfo ci) {
        Player player = (Player)(Object) this;
        List<ItemStack> armorItems = List.of(
                player.getItemBySlot(EquipmentSlot.HEAD),
                player.getItemBySlot(EquipmentSlot.CHEST),
                player.getItemBySlot(EquipmentSlot.LEGS),
                player.getItemBySlot(EquipmentSlot.FEET)
        );
        for (ItemStack stack : armorItems) {
            if (!stack.isEmpty()) {
                Trackers.DAMAGE_TAKEN.appendTracker(stack, Math.round(amount * 10.0F));
            }
        }
    }

    @Inject(method = "causeFallDamage", at = @At(value = "HEAD"))
    public void whenFallen(double fallDistance, float damagePerDistance, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player)(Object) this;
        ItemStack feet_stack = player.getItemBySlot(EquipmentSlot.FEET);
        if (!feet_stack.isEmpty()) {
            Trackers.DISTANCE_FALLEN.appendTracker(feet_stack, Mth.floor(fallDistance * 100.0));
        }
    }
}
