package embinmc.mod.strangeitems.tracker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class IdMapTracker extends MapLikeTracker {
    public static final MapCodec<IdMapTracker> CODEC = RecordCodecBuilder.mapCodec(a -> a.group(
        triggerCodec(), descriptionCodec(), altDescriptionCodec(),
        formatterCodec(), saveIdCodec(), itemsToTrackCodec(), mapSaveIdCodec(),
        Codec.STRING.optionalFieldOf("translation_prefix").forGetter(b -> b.translationPrefix)
    ).apply(a, IdMapTracker::new));

    protected final Optional<String> translationPrefix;

    public IdMapTracker(
            Trigger trigger, Component description,
            Optional<Component> altDescription,
            StatFormatter valueFormatter, Identifier saveId,
            HolderSet<Item> itemsToTrack, Identifier mapSaveId,
            Optional<String> translationPrefix
    ) {
        super(trigger, description, altDescription, valueFormatter, saveId, itemsToTrack, mapSaveId);
        this.translationPrefix = translationPrefix;
    }

    @Override
    public void writeMapToNbt(CompoundTag nbt, int count, @Nullable Identifier data) {
        if (data == null)
            return;
        CompoundTag mapNbt = nbt.getCompoundOrEmpty(this.mapSaveId.toString());
        int oldCount = mapNbt.getIntOr(data.toString(), 0);
        mapNbt.putInt(data.toString(), oldCount + count);
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
            MutableComponent keyText = this.translationPrefix.isEmpty() ? Component.literal(key) :
                    Component.literal(this.getTranslationKey(key));
            keyText.withStyle(ChatFormatting.GRAY);
            Component value = Component.literal(this.getFormattedValue(mapNbt.getIntOr(key, 0))).withStyle(ChatFormatting.YELLOW);
            consumer.accept(Component.empty().append(PREFIX).append(keyText).append(SEP).append(value));
        }
    }

    protected final String getTranslationKey(String key) {
        if (this.translationPrefix.isEmpty())
            return key;
        String translationKey = this.translationPrefix.orElseThrow() + "." + key
                .replace(Identifier.NAMESPACE_SEPARATOR, '.')
                .replace('/', '.');
        return Language.getInstance().getOrDefault(translationKey, key);
    }

    @Override
    public TrackerType<?> getType() {
        return TrackerType.ID_MAP;
    }
}
