package embinmc.mod.strangeitems.util;

import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.tracker.TimestampTracker;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;

import java.time.Instant;
import java.util.Date;

public interface StatFormatters {
    StatFormatter NONE = register("none", String::valueOf);
    StatFormatter DEFAULT = registerVanilla("default", StatFormatter.DEFAULT);
    StatFormatter DIVIDE_BY_TEN_VANILLA = registerVanilla("divide_by_ten", StatFormatter.DIVIDE_BY_TEN);
    StatFormatter DISTANCE = registerVanilla("distance", StatFormatter.DISTANCE);
    StatFormatter TIME = registerVanilla("time", StatFormatter.TIME);
    StatFormatter DIVIDE_BY_TEN = register("divide_by_ten", value -> "%,.2f".formatted(value / 10f));
    StatFormatter HOURS = register("hours", value -> {
        double seconds = value / 20D;
        double minutes = seconds / 60D;
        double hours = minutes / 60D;
        return "%,.2f h".formatted(hours);
    });
    StatFormatter TIMESTAMP = register("timestamp", value -> TimestampTracker.DATE_FORMAT.format(Date.from(Instant.ofEpochSecond(value))));

    private static StatFormatter register(final String id, StatFormatter formatter) {
        return Registry.register(StrangeRegistries.STAT_FORMATTER, Id.of(id), formatter);
    }

    private static StatFormatter registerVanilla(final String id, StatFormatter formatter) {
        return Registry.register(StrangeRegistries.STAT_FORMATTER, Identifier.withDefaultNamespace(id), formatter);
    }
}
