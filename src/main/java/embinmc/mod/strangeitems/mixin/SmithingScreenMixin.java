package embinmc.mod.strangeitems.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "OptionalUsedAsFieldOrParameterType"})
@Mixin(SmithingScreen.class)
abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {
    private SmithingScreenMixin(SmithingMenu menu, Inventory inventory, Component title, Identifier menuResource) {
        super(menu, inventory, title, menuResource);
        throw new UnsupportedOperationException();
    }

    @Redirect(
            method = "renderOnboardingTooltips",
            at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V")
    )
    private void giveDescription(Optional<Component> instance, Consumer<? super Component> action) {
        if (this.hoveredSlot != null) { // check special case for if template item is an echo shard
            ItemStack template = this.menu.getSlot(0).getItem();
            if (template.getItem() == Items.ECHO_SHARD && this.hoveredSlot.getItem().isEmpty()) {
                if (this.hoveredSlot.index == 1) {
                    action.accept(Component.translatable("tooltip.strangeitems.smithing_collectors.base"));
                    return;
                } else if (this.hoveredSlot.index == 2) {
                    action.accept(Component.translatable("tooltip.strangeitems.smithing_collectors.addition"));
                    return;
                }
            }
        }
        instance.ifPresent(action); // else, do vanilla code
    }
}