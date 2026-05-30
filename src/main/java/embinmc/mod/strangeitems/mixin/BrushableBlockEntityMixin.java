package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushableBlockEntity.class)
public abstract class BrushableBlockEntityMixin {
    @Shadow private ItemStack item;

    @Inject(method = "dropContent", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/world/level/block/entity/BrushableBlockEntity;unpackLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemInstance;)V"))
    public void blockBrushedSuccessfullyTrigger(ServerLevel level, LivingEntity user, ItemStack brush, CallbackInfo ci) {
        if (this.item.isEmpty())
            return;
        Identifier itemId = this.item.typeHolder().unwrapKey().orElseThrow().identifier();
        Trigger.BRUSH_BLOCK_SUCCEEDS.appendWithData(level.registryAccess(), brush, this.item.count(), itemId);
    }
}
