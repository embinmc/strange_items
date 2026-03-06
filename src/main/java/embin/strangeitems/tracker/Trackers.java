package embin.strangeitems.tracker;

import embin.strangeitems.StrangeItems;
import embin.strangeitems.StrangeRegistries;
import embin.strangeitems.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.stats.StatFormatter;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Class containing all default registered trackers.
 */
public class Trackers {
    public static final MapTracker BLOCKS_MINED = registerMap("blocks_mined", "block", TrackerItemTags.TRACKER_BLOCKS_MINED);
    public static final Tracker TIME_FLOWN_WITH_ELYTRA = register("time_flown_with_elytra", TrackerItemTags.TRACKER_TIME_FLOWN, StatFormatter.TIME, 20);
    public static final TimestampTracker TIMES_DROPPED = registerTimestamp("times_dropped");
    public static final Tracker MOBS_HIT = register("mobs_hit");
    public static final Tracker LOGS_STRIPPED = register("logs_stripped", TrackerItemTags.TRACKER_LOGS_STRIPPED);
    public static final Tracker DIRT_TILLED = register("dirt_tilled", TrackerItemTags.TRACKER_DIRT_TILLED);
    public static final Tracker PATHS_CREATED = register("paths_created", TrackerItemTags.TRACKER_PATHS_CREATED);
    public static final Tracker CAMPFIRES_PUT_OUT = register("campfires_put_out", TrackerItemTags.TRACKER_CAMPFIRES_PUT_OUT);
    public static final Tracker CAMPFIRES_LIT = register("campfires_lit", TrackerItemTags.TRACKER_CAMPFIRES_LIT);
    public static final Tracker FIRES_LIT = register("fires_lit", TrackerItemTags.TRACKER_FIRES_LIT);
    public static final Tracker SHEEP_SHEARED = register("sheep_sheared", TrackerItemTags.TRACKER_SHEEP_SHEARED);
    public static final Tracker PLANTS_TRIMMED = register("plants_trimmed", TrackerItemTags.TRACKER_PLANTS_TRIMMED);
    public static final Tracker SHOTS_FIRED = register("shots_fired", TrackerItemTags.TRACKER_SHOTS_FIRED);
    public static final MapTracker SHOTS_HIT = registerMap("shots_hit", "entity", TrackerItemTags.TRACKER_SHOTS_HIT);
    public static final Tracker DAMAGE_DEALT = register("damage_dealt", TrackerItemTags.TRACKER_DAMAGE_DEALT, StatFormatter.DIVIDE_BY_TEN, 1);
    public static final Tracker TRIDENT_THROWN = register("trident_thrown", TrackerItemTags.TRACKER_TRIDENT_THROWN);
    public static final Tracker BLOCKS_BRUSHED = register("blocks_brushed", TrackerItemTags.TRACKER_BLOCKS_BRUSHED);
    public static final Tracker ARMADILLOS_BRUSHED = register("armadillos_brushed", TrackerItemTags.TRACKER_ARMADILLOS_BRUSHED);
    public static final MapTracker MOBS_KILLED = registerMap("mobs_killed", "entity", TrackerItemTags.TRACKER_MOBS_KILLED);
    public static final Tracker FISH_CAUGHT = register("fish_caught", TrackerItemTags.TRACKER_FISH_CAUGHT);
    public static final Tracker DAMAGE_TAKEN = register("damage_taken", TrackerItemTags.TRACKER_DAMAGE_TAKEN, StatFormatter.DIVIDE_BY_TEN, 1);
    public static final Tracker TIMES_EQUIPPED = register("times_equipped", TrackerItemTags.TRACKER_TIMES_EQUIPPED);
    public static final Tracker TIMES_FISHING_ROD_REELED_IN = register("times_fishing_rod_reeled_in", TrackerItemTags.TRACKER_TIMES_FISHING_ROD_REELED_IN);
    public static final Tracker TIMES_FISHING_ROD_CAST = register("times_fishing_rod_cast", TrackerItemTags.TRACKER_TIMES_FISHING_ROD_CAST);
    public static final Tracker TIMES_FISHING_ROD_CAUGHT_SOMETHING = register("times_fishing_rod_caught_something", TrackerItemTags.TRACKER_TIMES_FISHING_CAUGHT_SOMETHING);
    public static final Tracker TIME_UNDERWATER = register("time_underwater", TrackerItemTags.TRACKER_TIME_UNDERWATER, StatFormatter.TIME, 1);
    public static final Tracker TIME_SNEAKING = register("time_sneaking", TrackerItemTags.TRACKER_TIME_SNEAKING, StatFormatter.TIME, 1);
    public static final Tracker DISTANCE_FALLEN = register("distance_fallen", TrackerItemTags.TRACKER_DISTANCE_FALLEN, StatFormatter.DISTANCE, 1);
    public static final Tracker TIME_IN_LAVA = register("time_in_lava", TrackerItemTags.TRACKER_TIME_IN_LAVA, StatFormatter.TIME, 1);
    public static final MapTracker TIME_IN_DIMENSIONS = registerMap("time_in_dimensions", "dimension", TrackerItemTags.TRACKER_TIME_IN_DIMENSIONS, StatFormatter.TIME);

    private static Tracker register(String id) {
        return Registry.register(StrangeRegistries.TRACKER, Id.of(id), new Tracker(id, TrackerItemTags.CAN_TRACK_STATS));
    }

    private static Tracker register(String id, TagKey<Item> tag) {
        return Registry.register(StrangeRegistries.TRACKER, Id.of(id), new Tracker(id, tag));
    }

    private static Tracker register(String id, TagKey<Item> tag, StatFormatter stat_formatter, int m) {
        return Registry.register(StrangeRegistries.TRACKER, Id.of(id), new Tracker(id, tag, stat_formatter, m));
    }

    private static TimestampTracker registerTimestamp(String id) {
        return Registry.register(StrangeRegistries.TRACKER, Id.of(id), new TimestampTracker(id));
    }

    private static TimestampTracker registerTimestamp(String id, TagKey<Item> tag) {
        return Registry.register(StrangeRegistries.TRACKER, Id.of(id), new TimestampTracker(id, tag));
    }

    private static MapTracker registerMap(String id, String prefix) {
        return Registry.register(StrangeRegistries.TRACKER, Id.of(id), new MapTracker(id, prefix));
    }

    private static MapTracker registerMap(String id, String prefix, TagKey<Item> tag) {
        return Registry.register(StrangeRegistries.TRACKER, Id.of(id), new MapTracker(id, prefix, tag));
    }

    private static MapTracker registerMap(String id, String prefix, TagKey<Item> tag, StatFormatter stat_formatter) {
        return Registry.register(StrangeRegistries.TRACKER, Id.of(id), new MapTracker(id, prefix, tag, stat_formatter));
    }

    public static void init() {
        StrangeItems.LOGGER.info("Registering trackers...");
    }
}
