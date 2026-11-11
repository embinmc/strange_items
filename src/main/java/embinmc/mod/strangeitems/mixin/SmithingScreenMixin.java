package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings({"unused"})
@Mixin(SmithingScreen.class)
abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {
    private SmithingScreenMixin(SmithingMenu menu, Inventory inventory, Component title, Identifier menuResource) {
        super(menu, inventory, title, menuResource);
        throw new UnsupportedOperationException();
    }

    @ModifyReturnValue(method = "createEmptySlotTooltip", at = @At("RETURN"))
    private Tooltip strangeitems$giveDescription(Tooltip original, @Local(argsOnly = true, name = "slot") Slot slot) {
        if (this.menu.getSlot(0).getItem().getItem() != Items.ECHO_SHARD)
            return original;
        return switch (slot.index) {
            case 1 -> Tooltip.create(Component.translatable("tooltip.strangeitems.smithing_collectors.base"));
            case 2 -> Tooltip.create(Component.translatable("tooltip.strangeitems.smithing_collectors.addition"));
            default -> null;
        };
    }
}