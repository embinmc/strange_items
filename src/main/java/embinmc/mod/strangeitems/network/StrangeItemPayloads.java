package embinmc.mod.strangeitems.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class StrangeItemPayloads {
    public static void register() {
        PayloadTypeRegistry.clientboundPlay()
                .register(ClientboundSyncEnderChestPacket.TYPE, ClientboundSyncEnderChestPacket.CODEC);
    }
}
