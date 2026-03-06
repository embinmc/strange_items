package embinmc.mod.strangeitems.client.config;

// import me.shedaniel.clothconfig2.api.ConfigBuilder;
// import me.shedaniel.clothconfig2.api.ConfigCategory;
// import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen {
    private ConfigScreen() {}

    /*
    public static ConfigBuilder configBuilder(final Screen parent) {
        final ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("screens.strangeitems.config"));


        final ConfigEntryBuilder entry_builder = builder.entryBuilder();
        final ConfigCategory strangeitems = builder.getOrCreateCategory(Component.translatable("screens.strangeitems.config"));

        strangeitems.addEntry(entry_builder.startBooleanToggle(Component.translatable("setting.strangeitems.in_depth_tracking"), StrangeConfig.in_depth_tracking)
            .setDefaultValue(true)
            .setSaveConsumer(newvalue -> StrangeConfig.in_depth_tracking = newvalue)
            .setTooltip(
                Component.translatable("setting.strangeitems.in_depth_tracking.desc"),
                Component.translatable("setting.strangeitems.in_depth_tracking.desc2")
            )
            .build()
        );

        strangeitems.addEntry(entry_builder.startBooleanToggle(Component.translatable("setting.strangeitems.check_for_tooltipscroll_mod"), StrangeConfig.check_for_tooltipscroll)
            .setDefaultValue(true)
            .setSaveConsumer(newvalue -> StrangeConfig.check_for_tooltipscroll = newvalue)
            .setTooltip(
                Component.translatable("setting.strangeitems.check_for_tooltipscroll_mod.desc"),
                Component.translatable("setting.strangeitems.check_for_tooltipscroll_mod.desc2")
            )
            .build()
        );

        strangeitems.addEntry(entry_builder.startBooleanToggle(Component.translatable("setting.strangeitems.invert_tooltipscroll_check_value"), StrangeConfig.invert_tooltipscroll_check_value)
            .setDefaultValue(false)
            .setSaveConsumer(newvalue -> StrangeConfig.invert_tooltipscroll_check_value = newvalue)
            .setTooltip(
                Component.translatable("setting.strangeitems.invert_tooltipscroll_check_value.desc"),
                Component.translatable("setting.strangeitems.invert_tooltipscroll_check_value.desc2"),
                Component.translatable("setting.strangeitems.invert_tooltipscroll_check_value.desc3")
            )
            .build()
        );

        builder.transparentBackground();
        builder.setSavingRunnable(StrangeConfig::saveConfig);
        return builder;
    }
     */
}
