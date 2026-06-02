package embinmc.mod.strangeitems.tracker;

import com.mojang.serialization.MapCodec;
import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.util.Id;
import net.minecraft.core.Registry;

public interface TrackerType<T extends Tracker> {
    /// A basic tracker. Records no additional information when appended.
    TrackerType<BasicTracker> BASIC = register("basic", BasicTracker.CODEC);

    /// Records the current timestamp when appended.
    TrackerType<TimestampTracker> TIMESTAMP = register("timestamp", TimestampTracker.CODEC);

    /// Records whatever [net.minecraft.resources.Identifier] is supplied when appended.
    TrackerType<IdMapTracker> ID_MAP = register("id_map", IdMapTracker.CODEC);

    MapCodec<T> codec();

    private static <T extends Tracker> TrackerType<T> register(final String id, final MapCodec<T> typeCodec) {
        return Registry.register(StrangeRegistries.TRACKER_TYPE, Id.of(id), () -> typeCodec);
    }
}
