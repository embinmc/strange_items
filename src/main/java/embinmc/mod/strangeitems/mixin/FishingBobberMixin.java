package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public class FishingBobberMixin {
    @Inject(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"))
    public void bobberMixin(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        Trackers.TIMES_FISHING_ROD_CAUGHT_SOMETHING.appendTracker(usedItem);
    }

    @Inject(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/Identifier;I)V"))
    public void fishCaughtMixin(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        Trackers.FISH_CAUGHT.appendTracker(usedItem);
    }
}
