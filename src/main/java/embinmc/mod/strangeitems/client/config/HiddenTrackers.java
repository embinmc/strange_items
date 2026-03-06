package embinmc.mod.strangeitems.client.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.tracker.Tracker;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record HiddenTrackers(List<Condition> conditions) {
    public static final Codec<HiddenTrackers> CODEC = RecordCodecBuilder.create(h -> h.group(
            Condition.CODEC.listOf().fieldOf("conditions").forGetter(HiddenTrackers::conditions)
        ).apply(h, HiddenTrackers::new)
    );

    public boolean shouldShowForItem(ItemStack item, Tracker tracker) {
        return this.shouldShowForItem(BuiltInRegistries.ITEM.wrapAsHolder(item.getItem()), StrangeRegistries.TRACKER.wrapAsHolder(tracker));
    }

    public boolean shouldShowForItem(Holder<Item> item, Holder<Tracker> tracker) {
        for (Condition condition : this.conditions) {
            if (condition.affectedItems.contains(item)) {
                if (condition.trackers.contains(tracker)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "HiddenTrackers{" +
                "conditions=" + conditions +
                '}';
    }

    public record Condition(List<Holder<Item>> affectedItems, List<Holder<Tracker>> trackers) {
        public static final Codec<Condition> CODEC = RecordCodecBuilder.create(c -> c.group(
                BuiltInRegistries.ITEM.holderByNameCodec().listOf().fieldOf("items").forGetter(Condition::affectedItems),
                StrangeRegistries.TRACKER.holderByNameCodec().listOf().fieldOf("trackers").forGetter(Condition::trackers)
            ).apply(c, Condition::new)
        );

        @Override
        public String toString() {
            return "Condition{" +
                    "affectedItems=" + affectedItems +
                    ", trackers=" + trackers +
                    '}';
        }
    }
}
