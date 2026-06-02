package embinmc.mod.strangeitems.network;

import embinmc.mod.strangeitems.util.Id;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record ClientboundSyncEnderChestPacket(List<ItemStack> contents) implements CustomPacketPayload {
    public static final Identifier ID = Id.of("sync_ender_chest");
    public static final CustomPacketPayload.Type<ClientboundSyncEnderChestPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncEnderChestPacket> CODEC = ItemStack.OPTIONAL_STREAM_CODEC
            .apply(ByteBufCodecs.list(54))
            .map(ClientboundSyncEnderChestPacket::new, ClientboundSyncEnderChestPacket::contents);

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void listen(ClientboundSyncEnderChestPacket packet, ClientPlayNetworking.Context context) {
        PlayerEnderChestContainer enderChest = context.player().getEnderChestInventory();
        int slot = 0;
        for (ItemStack itemStack : packet.contents()) {
            enderChest.setItem(slot, itemStack.copy());
            slot++;
        }
    }

    public static void sync(final ServerPlayer serverPlayer) {
        List<ItemStack> items = Util.make(new ArrayList<>(27), list -> {
           for (ItemStack itemStack : serverPlayer.getEnderChestInventory()) {
               list.add(itemStack.copy());
           }
        });
        ServerPlayNetworking.send(serverPlayer, new ClientboundSyncEnderChestPacket(items));
    }
}
