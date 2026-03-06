package embinmc.mod.strangeitems.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Level.class)
public class ExampleMixin {

	/**
	 * @author Embin
	 * @reason Test
	 */
	@Overwrite
	private static boolean isInWorldBoundsHorizontal(BlockPos pos) {
		return true;
	}

	/**
	 * @author Embin
	 * @reason Test
	 */
	@Overwrite
	private static boolean isOutsideSpawnableHeight(int y) {
		return false;
	}
}