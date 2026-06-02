package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PumpkinBlock.class)
public abstract class PumpkinBlockMixin {
    @WrapOperation(method = "useItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/stats/Stat;)V"))
    public void carvePumpkingTrigger(Player instance, Stat<?> stat, Operation<Void> original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) ItemStack itemStack) {
        original.call(instance, Stats.ITEM_USED.get(itemStack.getItem()));
        Identifier blockId = state.typeHolder().unwrapKey().orElseThrow().identifier();
        Trigger.CARVE_PUMPKIN.appendWithData(instance.registryAccess(), itemStack, 1, blockId);
    }
}
