package embinmc.mod.strangeitems.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.Command;
import embinmc.mod.strangeitems.client.config.StrangeConfig;
import embinmc.mod.strangeitems.network.ClientboundSyncEnderChestPacket;
import embinmc.mod.strangeitems.tracker.Tracker;
import embinmc.mod.strangeitems.util.Id;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
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

    public static KeyMapping show_tracker_ids = vanillaKeybind("show_tracker_ids", InputConstants.KEY_APOSTROPHE);
    public static KeyMapping SHOW_TRACKER_SCREEN = vanillaKeybind("show_tracker_screen", InputConstants.KEY_SEMICOLON);

    @Override
    public void onInitializeClient() {
        StrangeOptions.initalize();

        LOGGER.info("Reading config...");
        StrangeConfig.readConfig();

        ClientTickEvents.END_CLIENT_TICK.register(Id.of("open_menu_ingame"), minecraft -> {
            if (SHOW_TRACKER_SCREEN.consumeClick())
                minecraft.setScreenAndShow(new StatShowcaseScreen(null));
        });

        // Test to make sure my data fix sort logic was good
        //Stream<Integer> intStream = Stream.of(4, 7, 9, 8, 2, 0, 5, 4, 12, 6);
        //intStream.sorted(Comparator.comparingInt(Integer::intValue)).forEach(num -> LOGGER.info("{}", num));

        ItemTooltipCallback.EVENT.register(Id.of("item_tooltip"), Tracker::itemTooltip);

        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext) -> {
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

        ClientPlayNetworking.registerGlobalReceiver(ClientboundSyncEnderChestPacket.TYPE, ClientboundSyncEnderChestPacket::listen);
    }
}
