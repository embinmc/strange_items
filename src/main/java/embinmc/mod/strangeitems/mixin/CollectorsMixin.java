package embinmc.mod.strangeitems.mixin;

import embinmc.mod.strangeitems.util.StrangeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Gui.class)
public class CollectorsMixin {
	@Shadow private ItemStack lastToolHighlight;

	@Inject(at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I",
			shift = At.Shift.BEFORE
	), method = "extractSelectedItemName", locals = LocalCapture.CAPTURE_FAILHARD)
	private void adjust_color_for_collectors(GuiGraphicsExtractor context, CallbackInfo ci, MutableComponent mutableText) {
		if (StrangeUtil.isCollectors(this.lastToolHighlight)) {
			mutableText.withStyle(ChatFormatting.DARK_RED);
		}
	}
}