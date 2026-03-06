package embinmc.mod.strangeitems.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ServerPlayerEvents {
    private ServerPlayerEvents() {}

    public static final Event<@NotNull OnTick> ON_TICK = EventFactory.createArrayBacked(OnTick.class, listeners -> player -> {
        for (OnTick listener : listeners) {
            InteractionResult result = listener.tick(player);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    });

    public static final Event<@NotNull DropItem> ON_DROP_ITEM = EventFactory.createArrayBacked(DropItem.class, listeners -> (player, itemStack) -> {
        for (DropItem listener : listeners) {
            InteractionResult result = listener.onDrop(player, itemStack);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    });

    public interface OnTick {
        InteractionResult tick(ServerPlayer player);
    }

    public interface DropItem {
        InteractionResult onDrop(ServerPlayer player, ItemStack itemStack);
    }
}
