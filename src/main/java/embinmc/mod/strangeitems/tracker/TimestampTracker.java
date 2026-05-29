package embinmc.mod.strangeitems.tracker;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jspecify.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

public class TimestampTracker extends MapLikeTracker {
    public static final MapCodec<TimestampTracker> CODEC = noAdditionalArgsMapTrackerCodec(TimestampTracker::new);
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MMMM-dd HH:mm:ss");

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public TimestampTracker(
            Trigger trigger, Component description,
            Optional<Component> altDescription,
            StatFormatter valueFormatter, Identifier saveId,
            HolderSet<Item> itemsToTrack, Identifier mapSaveId
    ) {
        super(trigger, description, altDescription, valueFormatter, saveId, itemsToTrack, mapSaveId);
    }

    @Override
    public void writeMapToNbt(CompoundTag nbt, int count, @Nullable Identifier data) {
        long currentSecond = Instant.now().getEpochSecond(); // thought i used ints originally but i actually didn't, thanks past me for future proofing!
        CompoundTag mapNbt = nbt.getCompoundOrEmpty(this.mapSaveId.toString());
        int key = nbt.getIntOr(this.saveId.toString(), 1);
        mapNbt.putLong(String.valueOf(key), currentSecond);
        nbt.put(this.mapSaveId.toString(), mapNbt); // actually add the map to the og nbt
    }

    @Override
    public void addAdditionalShowcaseLines(Consumer<Component> consumer, HolderLookup.Provider provider, ItemStack itemStack) {
        CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        Optional<CompoundTag> mapOptional = nbt.getCompound(this.mapSaveId.toString());
        if (mapOptional.isEmpty())
            return;
        CompoundTag mapNbt = mapOptional.orElseThrow();
        for (String key : mapNbt.keySet()) {
            long second = mapNbt.getLongOr(key, 0L);
            Component timestamp = Component.literal(DATE_FORMAT.format(Date.from(Instant.ofEpochSecond(second)))).withStyle(ChatFormatting.GRAY);
            consumer.accept(Component.empty().withStyle(ChatFormatting.YELLOW).append(PREFIX).append(key).append(SEP).append(timestamp));
        }
    }

    @Override
    public TrackerType<?> getType() {
        return TrackerType.TIMESTAMP;
    }
}
