package embinmc.mod.strangeitems.util;

import embinmc.mod.strangeitems.tracker.Tracker;
import embinmc.mod.strangeitems.tracker.Trackers;
import net.minecraft.nbt.CompoundTag;

public class ElytraTrackerFix implements StrangeDataFixer {
    @Override
    public CompoundTag fix(Tracker tracker, int dataVersion, CompoundTag tag) {
        if (tracker != Trackers.TIME_FLOWN_WITH_ELYTRA) {
            StrangeDataFixer.tryWarn("wanted tracker {}, got {}", Trackers.TIME_FLOWN_WITH_ELYTRA, tracker);
            return tag;
        }
        if (dataVersion > 1) {
            StrangeDataFixer.tryWarn("wanted data version less than 1, got {}", dataVersion);
            return tag;
        }
        if (tag.isEmpty()) {
            StrangeDataFixer.tryWarn("wanted non empty tag, got {}", tag);
            return tag;
        }
        StrangeDataFixer.tryLog("before: {}", tag);
        int result = tag.getIntOr(tracker.id, 0);
        result *= 20;
        tag.putInt(tracker.id, result);
        StrangeDataFixer.tryLog("after: {}", tag);
        return tag;
    }

    @Override
    public String toString() {
        return "ElytraTrackerFix";
    }
}
