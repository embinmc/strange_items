package embinmc.mod.strangeitems.datagen;

import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.tracker.*;
import embinmc.mod.strangeitems.util.StatFormatters;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.StatFormatter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TrackerDataProvider extends FabricDynamicRegistryProvider {
    public TrackerDataProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, @NonNull Entries entries) {
        entries.addAll(provider.lookupOrThrow(StrangeRegistryKeys.TRACKER_NEW));
    }

    @Override
    public @NonNull String getName() {
        return "Trackers";
    }

    private static void registerBasic(BootstrapContext<Tracker> context, ResourceKey<Tracker> key, Trigger trigger, StatFormatter statFormatter, TagKey<Item> itemTag) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        Identifier trackerId = key.identifier();
        context.register(key, new BasicTracker(
                trigger,
                Component.translatable("tracker." + trackerId.getNamespace() + "." + trackerId.getPath()
                                .replace('/', '.')).withStyle(ChatFormatting.GRAY),
                Optional.empty(), statFormatter, trackerId, items.getOrThrow(itemTag)
                )
        );
    }

    private static void registerBasic(BootstrapContext<Tracker> context, ResourceKey<Tracker> key, Trigger trigger, Item item) {
        Identifier trackerId = key.identifier();
        context.register(key, new BasicTracker(
                        trigger,
                        Component.translatable("tracker." + trackerId.getNamespace() + "." + trackerId.getPath()
                                .replace('/', '.')).withStyle(ChatFormatting.GRAY),
                        Optional.empty(), StatFormatters.DEFAULT, trackerId, HolderSet.direct(item.builtInRegistryHolder())
                )
        );
    }

    private static void registerBasic(BootstrapContext<Tracker> context, ResourceKey<Tracker> key, Trigger trigger, TagKey<Item> itemTag) {
        registerBasic(context, key, trigger, StatFormatters.DEFAULT, itemTag);
    }

    private static void registerTimestamp(BootstrapContext<Tracker> context, ResourceKey<Tracker> key, Trigger trigger, TagKey<Item> itemTag) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        Identifier trackerId = key.identifier();
        context.register(key, new TimestampTracker(
                trigger,
                Component.translatable("tracker." + trackerId.getNamespace() + "." + trackerId.getPath()
                        .replace('/', '.')).withStyle(ChatFormatting.GRAY),
                Optional.empty(), StatFormatters.DEFAULT, trackerId, items.getOrThrow(itemTag),
                Identifier.fromNamespaceAndPath(trackerId.getNamespace(), trackerId.getPath() + "_map")
                )
        );
    }

    private static void registerIdMap(BootstrapContext<Tracker> context, ResourceKey<Tracker> key, Trigger trigger, StatFormatter statFormatter, TagKey<Item> itemTag, String translationPrefix) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        Identifier trackerId = key.identifier();
        context.register(key, new IdMapTracker(
                trigger,
                Component.translatable("tracker." + trackerId.getNamespace() + "." + trackerId.getPath()
                        .replace('/', '.')).withStyle(ChatFormatting.GRAY),
                Optional.empty(), statFormatter, trackerId, items.getOrThrow(itemTag),
                Identifier.fromNamespaceAndPath(trackerId.getNamespace(), trackerId.getPath() + "_map"),
                Optional.of(translationPrefix)
            )
        );
    }

    private static void registerIdMap(BootstrapContext<Tracker> context, ResourceKey<Tracker> key, Trigger trigger, StatFormatter statFormatter, TagKey<Item> itemTag) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        Identifier trackerId = key.identifier();
        context.register(key, new IdMapTracker(
                        trigger,
                        Component.translatable("tracker." + trackerId.getNamespace() + "." + trackerId.getPath()
                                .replace('/', '.')).withStyle(ChatFormatting.GRAY),
                        Optional.empty(), statFormatter, trackerId, items.getOrThrow(itemTag),
                        Identifier.fromNamespaceAndPath(trackerId.getNamespace(), trackerId.getPath() + "_map"),
                        Optional.empty()
                )
        );
    }

    public static void bootstrap(BootstrapContext<Tracker> c) {
        HolderGetter<Item> items = c.lookup(Registries.ITEM);
        registerIdMap(c, Trackers.BLOCKS_MINED, Trigger.BLOCK_MINED, StatFormatters.DEFAULT, TrackerItemTags.TRACKER_BLOCKS_MINED, "block");
        registerBasic(c, Trackers.TIME_FLOWN_WITH_ELYTRA, Trigger.TICK_GLIDING, StatFormatters.TIME, TrackerItemTags.TRACKER_GLIDER);
        registerTimestamp(c, Trackers.TIMES_DROPPED, Trigger.ITEM_DROPPED, TrackerItemTags.CAN_TRACK_STATS);
        registerBasic(c, Trackers.MOBS_HIT, Trigger.HIT_MOB, TrackerItemTags.CAN_TRACK_STATS);
        registerBasic(c, Trackers.LOGS_STRIPPED, Trigger.STRIP_BLOCK_WITH_AXE, TrackerItemTags.TRACKER_LOGS_STRIPPED);
        registerBasic(c, Trackers.DIRT_TILLED, Trigger.TILL_DIRT, TrackerItemTags.TRACKER_HOE);
        registerBasic(c, Trackers.PATHS_CREATED, Trigger.CREATE_PATH_BLOCK, TrackerItemTags.TRACKER_SHOVEL);
        registerBasic(c, Trackers.CAMPFIRES_PUT_OUT, Trigger.PUT_OUT_CAMPFIRE, TrackerItemTags.TRACKER_SHOVEL);
        registerBasic(c, Trackers.CAMPFIRES_LIT, Trigger.RELIGHT_CAMPFIRE, TrackerItemTags.TRACKER_IGNITER);
        registerBasic(c, Trackers.FIRES_LIT, Trigger.IGNITE_FIRE, TrackerItemTags.TRACKER_IGNITER);
        registerBasic(c, Trackers.SHEEP_SHEARED, Trigger.SHEAR_SHEEP, TrackerItemTags.TRACKER_SHEARS);
        registerBasic(c, Trackers.PLANTS_TRIMMED, Trigger.TRIM_PLANT, TrackerItemTags.TRACKER_SHEARS);
        registerBasic(c, Trackers.SHOTS_FIRED, Trigger.SHOOT_ARROW, TrackerItemTags.TRACKER_BOW);
        registerIdMap(c, Trackers.SHOTS_HIT, Trigger.ARROW_HIT_MOB, StatFormatters.DEFAULT, TrackerItemTags.TRACKER_BOW, "entity");
        registerBasic(c, Trackers.DAMAGE_DEALT, Trigger.DEAL_DAMAGE, StatFormatters.DIVIDE_BY_TEN, TrackerItemTags.TRACKER_WEAPON);
        registerBasic(c, Trackers.TRIDENT_THROWN, Trigger.THROW_TRIDENT, Items.TRIDENT);
        registerBasic(c, Trackers.BLOCKS_BRUSHED, Trigger.ATTEMPT_BRUSH_BLOCK, TrackerItemTags.TRACKER_BRUSH);
        registerBasic(c, Trackers.ARMADILLOS_BRUSHED, Trigger.BRUSH_ARMADILLO, TrackerItemTags.TRACKER_BRUSH);
        registerIdMap(c, Trackers.MOBS_KILLED, Trigger.KILL_MOB, StatFormatters.DEFAULT, TrackerItemTags.TRACKER_MOBS_KILLED, "entity");
        registerBasic(c, Trackers.FISH_CAUGHT, Trigger.CATCH_FISH_WITH_FISHING_ROD, TrackerItemTags.TRACKER_FISHING_ROD);
        registerIdMap(c, Trackers.DAMAGE_TAKEN, Trigger.TAKE_DAMAGE, StatFormatters.DIVIDE_BY_TEN, TrackerItemTags.TRACKER_ARMOR);
        registerBasic(c, Trackers.TIMES_EQUIPPED, Trigger.EQUIP_ITEM, TrackerItemTags.TRACKER_EQUIPPABLE);
        registerBasic(c, Trackers.TIMES_FISHING_ROD_REELED_IN, Trigger.REEL_IN_FISHING_ROD, TrackerItemTags.TRACKER_FISHING_ROD);
        registerBasic(c, Trackers.TIMES_FISHING_ROD_CAST, Trigger.CAST_FISHING_ROD, TrackerItemTags.TRACKER_FISHING_ROD);
        registerIdMap(c, Trackers.TIMES_FISHING_ROD_CAUGHT_SOMETHING, Trigger.CATCH_ITEM_WITH_FISHING_ROD, StatFormatters.DEFAULT, TrackerItemTags.TRACKER_FISHING_ROD);
        registerBasic(c, Trackers.TIME_UNDERWATER, Trigger.TICK_UNDERWATER, StatFormatters.TIME, TrackerItemTags.TRACKER_TIME_UNDERWATER);
        registerBasic(c, Trackers.TIME_SNEAKING, Trigger.TICK_SNEAK, StatFormatters.TIME, TrackerItemTags.TRACKER_TIME_SNEAKING);
        registerBasic(c, Trackers.DISTANCE_FALLEN, Trigger.FALL, StatFormatters.DISTANCE, TrackerItemTags.TRACKER_DISTANCE_FALLEN);
        registerBasic(c, Trackers.TIME_IN_LAVA, Trigger.TICK_SNEAK, StatFormatters.TIME, TrackerItemTags.TRACKER_TIME_IN_LAVA);
        registerIdMap(c, Trackers.TIME_IN_DIMENSIONS, Trigger.TICK_WEAR_ARMOR, StatFormatters.TIME, TrackerItemTags.TRACKER_TIME_IN_DIMENSIONS, "dimension");

        registerIdMap(c, Trackers.DAMAGE_BLOCKED, Trigger.DAMAGE_BLOCKED_BY_SHIELD, StatFormatters.DIVIDE_BY_TEN, TrackerItemTags.TRACKER_SHIELD);
        registerBasic(c, Trackers.PUMPKINS_CARVED, Trigger.CARVE_PUMPKIN, TrackerItemTags.TRACKER_SHEARS);
        registerIdMap(c, Trackers.ITEMS_RECOVERED_WITH_BRUSH, Trigger.BRUSH_BLOCK_SUCCEEDS, StatFormatters.DEFAULT, TrackerItemTags.TRACKER_BRUSH, "item");
    }
}
