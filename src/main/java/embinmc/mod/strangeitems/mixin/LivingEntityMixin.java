package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/Identifier;I)V"))
    private void shieldTrigger(ServerPlayer instance, Identifier identifier, int i, Operation<Void> original, @Local(argsOnly = true) DamageSource source, @Local(name = "itemInUse") ItemStack itemInUse) {
        original.call(instance, identifier, i);
        if (Stats.DAMAGE_BLOCKED_BY_SHIELD.equals(identifier))
            Trigger.DAMAGE_BLOCKED_BY_SHIELD.appendWithData(instance.registryAccess(), itemInUse, i, source.typeHolder().unwrapKey().orElseThrow().identifier());
    }
}
