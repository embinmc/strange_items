package embinmc.mod.strangeitems;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import embinmc.mod.strangeitems.util.StrangeUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class CollectorsTransformRecipe extends SimpleSmithingRecipe {
    private final Ingredient target, transformer;

    public static final MapCodec<CollectorsTransformRecipe> CODEC = RecordCodecBuilder.mapCodec(c -> c.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            Ingredient.CODEC.fieldOf("target").forGetter(o -> o.target),
            Ingredient.CODEC.fieldOf("transformer").forGetter(o -> o.transformer)
    ).apply(c, CollectorsTransformRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CollectorsTransformRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC,
            o -> o.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.target,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.transformer,
            CollectorsTransformRecipe::new
    );
    public static final RecipeSerializer<CollectorsTransformRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    protected CollectorsTransformRecipe(CommonInfo commonInfo, Ingredient target, Ingredient transformer) {
        super(commonInfo);
        this.target = target;
        this.transformer = transformer;
    }

    @Override
    public @NonNull ItemStack assemble(SmithingRecipeInput input) {
        ItemStack itemStack = new ItemStack(input.base().typeHolder(), input.base().count(), input.base().getComponentsPatch());
        itemStack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, cd -> cd.update(nbt -> nbt.putBoolean(StrangeUtil.COLLECTORS_ITEM_TAG, true)));
        return itemStack;
    }

    @Override
    public boolean matches(@NonNull SmithingRecipeInput input, @NonNull Level level) {
        return super.matches(input, level) && input.base().getItem() == input.addition().getItem();
    }

    @Override
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return CollectorsTransformRecipe.SERIALIZER;
    }

    @Override
    public @NonNull Optional<Ingredient> templateIngredient() {
        return Optional.of(this.transformer);
    }

    @Override
    public @NonNull Ingredient baseIngredient() {
        return this.target;
    }

    @Override
    public @NonNull Optional<Ingredient> additionIngredient() {
        return Optional.of(this.target);
    }

    @Override
    protected @NonNull PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(
                this.transformer,
                this.target,
                this.target
        ));
    }
}
