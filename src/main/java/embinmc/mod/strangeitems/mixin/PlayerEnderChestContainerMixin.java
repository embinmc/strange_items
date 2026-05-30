package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.network.ClientboundSyncEnderChestPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEnderChestContainer.class)
public abstract class PlayerEnderChestContainerMixin {
    @Inject(method = "stopOpen", at = @At("TAIL"))
    public void syncToClient(ContainerUser containerUser, CallbackInfo ci) {
        if (containerUser instanceof ServerPlayer player)
            ClientboundSyncEnderChestPacket.sync(player);
    }
}
