package embinmc.mod.strangeitems.tracker;

import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.util.Id;
import net.minecraft.tags.TagKey;

public class TrackerTags {
    private static TagKey<Tracker> createTag(String name) {
        return TagKey.create(StrangeRegistryKeys.TRACKER_NEW, Id.of(name));
    }

    public static final TagKey<Tracker> TOOLTIP_ORDER = createTag("tooltip_order");
    public static final TagKey<Tracker> MENU_ORDER = createTag("menu_order");
}
