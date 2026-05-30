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

@Deprecated(forRemoval = true)
public final class TrackerKeybindings {
    private TrackerKeybindings() {}

    public static final KeyMapping FALLBACK_KEYBINDING = new KeyMapping(
        "key.strangeitems.unknown",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_UNKNOWN,
        StrangeItemsClient.STRANGEKEYS
    );

    public static KeyMapping get_map_keybind(LegacyMapTracker tracker) {
        return FALLBACK_KEYBINDING;
    }

    public static KeyMapping get_timestamp_keybind(LegacyTimestampTracker tracker) {
        return FALLBACK_KEYBINDING;
    }
}
