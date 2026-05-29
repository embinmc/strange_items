package embinmc.mod.strangeitems.client;

import embinmc.mod.strangeitems.StrangeItems;
import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.tracker.MapLikeTracker;
import embinmc.mod.strangeitems.tracker.Tracker;
import embinmc.mod.strangeitems.tracker.TrackerTags;
import embinmc.mod.strangeitems.util.StrangeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TrackerListWidget extends ObjectSelectionList<TrackerListWidget.TrackerEntry> {
    public static final int WIDTH = 180;
    final List<TrackerListWidget.TrackerEntry> unfilteredEntries = new ArrayList<>(64);
    final ItemStack checkingStack;
    final LocalPlayer player;
    final Registry<Tracker> trackerRegistry;

    public TrackerListWidget(LocalPlayer player, ItemStack itemStack, Minecraft minecraft, int width, int y, int itemHeight) {
        super(minecraft, width - WIDTH, minecraft.getWindow().getGuiScaledHeight() - 80, y, itemHeight);
        this.checkingStack = itemStack;
        this.player = player;
        this.trackerRegistry = this.player.registryAccess().lookupOrThrow(StrangeRegistryKeys.TRACKER_NEW);
        this.setX(TrackerListWidget.WIDTH);

        List<Holder<Tracker>> trackersForItem = StrangeUtil.getTrackersForItem(this.player.registryAccess(), this.checkingStack, true);
        HolderSet<Tracker> tooltipOrder = StrangeUtil.getTooltipOrder(this.player.registryAccess(), TrackerTags.TOOLTIP_ORDER);
        for (Holder<Tracker> trackerHolder : tooltipOrder) {
            if (trackersForItem.contains(trackerHolder)) {
                TrackerEntry trackerEntry = new TrackerEntry(trackerHolder.value());
                this.addEntry(trackerEntry, trackerEntry.getHeight());
            }
        }
        for (Holder<Tracker> trackerHolder : trackersForItem) {
            if (!tooltipOrder.contains(trackerHolder)) {
                TrackerEntry trackerEntry = new TrackerEntry(trackerHolder.value());
                this.addEntry(trackerEntry, trackerEntry.getHeight());
            }
        }
    }

    public void reupdateWithSearch(final String search) {

    }

    public class TrackerEntry extends ObjectSelectionList.Entry<TrackerEntry> {
        protected final Tracker tracker;
        protected final LocalPlayer player;
        protected final ItemStack itemStack;
        protected final List<Component> lines;
        protected final Font font;

        public TrackerEntry(Tracker tracker) {
            super();
            this.tracker = tracker;
            this.player = TrackerListWidget.this.player;
            this.itemStack = TrackerListWidget.this.checkingStack;
            this.lines = new ArrayList<>(this.tracker instanceof MapLikeTracker ? 64 : 4);
            this.font = TrackerListWidget.this.minecraft.font;
            this.tracker.addToShowcaseText(this.lines::add, this.player.registryAccess(), this.itemStack);
            this.setHeight((this.lines.size() * 14) - (this.lines.size() * 2) + 2);
            this.setWidth(TrackerListWidget.this.width - 4);
        }

        @Override
        public @NonNull Component getNarration() {
            return this.lines.isEmpty() ? Component.literal("No content") : this.lines.getFirst();
        }

        @Override
        public void extractContent(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            int yOffset = 1;
            for (Component line : this.lines) {
                graphics.text(this.font, line, this.getX(), this.getContentY() + yOffset, ARGB.white(1f));
                yOffset += 12;
            }
        }
    }
}
