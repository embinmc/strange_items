package embinmc.mod.strangeitems.event;

import embinmc.mod.strangeitems.tracker.Tracker;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class TrackerEvents {
    private TrackerEvents() {}

    public static final Event<@NotNull OnAppend> ON_APPEND = EventFactory.createArrayBacked(OnAppend.class, listeners -> (tracker, itemStack, increaseAmount) -> {
        for (OnAppend listener : listeners) {
            boolean result = listener.onAppend(tracker, itemStack, increaseAmount);
            if (!result) return false;
        }
        return true;
    });

    public static final Event<@NotNull WriteInt> WRITE_INT = EventFactory.createArrayBacked(WriteInt.class, listeners -> (tracker, itemStack, val) -> {
        for (WriteInt listener : listeners) {
            boolean result = listener.writeInt(tracker, itemStack, val);
            if (!result) return false;
        }
        return true;
    });

    public static final Event<@NotNull WriteNbt> WRITE_NBT = EventFactory.createArrayBacked(WriteNbt.class, listeners -> (tracker, itemStack, val) -> {
        for (WriteNbt listener : listeners) {
            boolean result = listener.writeNbt(tracker, itemStack, val);
            if (!result) return false;
        }
        return true;
    });

    public interface OnAppend {
        boolean onAppend(Tracker tracker, ItemStack itemStack, int increaseAmount);
    }

    public interface WriteInt {
        boolean writeInt(Tracker tracker, ItemStack itemStack, int val);
    }

    public interface WriteNbt {
        boolean writeNbt(Tracker tracker, ItemStack itemStack, Tag val);
    }
}
