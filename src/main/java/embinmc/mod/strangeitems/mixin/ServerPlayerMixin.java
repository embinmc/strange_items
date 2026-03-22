package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.event.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "drop*", at = @At(value = "HEAD"), cancellable = true)
    public void dropItemMixin(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        ServerPlayer player = (ServerPlayer)(Object) this;
        InteractionResult result = ServerPlayerEvents.ON_DROP_ITEM.invoker().onDrop(player, stack);
        if (result == InteractionResult.FAIL) {
            cir.cancel();
        }
    }

    @Inject(method = "doTick", at = @At(value = "HEAD"), cancellable = true)
    public void tickEvents(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object) this;
        InteractionResult result = ServerPlayerEvents.ON_TICK.invoker().tick(player);
        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}
