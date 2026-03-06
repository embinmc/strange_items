package embin.strangeitems;

import embin.strangeitems.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Unit;

public class StrangeItemsComponents {
    /*
    @Deprecated(forRemoval = true)
    public static final ComponentType<Integer> BLOCKS_MINED = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        ConvertNamespace.convert("strangeitems:blocks_mined"),
        ComponentType.<Integer>builder().codec(Codec.INT).build()
    );
    */

    @Deprecated
    public static final DataComponentType<Unit> COLLECTORS_ITEM = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("strangeitems:collectors_item"),
        DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
    );

    /**
     * Items with this component will have every registered tracker on them and will increment them accordingly if they're capable of doing so.
     */
    public static final DataComponentType<Unit> HAS_ALL_TRACKERS = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("strangeitems:has_all_trackers"),
        DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
    );

    protected static void init() {
        //StrangeItems.LOGGER.info("Loading components and legacy components for Strange Items");
    }
}
