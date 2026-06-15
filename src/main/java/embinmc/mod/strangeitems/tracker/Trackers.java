package embinmc.mod.strangeitems.tracker;

import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.util.Id;
import net.minecraft.resources.ResourceKey;

/**
 * Class containing all default trackers.
 */
public class Trackers {
    // pre 2.0
    public static final ResourceKey<Tracker> BLOCKS_MINED = tracker("blocks_mined");
    public static final ResourceKey<Tracker> TIME_FLOWN_WITH_ELYTRA = tracker("time_flown_with_elytra");
    public static final ResourceKey<Tracker> TIMES_DROPPED = tracker("times_dropped");
    public static final ResourceKey<Tracker> MOBS_HIT = tracker("mobs_hit");
    public static final ResourceKey<Tracker> LOGS_STRIPPED = tracker("logs_stripped");
    public static final ResourceKey<Tracker> DIRT_TILLED = tracker("dirt_tilled");
    public static final ResourceKey<Tracker> PATHS_CREATED = tracker("paths_created");
    public static final ResourceKey<Tracker> CAMPFIRES_PUT_OUT = tracker("campfires_put_out");
    public static final ResourceKey<Tracker> CAMPFIRES_LIT = tracker("campfires_lit");
    public static final ResourceKey<Tracker> FIRES_LIT = tracker("fires_lit");
    public static final ResourceKey<Tracker> SHEEP_SHEARED = tracker("sheep_sheared");
    public static final ResourceKey<Tracker> PLANTS_TRIMMED = tracker("plants_trimmed");
    public static final ResourceKey<Tracker> SHOTS_FIRED = tracker("shots_fired");
    public static final ResourceKey<Tracker> SHOTS_HIT = tracker("shots_hit");
    public static final ResourceKey<Tracker> DAMAGE_DEALT = tracker("damage_dealt");
    public static final ResourceKey<Tracker> TRIDENT_THROWN = tracker("trident_thrown");
    public static final ResourceKey<Tracker> BLOCKS_BRUSHED = tracker("blocks_brushed");
    public static final ResourceKey<Tracker> ARMADILLOS_BRUSHED = tracker("armadillos_brushed");
    public static final ResourceKey<Tracker> MOBS_KILLED = tracker("mobs_killed");
    public static final ResourceKey<Tracker> FISH_CAUGHT = tracker("fish_caught");
    public static final ResourceKey<Tracker> DAMAGE_TAKEN = tracker("damage_taken");
    public static final ResourceKey<Tracker> TIMES_EQUIPPED = tracker("times_equipped");
    public static final ResourceKey<Tracker> TIMES_FISHING_ROD_REELED_IN = tracker("times_fishing_rod_reeled_in");
    public static final ResourceKey<Tracker> TIMES_FISHING_ROD_CAST = tracker("times_fishing_rod_cast");
    public static final ResourceKey<Tracker> TIMES_FISHING_ROD_CAUGHT_SOMETHING = tracker("times_fishing_rod_caught_something");
    public static final ResourceKey<Tracker> TIME_UNDERWATER = tracker("time_underwater");
    public static final ResourceKey<Tracker> TIME_SNEAKING = tracker("time_sneaking");
    public static final ResourceKey<Tracker> DISTANCE_FALLEN = tracker("distance_fallen");
    public static final ResourceKey<Tracker> TIME_IN_LAVA = tracker("time_in_lava");
    public static final ResourceKey<Tracker> TIME_IN_DIMENSIONS = tracker("time_in_dimensions");

    // post 2.0
    public static final ResourceKey<Tracker> DAMAGE_BLOCKED = tracker("damage_blocked");
    public static final ResourceKey<Tracker> PUMPKINS_CARVED = tracker("pumpkins_carved");
    public static final ResourceKey<Tracker> ITEMS_RECOVERED_WITH_BRUSH = tracker("items_recovered_with_brush");

    private static ResourceKey<Tracker> tracker(String id) {
        return ResourceKey.create(StrangeRegistryKeys.TRACKER_NEW, Id.of(id));
    }
}
