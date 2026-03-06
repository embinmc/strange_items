package embin.strangeitems;

import embin.strangeitems.tracker.Tracker;
import embin.strangeitems.tracker.Trackers;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;

public class StrangeRegistries {

    /**
     * Registry containing all registered trackers.
     * @see Trackers
     */
    public static final Registry<Tracker> TRACKER = FabricRegistryBuilder.create(
            StrangeRegistryKeys.TRACKER
    ).attribute(RegistryAttribute.OPTIONAL).buildAndRegister();

    public static void acknowledgeRegistries() {
        StrangeItems.LOGGER.info("Creating registry \"strangeitems:tracker\"");
    }
}
