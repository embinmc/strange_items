package embinmc.mod.strangeitems.client;

import embinmc.mod.strangeitems.util.Id;
import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VanillaOptionsAPI;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class StrangeOptions {
    static final int UNLIMITED_POINT = 51;
    private static final OptionInstance<Integer> MAP_ELEMEMT_LIMIT = new OptionInstance<>(
            "options.strangeitems.map_element_limit", OptionInstance.noTooltip(),
            (caption, value) -> {
                String key = "options.strangeitems.map_element_limit.unlimited";
                Component valStr = (value >= UNLIMITED_POINT ? Component.translatable(key) : Component.literal(String.valueOf(value)));
                return caption.copy().append(": ").append(valStr);
            },
            new OptionInstance.IntRange(3, UNLIMITED_POINT, true), 8, num -> {}
    );
    private static final OptionInstance<Boolean> SHOW_TRACKERS_IN_TOOLTIP = OptionInstance.createBoolean("options.strangeitems.show_trackers_in_tooltip", true);
    private static final OptionInstance<Boolean> SHOW_TRACKER_IF_ZERO = OptionInstance.createBoolean("options.strangeitems.show_tracker_if_zero", false);

    private static void register(String id, Supplier<OptionInstance<?>> supplier) {
        VanillaOptionsAPI.register(Id.of(id), OptionsMenuLocation.NONE, supplier);
    }

    public static void initalize() {
        register("map_element_limit", StrangeOptions::mapElementLimitOption);
        register("show_trackers_in_tooltip", StrangeOptions::showTrackersInTooltipOption);
        register("show_tracker_if_zero", StrangeOptions::showTrackerIfZeroOption);
    }

    public static OptionInstance<Integer> mapElementLimitOption() {
        return StrangeOptions.MAP_ELEMEMT_LIMIT;
    }

    public static int mapElementLimit() {
        int val = StrangeOptions.MAP_ELEMEMT_LIMIT.get();
        return val >= UNLIMITED_POINT ? 512 : val;
    }

    public static OptionInstance<Boolean> showTrackersInTooltipOption() {
        return StrangeOptions.SHOW_TRACKERS_IN_TOOLTIP;
    }

    public static boolean showTrackersInTooltip() {
        return StrangeOptions.SHOW_TRACKERS_IN_TOOLTIP.get();
    }

    public static OptionInstance<Boolean> showTrackerIfZeroOption() {
        return StrangeOptions.SHOW_TRACKER_IF_ZERO;
    }

    public static boolean showTrackerIfZero() {
        return StrangeOptions.SHOW_TRACKER_IF_ZERO.get();
    }
}
