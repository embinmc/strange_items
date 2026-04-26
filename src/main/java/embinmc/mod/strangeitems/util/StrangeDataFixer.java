package embinmc.mod.strangeitems.util;

import com.mojang.logging.LogUtils;
import embinmc.mod.strangeitems.StrangeItems;
import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.tracker.MapTracker;
import embinmc.mod.strangeitems.tracker.TimestampTracker;
import embinmc.mod.strangeitems.tracker.Tracker;
import embinmc.mod.strangeitems.tracker.TrackerItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public interface StrangeDataFixer {
    boolean EXCESSIVE_LOGGING = false;
    Logger LOGGER = LogUtils.getLogger();
    List<StrangeDataFixer> FIXERS = new ArrayList<>(64);

    CompoundTag fix(Tracker tracker, int dataVersion, CompoundTag tag);

    static StrangeDataFixer register(final StrangeDataFixer fixer) {
        FIXERS.add(fixer);
        return fixer;
    }

    static ItemStack apply(final ItemStack itemStack) {
        if (Thread.currentThread().getName().equals("Render thread"))
            return itemStack;
        if (!(itemStack.is(TrackerItemTags.CAN_TRACK_STATS) || StrangeUtil.hasAllTrackers(itemStack)))
            return itemStack;

        boolean hadCustomDataBefore = itemStack.has(DataComponents.CUSTOM_DATA);
        if (!hadCustomDataBefore) {
            CompoundTag newTag = new CompoundTag();
            newTag.putInt(StrangeUtil.DATA_VERSION_TAG, StrangeItems.DATA_VERSION);
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag));
            return itemStack;
        }

        boolean hadStrangeItemsDataBefore = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).toString().contains(StrangeItems.MOD_ID);
        if (!hadStrangeItemsDataBefore) {
            CustomData.update(DataComponents.CUSTOM_DATA, itemStack, compoundTag -> {
                compoundTag.putInt(StrangeUtil.DATA_VERSION_TAG, StrangeItems.DATA_VERSION);
            });
            return itemStack;
        }

        CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int dv = tag.getIntOr(StrangeUtil.DATA_VERSION_TAG, 0);
        if (dv >= StrangeItems.DATA_VERSION)
            return itemStack;
        for (Tracker tracker : StrangeRegistries.TRACKER) {
            if (tracker.stackHasTracker(itemStack)) {
                CompoundTag fixingTag = switch (tracker) {
                    case TimestampTracker timestampTracker -> {
                        CompoundTag compoundTag = new CompoundTag();
                        compoundTag.putInt(timestampTracker.id, timestampTracker.getTrackerValueInt(itemStack));
                        compoundTag.put(timestampTracker.map_id, timestampTracker.getTrackerValueNbt(itemStack));
                        yield compoundTag;
                    }
                    case MapTracker mapTracker -> {
                        CompoundTag compoundTag = new CompoundTag();
                        compoundTag.putInt(mapTracker.id, mapTracker.getTrackerValueInt(itemStack));
                        compoundTag.put(mapTracker.map_id, mapTracker.getTrackerValueNbt(itemStack));
                        yield compoundTag;
                    }
                    default -> {
                        CompoundTag compoundTag = new CompoundTag();
                        compoundTag.putInt(tracker.id, tracker.getTrackerValueInt(itemStack));
                        yield compoundTag;
                    }
                };
                for (StrangeDataFixer dataFixer : FIXERS) {
                    StrangeDataFixer.tryLog("attempting apply {} with tracker {}", dataFixer, tracker);
                    fixingTag = dataFixer.fix(tracker, dv, fixingTag);
                }
                tag.merge(fixingTag);
            }
        }
        tag.putInt(StrangeUtil.DATA_VERSION_TAG, StrangeItems.DATA_VERSION);
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        LOGGER.info("Fixed data on item stack \"{}\", from version {} to {}", itemStack, dv, StrangeItems.DATA_VERSION);
        return itemStack;
    }

    static void tryLog(String info, Object... args) {
        if (EXCESSIVE_LOGGING)
            LOGGER.info(info, args);
    }

    static void tryWarn(String info, Object... args) {
        if (EXCESSIVE_LOGGING)
            LOGGER.warn(info, args);
    }
}
