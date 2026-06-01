package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.util.StrangeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemMixin {

    @Inject(method = "getHoverName", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void collectorsName(CallbackInfoReturnable<Component> cir) {
        ItemStack stack = (ItemStack)(Object) this;
        if (StrangeUtil.isCollectors(stack)) {
            cir.setReturnValue(Component.translatable("tooltip.strangeitems.collectors_item.item_name", cir.getReturnValue()));
        }
    }
}
