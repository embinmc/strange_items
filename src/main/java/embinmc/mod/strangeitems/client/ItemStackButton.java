package embinmc.mod.strangeitems.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ItemStackButton extends Button {
    protected static final Identifier SELECTED = Identifier.withDefaultNamespace("widget/button_highlighted");
    protected final ItemStack itemStack;
    protected final StatShowcaseScreen showcaseScreen;
    protected final @Nullable EquipmentSlot slot;

    protected ItemStackButton(int x, int y, ItemStack itemStack, OnPressStack onPress, StatShowcaseScreen showcaseScreen, @Nullable EquipmentSlot slot) {
        super(x, y, 18, 18, itemStack.getItemName(), button -> {
            if (button instanceof ItemStackButton itemStackButton) {
                onPress.onPress(itemStackButton);
            }
        }, ns -> itemStack.getItemName().copy());
        this.itemStack = itemStack;
        this.showcaseScreen = showcaseScreen;
        if (this.itemStack.isEmpty())
            this.active = false;
        this.slot = slot;
    }

    protected ItemStackButton(int x, int y, ItemStack itemStack, OnPressStack onPress, StatShowcaseScreen showcaseScreen) {
        this(x, y, itemStack, onPress, showcaseScreen, null);
    }

    @Override
    protected void extractContents(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (this.showcaseScreen.trackerListWidget.checkingStack == this.itemStack && this.active)
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SELECTED, this.getX(), this.getY(), this.getWidth(), this.getHeight(), ARGB.white(this.getAlpha()));
        else
            this.extractDefaultSprite(graphics);
        if (this.slot != null && this.itemStack.isEmpty()) {
            Identifier texture = this.slot == EquipmentSlot.OFFHAND ?
                    InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD :
                    InventoryMenu.TEXTURE_EMPTY_SLOTS.get(this.slot);
            if (texture != null)
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, this.getX() + 1, this.getY() + 1, this.getWidth() - 2, this.getHeight() - 2, ARGB.white(this.getAlpha()));
        }
        graphics.item(this.itemStack, this.getX() + 1, this.getY() + 1);
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public interface OnPressStack {
        void onPress(final ItemStackButton button);
    }
}
