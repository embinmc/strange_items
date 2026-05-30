package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.tracker.Trigger;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sheep.class)
public abstract class SheepMixin {
    @Inject(method = "shear", at = @At("HEAD"))
    public void sheepShear(ServerLevel level, SoundSource soundSource, ItemStack tool, CallbackInfo ci) {
        Sheep myself = (Sheep)(Object)this;
        Trigger.SHEAR_SHEEP.appendWithDimension(myself, tool);
    }
}
