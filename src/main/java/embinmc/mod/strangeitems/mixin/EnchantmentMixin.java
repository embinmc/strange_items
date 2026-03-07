package embinmc.mod.strangeitems.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemEnchantments.class)
public abstract class EnchantmentMixin {

    private @Shadow @Final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;

    @Inject(method = "addToTooltip", at = @At(value = "HEAD"))
    private void addHeader(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components, CallbackInfo ci) {
        if (!this.enchantments.isEmpty()) {
            consumer.accept(Component.translatable("tooltip.strangeitems.enchantments").append(":").withStyle(ChatFormatting.GRAY));
        }
    }

    @WrapOperation(method = "addToTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private <T> void addSpaceToEnchantName(Consumer<Component> instance, T t, Operation<Void> original) {
        if (t instanceof Component input) {
            String str = input.tryCollapseToString();
            if (str != null && !str.endsWith(":")) instance.accept(input);
            else instance.accept(Component.literal(" ").append(input));
        }
    }
}
