package embinmc.mod.strangeitems.tracker;

import com.mojang.datafixers.util.Function7;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class MapLikeTracker extends Tracker {
    protected static final Component PREFIX = Component.literal(" > ").withStyle(ChatFormatting.GRAY);
    protected static final Component SEP = Component.literal(": ").withStyle(ChatFormatting.GRAY);
    protected final Identifier mapSaveId;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected MapLikeTracker(
            Trigger trigger, Component description, Optional<Component> altDescription,
            StatFormatter valueFormatter, Identifier saveId, HolderSet<Item> itemsToTrack,
            Identifier mapSaveId
    ) {
        super(trigger, description, altDescription, valueFormatter, saveId, itemsToTrack);
        this.mapSaveId = mapSaveId;
    }

    public abstract void writeMapToNbt(final CompoundTag nbt, final int count, final @Nullable Identifier data);
    public abstract void addAdditionalShowcaseLines(final Consumer<Component> consumer, final HolderLookup.Provider provider, final ItemStack itemStack);

    @Override
    public void writeToNbt(CompoundTag nbt, int count, @Nullable Identifier data) {
        super.writeToNbt(nbt, count, data);
        this.writeMapToNbt(nbt, count, data);
    }

    @Override
    public void addToShowcaseText(Consumer<Component> consumer, HolderLookup.Provider provider, ItemStack itemStack) {
        super.addToShowcaseText(consumer, provider, itemStack);
        this.addAdditionalShowcaseLines(consumer, provider, itemStack);
    }

    protected static <T extends MapLikeTracker> RecordCodecBuilder<T, Identifier> mapSaveIdCodec() {
        return Identifier.CODEC.validate(Tracker::validateSaveId).fieldOf("map_save_id").forGetter(a -> a.mapSaveId);
    }

    protected static <T extends MapLikeTracker> MapCodec<T> noAdditionalArgsMapTrackerCodec(
            Function7<Trigger, Component, Optional<Component>,
            StatFormatter, Identifier, HolderSet<Item>,
            Identifier, T> constructor
    ) {
        return RecordCodecBuilder.mapCodec(a -> a.group(
                triggerCodec(),
                descriptionCodec(),
                altDescriptionCodec(),
                formatterCodec(),
                saveIdCodec(),
                itemsToTrackCodec(),
                mapSaveIdCodec()
        ).apply(a, constructor));
    }
}
