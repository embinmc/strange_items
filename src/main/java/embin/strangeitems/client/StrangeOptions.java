package embin.strangeitems.client;

import embin.strangeitems.util.Id;
import embinmc.mod.optionsapi.OptionsMenuLocation;
import embinmc.mod.optionsapi.VanillaOptionsAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

public class StrangeOptions {
    static final int UNLIMITED_POINT = 51;
    private static final OptionInstance<Integer> MAP_ELEMEMT_LIMIT = new OptionInstance<>(
            "options.strangeitems.map_element_limit", OptionInstance.noTooltip(),
            (caption, value) -> {
                String key = "options.strangeitems.map_element_limit.unlimited";
                Component valStr = (value >= UNLIMITED_POINT ? Component.translatable(key) : Component.literal(String.valueOf(value))).withStyle(ChatFormatting.YELLOW);
                return caption.copy().append(": ").append(valStr);
            },
            new OptionInstance.IntRange(3, UNLIMITED_POINT, true), 8, num -> {}
    );

    public static void initalize() {
        VanillaOptionsAPI.register(Id.of("map_element_limit"), OptionsMenuLocation.NONE, StrangeOptions::mapElementLimitOption);
    }

    public static OptionInstance<Integer> mapElementLimitOption() {
        return StrangeOptions.MAP_ELEMEMT_LIMIT;
    }

    public static int mapElementLimit() {
        int val = StrangeOptions.MAP_ELEMEMT_LIMIT.get();
        return val >= UNLIMITED_POINT ? 999_999 : val;
    }
}
