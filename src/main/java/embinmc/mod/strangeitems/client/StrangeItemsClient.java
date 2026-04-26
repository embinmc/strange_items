package embinmc.mod.strangeitems.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.Command;
import com.mojang.serialization.DataResult;
import embinmc.mod.strangeitems.StrangeItems;
import embinmc.mod.strangeitems.client.config.StrangeConfig;
import embinmc.mod.strangeitems.client.debug.TrackerListScreen;
import embinmc.mod.strangeitems.util.Id;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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
    public static KeyMapping show_time_in_dimensions = vanillaKeybind("show_time_in_dimension", InputConstants.KEY_LALT);
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
                ItemStack holdingItem = source.getPlayer().getActiveItem();
                if (!holdingItem.has(DataComponents.CUSTOM_DATA)) {
                    source.sendFeedback(Component.literal("Item doesn't have minecraft:custom_data"));
                    return Command.SINGLE_SUCCESS;
                }
                CompoundTag nbt = holdingItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                CompoundTag filteredNbt = Util.make(new CompoundTag(), tag -> {
                    for (String key : nbt.keySet()) {
                        if (key.contains(StrangeItems.MOD_ID))
                            tag.put(key, Objects.requireNonNull(nbt.get(key)));
                    }
                });

                holdingItem.getComponents().forEach(componentData -> {
                    DataComponentType<?> component = componentData.type();
                    if (component == DataComponents.CUSTOM_DATA)
                        return;
                    Identifier key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(component);
                    if (key == null)
                        return;
                    if (key.getNamespace().equals(StrangeItems.MOD_ID)) {
                        MutableComponent text = Component.literal(key.toString());
                        text.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                        DataResult<Tag> dataResult = componentData.encodeValue(NbtOps.INSTANCE);
                        if (dataResult.isSuccess()) {
                            text.append(NbtUtils.toPrettyComponent(dataResult.getOrThrow()));
                        } else {
                            text.append("Failed to serialize");
                        }
                        source.sendFeedback(text);
                    }
                });
                MutableComponent text = Component.literal("minecraft:custom_data (Filtered)");
                text.append(Component.literal(": ").withStyle(ChatFormatting.GRAY));
                text.append(NbtUtils.toPrettyComponent(filteredNbt));
                source.sendFeedback(text);
                return Command.SINGLE_SUCCESS;
            }));
            commandDispatcher.register(ClientCommands.literal("strangeitems:debug_listCustomData").executes(context -> {
                FabricClientCommandSource source = context.getSource();
                ItemStack holdingItem = source.getPlayer().getActiveItem();
                if (!holdingItem.has(DataComponents.CUSTOM_DATA)) {
                    source.sendFeedback(Component.literal("Item doesn't have minecraft:custom_data"));
                    return Command.SINGLE_SUCCESS;
                }
                CompoundTag nbt = holdingItem.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                source.sendFeedback(Component.literal("Contents of \"minecraft:custom_data\":"));
                source.sendFeedback(NbtUtils.toPrettyComponent(nbt));
                return Command.SINGLE_SUCCESS;
            }));
        });
    }
}
