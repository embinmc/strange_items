package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trigger;
import embinmc.mod.strangeitems.util.StrangeUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Equippable.class)
public class EquippableComponentMixin {
    @Inject(method = "swapWithEquipmentSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
    public void equipMixin(ItemStack stack, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        Equippable ec = (Equippable) (Object) this;
        if (StrangeUtil.canSwap(stack, player.getItemBySlot(ec.slot()), player)) {
            Identifier swapToId = player.getItemBySlot(ec.slot()).typeHolder().unwrapKey().orElseThrow().identifier();
            Trigger.EQUIP_ITEM.appendWithData(player.registryAccess(), stack, 1, swapToId);
        }
    }
}
