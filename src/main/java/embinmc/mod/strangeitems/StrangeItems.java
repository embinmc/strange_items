package embinmc.mod.strangeitems;

import embinmc.mod.strangeitems.network.ClientboundSyncEnderChestPacket;
import embinmc.mod.strangeitems.network.StrangeItemPayloads;
import embinmc.mod.strangeitems.util.Id;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrangeItems implements ModInitializer {
	public static final String MOD_ID = "strangeitems";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int DATA_VERSION = 1;
	/**
	 * Check if Tooltip Scroll is installed.
	 * Does not respect the users settings on how it should handle this check.
	 */
	@Deprecated public static final boolean tooltipscroll_installed = FabricLoader.getInstance().isModLoaded("tooltipscroll");
	@Deprecated public static final boolean componentless_installed = FabricLoader.getInstance().isModLoaded("componentless");

	@Override
	public void onInitialize() {
		StrangeRegistries.acknowledgeRegistries();

        SIRegisteredEvents.registerEvents();

		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Id.of("smithing_collectors"), CollectorsTransformRecipe.SERIALIZER);
		RecipeSynchronization.synchronizeRecipeSerializer(CollectorsTransformRecipe.SERIALIZER);

		LOGGER.info("These items... they're strange...");

		StrangeItemPayloads.register();

		ServerPlayConnectionEvents.JOIN.register(Id.of("ender_chest_join_sync"), (listener, sender, server) -> {
			ServerPlayer player = listener.player;
			ClientboundSyncEnderChestPacket.sync(player);
		});
	}
}