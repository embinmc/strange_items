package embinmc.mod.strangeitems.event;

import embinmc.mod.strangeitems.tracker.Tracker;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class TrackerEvents {
    private TrackerEvents() {}

    public static final Event<@NonNull OnAppend> ON_APPEND = EventFactory.createArrayBacked(OnAppend.class, listeners -> (registryAccess, tracker, itemStack, increaseAmount, data) -> {
        for (OnAppend listener : listeners) {
            boolean result = listener.onAppend(registryAccess, tracker, itemStack, increaseAmount, data);
            if (!result) return false;
        }
        return true;
    });

    public static final Event<@NonNull WriteNbt> WRITE_NBT = EventFactory.createArrayBacked(WriteNbt.class, listeners -> (registryAccess, tracker, itemStack, nbt) -> {
        for (WriteNbt listener : listeners) {
            boolean result = listener.writeNbt(registryAccess, tracker, itemStack, nbt);
            if (!result) return false;
        }
        return true;
    });

    public interface OnAppend {
        boolean onAppend(RegistryAccess registryAccess, Tracker tracker, ItemStack itemStack, int increaseAmount, @Nullable Identifier data);
    }

    public interface WriteNbt {
        boolean writeNbt(RegistryAccess registryAccess, Tracker tracker, ItemStack itemStack, CompoundTag nbt);
    }
}
