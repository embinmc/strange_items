package embin.strangeitems.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.Command;
import embin.strangeitems.StrangeItems;
import embin.strangeitems.client.config.StrangeConfig;
import embin.strangeitems.client.debug.TrackerListScreen;
import embin.strangeitems.util.Id;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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

    public static KeyMapping show_tracker_ids = vanillaKeybind("show_tracker_ids", InputConstants.KEY_COMMA);
    public static KeyMapping show_blocks_mined = vanillaKeybind("show_blocks_mined", InputConstants.KEY_Z);
    public static KeyMapping show_times_dropped = vanillaKeybind("show_times_dropped", InputConstants.KEY_RALT);
    public static KeyMapping show_mobs_killed = vanillaKeybind("show_mobs_killed", InputConstants.KEY_LALT);
    public static KeyMapping show_time_in_dimensions = vanillaKeybind("show_time_in_dimension", InputConstants.KEY_GRAVE);
    public static KeyMapping SHOW_SHOTS_HIT = vanillaKeybind("show_shots_hit", InputConstants.KEY_RSHIFT);

    public static KeyMapping DEBUG_LIST_TRACKERS = vanillaKeybind("debug_list_trackers", GLFW.GLFW_KEY_KP_DIVIDE);

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(Id.of("debug_list_trackers"), client -> {
            while (DEBUG_LIST_TRACKERS.consumeClick()) {
                client.setScreen(new TrackerListScreen(client.screen));
            }
        });
        StrangeOptions.initalize();

        LOGGER.info("Reading config...");
        StrangeConfig.readConfig();

        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext) -> {
            commandDispatcher.register(ClientCommands.literal("strangeitems:debug_listComponents").executes(context -> {
                FabricClientCommandSource source = context.getSource();
                //LOGGER.info(source.getPlayer().getActiveItem().getComponents().toString());
                source.sendFeedback(Component.literal(DataComponentMap.builder().addAll(source.getPlayer().getActiveItem().getComponents().filter(dct -> {
                    String namespace = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(dct).getNamespace();
                    return namespace.equals(StrangeItems.MOD_ID) || dct == DataComponents.CUSTOM_DATA;
                })).build().toString()));
                return Command.SINGLE_SUCCESS;
            }));
        });
    }
}
