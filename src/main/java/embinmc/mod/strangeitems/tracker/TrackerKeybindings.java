package embinmc.mod.strangeitems.tracker;

import com.mojang.blaze3d.platform.InputConstants;
import embinmc.mod.strangeitems.StrangeItems;
import embinmc.mod.strangeitems.client.StrangeItemsClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TrackerKeybindings {
    private TrackerKeybindings() {}

    public static final KeyMapping FALLBACK_KEYBINDING = new KeyMapping(
        "key.strangeitems.unknown",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        StrangeItemsClient.STRANGEKEYS
    );

    public static final List<Tracker> WARNED_KEYBINDINGS = new ArrayList<>(99);

    public static final Map<MapTracker, KeyMapping> MAP_TRACKER_KEYBINDINGS = Util.make(HashMap.newHashMap(8), (map) -> {
        map.put(Trackers.BLOCKS_MINED, StrangeItemsClient.show_blocks_mined);
        map.put(Trackers.MOBS_KILLED, StrangeItemsClient.show_mobs_killed);
        map.put(Trackers.TIME_IN_DIMENSIONS, StrangeItemsClient.show_time_in_dimensions);
        map.put(Trackers.SHOTS_HIT, StrangeItemsClient.SHOW_SHOTS_HIT);
    });

    public static final Map<TimestampTracker, KeyMapping> TIMESTAMP_TRACKER_KEYBINDINGS = Util.make(HashMap.newHashMap(8), (map) -> {
        map.put(Trackers.TIMES_DROPPED, StrangeItemsClient.show_times_dropped);
    });


    public static void define_map_keybind(MapTracker tracker, KeyMapping key) {
        MAP_TRACKER_KEYBINDINGS.put(tracker, key);
    }

    public static void define_timestamp_keybind(TimestampTracker tracker, KeyMapping key) {
        TIMESTAMP_TRACKER_KEYBINDINGS.put(tracker, key);
    }

    public static KeyMapping get_map_keybind(MapTracker tracker) {
        if (MAP_TRACKER_KEYBINDINGS.containsKey(tracker)) {
            return MAP_TRACKER_KEYBINDINGS.get(tracker);
        }
        if (!WARNED_KEYBINDINGS.contains(tracker)) {
            StrangeItems.LOGGER.warn("Tracker " + tracker.getId().toString() + " does not have an assigned key binding!");
            WARNED_KEYBINDINGS.add(tracker);
        }
        return FALLBACK_KEYBINDING;
    }

    public static KeyMapping get_timestamp_keybind(TimestampTracker tracker) {
        if (TIMESTAMP_TRACKER_KEYBINDINGS.containsKey(tracker)) {
            return TIMESTAMP_TRACKER_KEYBINDINGS.get(tracker);
        }
        if (!WARNED_KEYBINDINGS.contains(tracker)) {
            StrangeItems.LOGGER.warn("Tracker " + tracker.getId().toString() + " does not have an assigned key binding!");
            WARNED_KEYBINDINGS.add(tracker);
        }
        return FALLBACK_KEYBINDING;
    }
}
