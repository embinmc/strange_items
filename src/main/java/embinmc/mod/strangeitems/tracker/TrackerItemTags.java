package embinmc.mod.strangeitems.tracker;

import embinmc.mod.strangeitems.util.Id;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class TrackerItemTags {
    private static TagKey<Item> createTag(String name) {
        return TagKey.create(Registries.ITEM, Id.of(name));
    }

    public static final TagKey<Item> CAN_TRACK_STATS = createTag("can_track_stats");
    public static final TagKey<Item> TRACKER_BLOCKS_MINED = createTag("trackers/blocks_mined");
    public static final TagKey<Item> TRACKER_LOGS_STRIPPED = createTag("trackers/logs_stripped");
    public static final TagKey<Item> TRACKER_MOBS_KILLED = createTag("trackers/mobs_killed");
    public static final TagKey<Item> TRACKER_TIME_UNDERWATER = createTag("trackers/time_underwater");
    public static final TagKey<Item> TRACKER_TIME_SNEAKING = createTag("trackers/time_sneaking");
    public static final TagKey<Item> TRACKER_DISTANCE_FALLEN = createTag("trackers/distance_fallen");
    public static final TagKey<Item> TRACKER_TIME_IN_LAVA = createTag("trackers/time_in_lava");
    public static final TagKey<Item> TRACKER_TIME_IN_DIMENSIONS = createTag("trackers/time_in_dimensions");

    public static final TagKey<Item> TRACKER_FISHING_ROD = createTag("trackers/fishing_rod");
    public static final TagKey<Item> TRACKER_SHIELD = createTag("trackers/shield");
    public static final TagKey<Item> TRACKER_BRUSH = createTag("trackers/brush");
    public static final TagKey<Item> TRACKER_BOW = createTag("trackers/bow");
    public static final TagKey<Item> TRACKER_WEAPON = createTag("trackers/weapon");
    public static final TagKey<Item> TRACKER_GLIDER = createTag("trackers/glider");
    public static final TagKey<Item> TRACKER_IGNITER = createTag("trackers/igniter");
    public static final TagKey<Item> TRACKER_HOE = createTag("trackers/hoe");
    public static final TagKey<Item> TRACKER_SHOVEL = createTag("trackers/shovel");
    public static final TagKey<Item> TRACKER_SHEARS = createTag("trackers/shears");
    public static final TagKey<Item> TRACKER_EQUIPPABLE = createTag("trackers/equippable");
    public static final TagKey<Item> TRACKER_ARMOR = createTag("trackers/armor");

}
