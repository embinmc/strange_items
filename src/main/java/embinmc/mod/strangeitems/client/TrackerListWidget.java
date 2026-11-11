package embinmc.mod.strangeitems.client;

import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.tracker.MapLikeTracker;
import embinmc.mod.strangeitems.tracker.Tracker;
import embinmc.mod.strangeitems.tracker.TrackerTags;
import embinmc.mod.strangeitems.util.StrangeUtil;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class TrackerListWidget extends ObjectSelectionList<TrackerListWidget.TrackerEntry> {
    public static final int WIDTH = 180;
    public static final Component EMPTY = Component.translatable("screens.strangeitems.stat_showcase.empty").withStyle(ChatFormatting.GRAY);
    public static final Component EMPTY_SEARCH = Component.translatable("screens.strangeitems.stat_showcase.empty_search").withStyle(ChatFormatting.GRAY);
    final Object2IntMap<TrackerEntry> unfilteredEntries = new Object2IntLinkedOpenHashMap<>(64);
    final ItemStack checkingStack;
    final LocalPlayer player;
    final Registry<Tracker> trackerRegistry;
    public String currentSearchTerm = "";

    public TrackerListWidget(StatShowcaseScreen showcaseScreen, ItemStack itemStack, int width, int y, int itemHeight) {
        super(Minecraft.getInstance(), width - WIDTH, showcaseScreen.height - 60, y, itemHeight);
        this.checkingStack = itemStack;
        this.player = showcaseScreen.player;
        this.trackerRegistry = this.player.registryAccess().lookupOrThrow(StrangeRegistryKeys.TRACKER_NEW);
        this.setX(TrackerListWidget.WIDTH);

        List<Holder<Tracker>> trackersForItem = StrangeUtil.getTrackersForItem(this.player.registryAccess(), this.checkingStack, true);
        HolderSet<Tracker> tooltipOrder = StrangeUtil.getTooltipOrder(this.player.registryAccess(), TrackerTags.MENU_ORDER);
        Consumer<Holder<Tracker>> addAction = trackerHolder -> {
            Tracker tracker = trackerHolder.value();
            TrackerEntry trackerEntry = new TrackerEntry(tracker, null, null);
            this.unfilteredEntries.put(trackerEntry, trackerEntry.getHeight());
            tracker.addShowcaseChildren(line -> {
                TrackerEntry childEntry = new TrackerEntry(tracker, trackerEntry, line);
                this.unfilteredEntries.put(childEntry, childEntry.getHeight());
            }, this.player.registryAccess(), this.checkingStack);
        };
        for (Holder<Tracker> trackerHolder : tooltipOrder) {
            if (trackersForItem.contains(trackerHolder)) {
                addAction.accept(trackerHolder);
            }
        }
        for (Holder<Tracker> trackerHolder : trackersForItem) {
            if (!tooltipOrder.contains(trackerHolder)) {
                addAction.accept(trackerHolder);
            }
        }
        this.reupdateWithSearch(this.currentSearchTerm);
    }

    public void reupdateWithSearch(final String search) {
        this.clearEntries();
        this.currentSearchTerm = search;
        if (search.isBlank()) {
            this.unfilteredEntries.forEach(this::addEntry);
            return;
        }
        List<TrackerEntry> filtered = Util.make(new ArrayList<>(this.unfilteredEntries.size()), list -> {
            for (TrackerEntry trackerEntry : this.unfilteredEntries.keySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                List.copyOf(trackerEntry.lines).stream()
                        .map(Component::getString)
                        .map(string -> string.toLowerCase(Locale.ROOT))
                        .forEach(stringBuilder::append);
                String searchString = stringBuilder.toString();
                if (searchString.contains(search)) {
                    if (trackerEntry.parent != null && !list.contains(trackerEntry.parent)) {
                        list.add(trackerEntry.parent);
                    }
                    list.add(trackerEntry);
                }
            }
        });
        filtered.forEach(trackerEntry -> this.addEntry(trackerEntry, trackerEntry.getHeight()));
    }

    @Override
    protected void extractListItems(@NonNull GuiGraphicsExtractor graphics) {
        if (this.children().isEmpty()) {
            int x = this.getX() + (this.getWidth() / 2);
            int y = this.getY() + (this.getHeight() / 2);
            Component emptyText = this.currentSearchTerm.isBlank() ? EMPTY : EMPTY_SEARCH;
            graphics.textRenderer().accept(TextAlignment.CENTER, x, y, emptyText);
            return;
        }
        super.extractListItems(graphics);
    }

    @Override
    public void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics) {
        super.extractWidgetRenderState(graphics);
        if (!this.checkingStack.isEmpty()) {
            graphics.textRenderer().accept(TextAlignment.LEFT, this.getX(), this.getY() - 12, this.checkingStack.getStyledHoverName());
        }
    }

    public class TrackerEntry extends ObjectSelectionList.Entry<TrackerEntry> {
        protected final @Nullable TrackerEntry parent;
        protected final Tracker tracker;
        protected final LocalPlayer player;
        protected final ItemStack itemStack;
        protected final List<Component> lines;
        protected final Font font;

        private TrackerEntry(Tracker tracker, @Nullable TrackerEntry parent, @Nullable Component line) {
            super();
            this.tracker = tracker;
            this.player = TrackerListWidget.this.player;
            this.itemStack = TrackerListWidget.this.checkingStack;
            this.lines = new ArrayList<>(this.tracker instanceof MapLikeTracker ? 64 : 4);
            this.font = TrackerListWidget.this.minecraft.font;
            this.parent = parent;
            if (this.parent == null) {
                this.lines.add(this.tracker.getShowcaseHeader(this.player.registryAccess(), this.itemStack));
            } else {
                this.lines.add(line);
            }
            this.setHeight((this.lines.size() * 14) - (this.lines.size() * 2) + 2);
            this.setWidth(TrackerListWidget.this.width - 4);
        }

        @Override
        public @NonNull Component getNarration() {
            return this.lines.isEmpty() ? Component.literal("No content") : this.lines.getFirst();
        }

        @Override
        public void extractContent(@NonNull GuiGraphicsExtractor graphics, boolean hovered) {
            int yOffset = 1;
            for (Component line : this.lines) {
                graphics.text(this.font, line, this.getX() + 4, this.getContentY() + yOffset, ARGB.white(1f));
                yOffset += 12;
            }
        }
    }
}
