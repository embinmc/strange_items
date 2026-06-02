package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trackers;
import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public class FishingBobberMixin {
    @Inject(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;setDeltaMovement(DDD)V"))
    public void bobberMixin(ItemStack rod, CallbackInfoReturnable<Integer> cir, @Local(name = "itemStack") ItemStack itemStack, @Local(name = "owner") Player owner) {
        Identifier caughtId = itemStack.typeHolder().unwrapKey().orElseThrow().identifier();
        Trigger.CATCH_ITEM_WITH_FISHING_ROD.appendWithData(owner.registryAccess(), rod, itemStack.count(), caughtId);
        if (itemStack.is(ItemTags.FISHES))
            Trigger.CATCH_FISH_WITH_FISHING_ROD.appendWithData(owner.registryAccess(), rod, itemStack.count(), caughtId);
    }
}
