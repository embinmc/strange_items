package embin.strangeitems.client;

import com.mojang.blaze3d.platform.InputConstants;
import embin.strangeitems.client.config.StrangeConfig;
import embin.strangeitems.client.debug.TrackerListScreen;
import embin.strangeitems.util.Id;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrangeItemsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("strangeitems/client");
    public static final KeyMapping.Category STRANGEKEYS = KeyMapping.Category.register(Id.of("keys"));

    private static KeyMapping keybind(String translation, int key) {
        return new KeyMapping(
            "key.strangeitems." + translation,
            InputConstants.Type.KEYSYM,
            key,
            STRANGEKEYS
        );
    }

    private static KeyMapping vanillaKeybind(String translation, int key) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.strangeitems." + translation,
                InputConstants.Type.KEYSYM,
                key,
                STRANGEKEYS
        ));
    }

    public static KeyMapping show_tracker_ids = vanillaKeybind("show_tracker_ids", GLFW.GLFW_KEY_COMMA);
    public static KeyMapping show_blocks_mined = vanillaKeybind("show_blocks_mined", GLFW.GLFW_KEY_Z);
    public static KeyMapping show_times_dropped = vanillaKeybind("show_times_dropped", GLFW.GLFW_KEY_RIGHT_ALT);
    public static KeyMapping show_mobs_killed = vanillaKeybind("show_mobs_killed", GLFW.GLFW_KEY_LEFT_ALT);
    public static KeyMapping show_time_in_dimensions = vanillaKeybind("show_time_in_dimension", GLFW.GLFW_KEY_GRAVE_ACCENT);
    public static KeyMapping SHOW_SHOTS_HIT = vanillaKeybind("show_shots_hit", GLFW.GLFW_KEY_RIGHT_SHIFT);

    public static KeyMapping DEBUG_LIST_TRACKERS = vanillaKeybind("debug_list_trackers", GLFW.GLFW_KEY_KP_DIVIDE);

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(Id.of("debug_list_trackers"), client -> {
            while (DEBUG_LIST_TRACKERS.consumeClick()) {
                client.setScreen(new TrackerListScreen(client.screen));
            }
        });

        LOGGER.info("Reading config...");
        StrangeConfig.readConfig();
    }
}
