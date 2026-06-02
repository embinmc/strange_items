package embinmc.mod.strangeitems.client;

import embinmc.mod.strangeitems.util.Id;
import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VanillaOptionsAPI;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class StrangeOptions {
    private static final OptionInstance<Boolean> SHOW_TRACKERS_IN_TOOLTIP = OptionInstance.createBoolean("options.strangeitems.show_trackers_in_tooltip", true);
    private static final OptionInstance<Boolean> SHOW_TRACKER_IF_ZERO = OptionInstance.createBoolean("options.strangeitems.show_tracker_if_zero", false);

    private static void register(String id, Supplier<OptionInstance<?>> supplier) {
        VanillaOptionsAPI.register(Id.of(id), OptionsMenuLocation.NONE, supplier);
    }

    public static void initalize() {
        register("show_trackers_in_tooltip", StrangeOptions::showTrackersInTooltipOption);
        register("show_tracker_if_zero", StrangeOptions::showTrackerIfZeroOption);
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
