package embinmc.mod.strangeitems;

import embinmc.mod.strangeitems.event.ServerPlayerEvents;
import embinmc.mod.strangeitems.event.TrackerEvents;
import embinmc.mod.strangeitems.tracker.Trackers;
import embinmc.mod.strangeitems.util.ElytraTrackerFix;
import embinmc.mod.strangeitems.util.Id;
import embinmc.mod.strangeitems.util.StrangeDataFixer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class SIRegisteredEvents {
    public static final Identifier BLOCK_MINED = Id.of("blocks_mined_tracker");
    public static final Identifier ENTITY_ATTACKED = Id.of("mobs_hit_tracker");
    public static final Identifier PLAYER_DROP_ITEM = Id.of("player_drop_item");
    public static final Identifier PLAYER_TICK = Id.of("player_tick");

    public static void registerEvents() {
        PlayerBlockBreakEvents.AFTER.register(BLOCK_MINED, (level, player, blockPos, blockState, blockEntity) -> {
            Identifier blockId = BuiltInRegistries.BLOCK.getKey(blockState.getBlock());
            Trackers.BLOCKS_MINED.appendTracker(player.getActiveItem(), blockId.toString());
        });

        ServerPlayerEvents.ON_TICK.register(PLAYER_TICK, player -> {
            if (!player.isSpectator() || !player.touchingUnloadedChunk()) {
                ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack legsStack = player.getItemBySlot(EquipmentSlot.LEGS);

                if (!headStack.isEmpty()) {
                    if (player.isEyeInFluid(FluidTags.WATER)) {
                        Trackers.TIME_UNDERWATER.appendTracker(headStack);
                    }
                }
                if (!chestStack.isEmpty()) {
                    if (player.isInLava()) {
                        Trackers.TIME_IN_LAVA.appendTracker(chestStack);
                    }
                }
                if (!legsStack.isEmpty()) {
                    String dimension = player.level().dimensionTypeRegistration().getRegisteredName();
                    Trackers.TIME_IN_DIMENSIONS.appendTracker(legsStack, dimension);
                    if (player.isDiscrete()) {
                        Trackers.TIME_SNEAKING.appendTracker(legsStack);
                    }
                }
                if (player.isFallFlying()) {
                    List<EquipmentSlot> slotsWithGlider = EquipmentSlot.VALUES.stream()
                            .filter(slot -> LivingEntity.canGlideUsing(player.getItemBySlot(slot), slot))
                            .toList();
                    for (EquipmentSlot equipmentSlot : slotsWithGlider) {
                        ItemStack gliderItem = player.getItemBySlot(equipmentSlot);
                        Trackers.TIME_FLOWN_WITH_ELYTRA.appendTracker(gliderItem);
                    }
                }
            }
            return InteractionResult.PASS;
        });

        ServerPlayerEvents.ON_DROP_ITEM.register(PLAYER_DROP_ITEM, (player, itemStack) -> {
            Trackers.TIMES_DROPPED.appendTracker(itemStack);
            return InteractionResult.PASS;
        });

        StrangeDataFixer.register(new ElytraTrackerFix());

        TrackerEvents.ON_APPEND.register(Id.of("data_fix"), (tracker, itemStack, increaseAmount) -> {
            StrangeDataFixer.apply(itemStack);
            return true;
        });
    }
}
