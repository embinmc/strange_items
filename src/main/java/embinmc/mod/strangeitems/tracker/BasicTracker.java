package embinmc.mod.strangeitems.tracker;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class BasicTracker extends Tracker {
    public static final MapCodec<BasicTracker> CODEC = noAdditionalArgsCodec(BasicTracker::new);

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public BasicTracker(
            Trigger trigger, Component description, Optional<Component> altDescription,
            StatFormatter valueFormatter, Identifier saveId, HolderSet<Item> itemsToTrack
    ) {
        super(trigger, description, altDescription, valueFormatter, saveId, itemsToTrack);
    }

    @Override
    public TrackerType<?> getType() {
        return TrackerType.BASIC;
    }

    @Override
    public void writeToNbt(CompoundTag nbt, int count, @Nullable Identifier data) {
    }
}
