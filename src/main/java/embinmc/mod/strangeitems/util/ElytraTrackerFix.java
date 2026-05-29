package embinmc.mod.strangeitems.util;

import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.NonNull;

public class ElytraTrackerFix implements StrangeDataFixer {
    public static String STAT_FIELD = "strangeitems:time_flown_with_elytra";

    @Override
    public void fix(int dataVersion, @NonNull CompoundTag tag) {
        if (dataVersion > this.targetDataVersion()) {
            StrangeDataFixer.tryWarn("wanted data version less than 1, got {}", dataVersion);
            return;
        }
        if (!tag.contains(STAT_FIELD)) {
            StrangeDataFixer.tryWarn("wanted tag with field " + STAT_FIELD);
            return;
        }
        StrangeDataFixer.tryLog("before: {}", tag);
        int result = tag.getIntOr(STAT_FIELD, 0);
        result *= 20;
        tag.putInt(STAT_FIELD, result);
        StrangeDataFixer.tryLog("after: {}", tag);
    }

    @Override
    public int targetDataVersion() {
        return DataVersions.PRE_V1_6;
    }

    @Override
    public String toString() {
        return "ElytraTrackerFix";
    }
}
