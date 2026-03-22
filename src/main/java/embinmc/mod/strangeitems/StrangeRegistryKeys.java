package embinmc.mod.strangeitems;

import embinmc.mod.strangeitems.tracker.MapTracker;
import embinmc.mod.strangeitems.tracker.TimestampTracker;
import embinmc.mod.strangeitems.tracker.Tracker;
import embinmc.mod.strangeitems.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class StrangeRegistryKeys {
    /**
     * Registry key for the tracker registry.
     */
    public static final ResourceKey<Registry<Tracker>> TRACKER = ResourceKey.createRegistryKey(Id.of("tracker"));

    @Deprecated(forRemoval = true)
    public static final ResourceKey<Registry<TimestampTracker>> TIMESTAMP_TRACKER = ResourceKey.createRegistryKey(Id.of("timestamp_tracker"));

    @Deprecated(forRemoval = true)
    public static final ResourceKey<Registry<MapTracker>> MAP_TRACKER = ResourceKey.createRegistryKey(Id.of("map_tracker"));
}
