package embinmc.mod.strangeitems;

import embinmc.mod.strangeitems.tracker.*;
import embinmc.mod.strangeitems.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.StatFormatter;

public interface StrangeRegistryKeys {
    @Deprecated(forRemoval = true)
    ResourceKey<Registry<LegacyTracker>> TRACKER = ResourceKey.createRegistryKey(Id.of("tracker_old"));
    ResourceKey<Registry<TrackerType<?>>> TRACKER_TYPE = ResourceKey.createRegistryKey(Id.of("tracker_type"));
    ResourceKey<Registry<Tracker>> TRACKER_NEW = ResourceKey.createRegistryKey(Id.of("tracker"));
    ResourceKey<Registry<Trigger>> TRIGGER = ResourceKey.createRegistryKey(Id.of("trigger"));
    ResourceKey<Registry<StatFormatter>> STAT_FORMATTER = ResourceKey.createRegistryKey(Id.of("stat_formatter"));
}
