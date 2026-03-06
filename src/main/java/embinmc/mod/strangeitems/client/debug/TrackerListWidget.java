package embinmc.mod.strangeitems.client.debug;

import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.tracker.Tracker;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

public class TrackerListWidget extends ContainerObjectSelectionList<TrackerListWidget.TrackerEntry> {

    public TrackerListWidget(Minecraft minecraftClient, TrackerListScreen screen) {
        super(minecraftClient, screen.width, screen.height - 67, 16, 24);
        for (Tracker tracker : StrangeRegistries.TRACKER) {
            this.addEntry(new TrackerEntry(tracker));
        }
    }

    @Override
    public int getRowWidth() {
        return 200;
    }

    public class TrackerEntry extends ContainerObjectSelectionList.Entry<TrackerListWidget.TrackerEntry> {
        public final Tracker tracker;
        public final Font textRenderer;

        TrackerEntry(Tracker tracker) {
            this.tracker = tracker;
            this.textRenderer = TrackerListWidget.this.minecraft.font;
            this.setWidth(150);
            this.setHeight(50);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }

        public void render(GuiGraphics context, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered) {
            Component name = Component.translatable(tracker.getTranslationKey());
            context.fillGradient(x, y, x + entryWidth, y + entryHeight, 1615855616, -1602211792);
            context.drawCenteredString(this.textRenderer, name, x + (entryWidth / 2), y + (entryHeight / 4) + 1, CommonColors.WHITE);
            if (hovered) {
                Component id = Component.literal(tracker.toString()).withStyle(ChatFormatting.DARK_GRAY);
                List<Component> tooltip = List.of(name, id);
                context.setComponentTooltipForNextFrame(this.textRenderer, tooltip, mouseX, mouseY);
            }
        }

        public Tracker getTracker() {
            return tracker;
        }

        @Override
        public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            this.render(context, this.getContentY(), this.getX(), this.getContentWidth(), this.getContentHeight(), mouseX, mouseY, hovered);
        }
    }
}
