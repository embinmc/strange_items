package embinmc.mod.strangeitems.client.debug;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class TrackerListScreen extends Screen {
    private TrackerListWidget trackerList;
    private final Screen parent;

    public TrackerListScreen(Screen parent) {
        super(Component.literal("Debug: Registered Trackers"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.trackerList = new TrackerListWidget(this.minecraft, this);
        this.addRenderableWidget(this.trackerList);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        this.extractPanorama(context, deltaTicks);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
