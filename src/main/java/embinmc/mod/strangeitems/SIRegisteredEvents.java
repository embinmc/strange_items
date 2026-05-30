package embinmc.mod.strangeitems;

import embinmc.mod.strangeitems.event.ServerPlayerEvents;
import embinmc.mod.strangeitems.event.TrackerEvents;
import embinmc.mod.strangeitems.tracker.Trigger;
import embinmc.mod.strangeitems.util.ElytraTrackerFix;
import embinmc.mod.strangeitems.util.Id;
import embinmc.mod.strangeitems.util.StrangeDataFixer;
import embinmc.mod.strangeitems.util.StrangeUtil;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public final class SIRegisteredEvents {
    public static final Identifier BLOCK_MINED = Id.of("blocks_mined_tracker");
    public static final Identifier ENTITY_ATTACKED = Id.of("mobs_hit_tracker");
    public static final Identifier PLAYER_DROP_ITEM = Id.of("player_drop_item");
    public static final Identifier PLAYER_TICK = Id.of("player_tick");

    public static void registerEvents() {
        PlayerBlockBreakEvents.AFTER.register(BLOCK_MINED, (level, player, blockPos, blockState, blockEntity) -> {
            Identifier blockId = BuiltInRegistries.BLOCK.getKey(blockState.getBlock());
            Trigger.BLOCK_MINED.appendWithData(player.registryAccess(), player.getActiveItem(), 1, blockId);
        });

        ServerPlayerEvents.ON_TICK.register(PLAYER_TICK, player -> {
            if (!player.isSpectator() || !player.touchingUnloadedChunk()) {
                ItemStack headStack = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack legsStack = player.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack footStack = player.getItemBySlot(EquipmentSlot.FEET);

                Trigger.TICK_WEAR_ARMOR.appendWithDimension(player, headStack, chestStack, legsStack, footStack);
                if (player.isEyeInFluid(FluidTags.WATER))
                    Trigger.TICK_UNDERWATER.appendWithDimension(player, headStack, chestStack, legsStack, footStack);
                if (player.isInLava())
                    Trigger.TICK_IN_LAVA.appendWithDimension(player, headStack, chestStack, legsStack, footStack);
                if (player.isDiscrete())
                    Trigger.TICK_SNEAK.appendWithDimension(player, headStack, chestStack, legsStack, footStack);

                if (player.isFallFlying()) {
                    List<EquipmentSlot> slotsWithGlider = EquipmentSlot.VALUES.stream()
                            .filter(slot -> LivingEntity.canGlideUsing(player.getItemBySlot(slot), slot))
                            .toList();
                    for (EquipmentSlot equipmentSlot : slotsWithGlider) {
                        ItemStack gliderItem = player.getItemBySlot(equipmentSlot);
                        Trigger.TICK_GLIDING.appendWithDimension(player, gliderItem);
                    }
                }
            }
            return InteractionResult.PASS;
        });

        ServerPlayerEvents.ON_DROP_ITEM.register(PLAYER_DROP_ITEM, (player, itemStack) -> {
            Trigger.ITEM_DROPPED.appendWithDimension(player, itemStack);
            return InteractionResult.PASS;
        });

        StrangeDataFixer.register(new ElytraTrackerFix());

        TrackerEvents.WRITE_NBT.register(Id.of("data_fix"), (registryAccess, tracker, itemStack, nbt) -> {
            int dataVersion = nbt.getIntOr(StrangeUtil.DATA_VERSION_TAG, 0);
            if (dataVersion >= StrangeItems.DATA_VERSION)
                return true;
            StrangeDataFixer.FIXERS.stream()
                    .sorted(Comparator.comparingInt(StrangeDataFixer::targetDataVersion))
                    .forEach(dataFixer -> dataFixer.fix(dataVersion, nbt));
            nbt.putInt(StrangeUtil.DATA_VERSION_TAG, StrangeItems.DATA_VERSION);
            return true;
        });
    }
}
