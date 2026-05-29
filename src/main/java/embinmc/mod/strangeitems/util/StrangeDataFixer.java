package embinmc.mod.strangeitems.util;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public interface StrangeDataFixer {
    boolean EXCESSIVE_LOGGING = false;
    Logger LOGGER = LogUtils.getLogger();
    List<StrangeDataFixer> FIXERS = new ArrayList<>(64);

    void fix(int dataVersion, @NonNull CompoundTag nbt);
    int targetDataVersion();

    static StrangeDataFixer register(final StrangeDataFixer fixer) {
        FIXERS.add(fixer);
        return fixer;
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
