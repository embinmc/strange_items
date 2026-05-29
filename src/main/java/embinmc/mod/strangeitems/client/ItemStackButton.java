package embinmc.mod.strangeitems.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class ItemStackButton extends Button {
    protected static final Identifier SELECTED = Identifier.withDefaultNamespace("widget/button_highlighted");
    protected final ItemStack itemStack;
    protected final StatShowcaseScreen showcaseScreen;

    protected ItemStackButton(int x, int y, ItemStack itemStack, OnPressStack onPress, StatShowcaseScreen showcaseScreen) {
        super(x, y, 18, 18, itemStack.getItemName(), button -> {
            if (button instanceof ItemStackButton itemStackButton) {
                onPress.onPress(itemStackButton);
            }
        }, ns -> itemStack.getItemName().copy());
        this.itemStack = itemStack;
        this.showcaseScreen = showcaseScreen;
        if (this.itemStack.isEmpty())
            this.active = false;
    }

    @Override
    protected void extractContents(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (this.showcaseScreen.trackerListWidget.checkingStack == this.itemStack && this.active)
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SELECTED, this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.getAlpha()));
        else
            this.extractDefaultSprite(graphics);
        graphics.item(this.itemStack, this.getX() + 1, this.getY() + 1);
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public interface OnPressStack {
        void onPress(final ItemStackButton button);
    }
}
