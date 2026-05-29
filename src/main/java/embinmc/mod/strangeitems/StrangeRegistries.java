package embinmc.mod.strangeitems;

import embinmc.mod.strangeitems.tracker.*;
import embinmc.mod.strangeitems.util.StatFormatters;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.stats.StatFormatter;

public class StrangeRegistries {

    /**
     * Registry containing all registered trackers.
     * @see Trackers
     */
    public static final Registry<LegacyTracker> TRACKER = FabricRegistryBuilder.create(
            StrangeRegistryKeys.TRACKER
    ).attribute(RegistryAttribute.OPTIONAL).buildAndRegister();

    public static final Registry<TrackerType<?>> TRACKER_TYPE = FabricRegistryBuilder.create(
            StrangeRegistryKeys.TRACKER_TYPE
    ).attribute(RegistryAttribute.OPTIONAL).buildAndRegister();

    public static final Registry<Trigger> TRIGGER = FabricRegistryBuilder.create(
            StrangeRegistryKeys.TRIGGER
    ).attribute(RegistryAttribute.OPTIONAL).buildAndRegister();

    public static final Registry<StatFormatter> STAT_FORMATTER = FabricRegistryBuilder.create(
            StrangeRegistryKeys.STAT_FORMATTER
    ).attribute(RegistryAttribute.OPTIONAL).buildAndRegister();

    public static void acknowledgeRegistries() {
        StrangeItems.LOGGER.info("Creating registries...");

        StatFormatters.NONE.format(0);
        TrackerType.BASIC.codec();
        var _ = Trigger.NEVER.getClass();

        DynamicRegistries.registerSynced(StrangeRegistryKeys.TRACKER_NEW, Tracker.CODEC, DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
    }
}
