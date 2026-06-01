package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import embinmc.mod.strangeitems.util.datafix.actual.StrangeComponentRemovalFix;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.filefix.FileFixerUpper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataFixers.class)
public abstract class DataFixersMixin {
    @Inject(method = "addFixers", at = @At("TAIL"))
    private static void addDataFixers(DataFixerBuilder fixerUpper, FileFixerUpper.Builder fileFixerUpper, CallbackInfo ci, @Local(name = "v4892") Schema v4892) {
        fixerUpper.addFixer(new StrangeComponentRemovalFix(v4892));
    }
}
