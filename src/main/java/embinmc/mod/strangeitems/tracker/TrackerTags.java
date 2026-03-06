package embinmc.mod.strangeitems.tracker;

import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.util.Id;
import net.minecraft.tags.TagKey;

public class TrackerTags {
    private static TagKey<Tracker> createTag(String name) {
        return TagKey.create(StrangeRegistryKeys.TRACKER, Id.of(name));
    }

    public static final TagKey<Tracker> HAS_SPECIAL_TOOLTIP = createTag("has_special_tooltip");
    public static final TagKey<Tracker> MAP_TRACKERS = createTag("map_trackers");
    public static final TagKey<Tracker> TIMESTAMP_TRACKERS = createTag("timestamp_trackers");
    public static final TagKey<Tracker> TOOLTIP_ORDER = createTag("tooltip_order");
}
