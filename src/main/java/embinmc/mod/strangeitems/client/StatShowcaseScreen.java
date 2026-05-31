package embinmc.mod.strangeitems.client;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class StatShowcaseScreen extends Screen {
    public static final Component TITLE = Component.translatable("screens.strangeitems.stat_showcase.title");
    public static final Component SEARCH_BAR_HINT = Component.translatable("screens.strangeitems.stat_showcase.search_bar_hint");
    protected final LocalPlayer player;
    protected final @NonNull ClientLevel level;
    protected final @Nullable ItemStack initialStack;
    protected EditBox searchBar;
    protected TrackerListWidget trackerListWidget;

    public StatShowcaseScreen(@Nullable ItemStack initialStack) {
        super(TITLE);
        this.player = this.minecraft.player;
        this.level = Objects.requireNonNull(this.minecraft.level);
        this.initialStack = initialStack;
    }

    @Override
    protected void init() {
        if (this.player == null) {
            this.minecraft.gui.setScreen(null);
            return;
        }
        this.searchBar = new EditBox(this.minecraft.font, SEARCH_BAR_HINT);
        this.searchBar.setHint(SEARCH_BAR_HINT);
        this.searchBar.setY(10);
        this.searchBar.setX(TrackerListWidget.WIDTH);
        this.searchBar.setWidth(this.width - TrackerListWidget.WIDTH);
        this.addRenderableWidget(this.searchBar);
        ItemStack itemStack2 = this.initialStack == null ? this.player.getActiveItem() : this.initialStack;
        this.trackerListWidget = new TrackerListWidget(this, itemStack2, this.width, 40, 16);
        this.addRenderableWidget(this.trackerListWidget);
        this.searchBar.setResponder(this.trackerListWidget::reupdateWithSearch);

        int start = 8;

        int x = start;
        int y = start;
        int slot = 0;
        boolean onHotbar = true;
        ItemStackButton.OnPressStack onPress = button -> {
            this.removeWidget(this.trackerListWidget);
            this.trackerListWidget = new TrackerListWidget(this, button.getItemStack(), this.width, 40, 16);
            this.addRenderableWidget(this.trackerListWidget);
            this.searchBar.setResponder(this.trackerListWidget::reupdateWithSearch);
            this.trackerListWidget.reupdateWithSearch(this.searchBar.getValue());
        };
        for (ItemStack itemStack : this.player.getInventory()) {
            if (slot == Inventory.SLOT_SADDLE || slot == Inventory.SLOT_BODY_ARMOR) {
                slot += 1;
                continue;
            }
            ItemStackButton button = new ItemStackButton(x, y, itemStack, onPress, this);
            this.addRenderableWidget(button);
            x += 18;
            slot += 1;
            if (x > TrackerListWidget.WIDTH - 16) {
                x = start;
                y += 18;
                if (onHotbar) {
                    y += 2;
                    onHotbar = false;
                }
            }
        }

        x = start;
        y += 24;
        this.addRenderableWidget(new StringWidget(x, y, 64, 10, Component.translatable("container.enderchest"), this.minecraft.font));
        y += 10;
        slot = 0;
        for (ItemStack itemStack : this.player.getEnderChestInventory()) {
            ItemStackButton button = new ItemStackButton(x, y, itemStack, onPress, this);
            this.addRenderableWidget(button);
            x += 18;
            slot += 1;
            if (x > TrackerListWidget.WIDTH - 16) {
                x = start;
                y += 18;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
    }
}
