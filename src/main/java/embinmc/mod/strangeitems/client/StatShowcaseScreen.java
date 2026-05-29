package embinmc.mod.strangeitems.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class StatShowcaseScreen extends Screen {
    public static final Component TITLE = Component.translatable("screens.strangeitems.stat_showcase.title");
    public static final Component SEARCH_BAR_HINT = Component.translatable("screens.strangeitems.stat_showcase.search_bar_hint");
    protected final LocalPlayer player;
    protected EditBox searchBar;
    protected TrackerListWidget trackerListWidget;

    protected StatShowcaseScreen() {
        super(TITLE);
        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        if (this.player == null || this.player.getActiveItem().isEmpty())
            return;
        this.searchBar = new EditBox(this.minecraft.font, SEARCH_BAR_HINT);
        this.searchBar.setHint(SEARCH_BAR_HINT);
        this.searchBar.setY(10);
        this.searchBar.setX(TrackerListWidget.WIDTH);
        this.searchBar.setWidth(this.width - TrackerListWidget.WIDTH);
        this.addRenderableWidget(this.searchBar);
        this.trackerListWidget = new TrackerListWidget(this.player, this.player.getActiveItem(), this.minecraft, this.width, 40, 16);
        this.addRenderableWidget(this.trackerListWidget);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
