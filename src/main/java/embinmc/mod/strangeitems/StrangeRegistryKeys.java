package embinmc.mod.strangeitems;

import embinmc.mod.strangeitems.tracker.*;
import embinmc.mod.strangeitems.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.StatFormatter;

public class StrangeRegistryKeys {
    /**
     * Registry key for the tracker registry.
     */
    public static final ResourceKey<Registry<LegacyTracker>> TRACKER = ResourceKey.createRegistryKey(Id.of("tracker_old"));
    public static final ResourceKey<Registry<TrackerType<?>>> TRACKER_TYPE = ResourceKey.createRegistryKey(Id.of("tracker_type"));
    public static final ResourceKey<Registry<Tracker>> TRACKER_NEW = ResourceKey.createRegistryKey(Id.of("tracker"));
    public static final ResourceKey<Registry<Trigger>> TRIGGER = ResourceKey.createRegistryKey(Id.of("trigger"));
    public static final ResourceKey<Registry<StatFormatter>> STAT_FORMATTER = ResourceKey.createRegistryKey(Id.of("stat_formatter"));

    @Deprecated(forRemoval = true)
    public static final ResourceKey<Registry<LegacyTimestampTracker>> TIMESTAMP_TRACKER = ResourceKey.createRegistryKey(Id.of("timestamp_tracker"));

    @Deprecated(forRemoval = true)
    public static final ResourceKey<Registry<LegacyMapTracker>> MAP_TRACKER = ResourceKey.createRegistryKey(Id.of("map_tracker"));
}
