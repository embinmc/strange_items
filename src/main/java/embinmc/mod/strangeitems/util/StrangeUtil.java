package embinmc.mod.strangeitems.util;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import embinmc.mod.strangeitems.StrangeItems;
import embinmc.mod.strangeitems.StrangeItemsComponents;
import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.client.config.StrangeConfig;
import embinmc.mod.strangeitems.mixin.KeyBindAccessor;
import embinmc.mod.strangeitems.tracker.Tracker;
import embinmc.mod.strangeitems.tracker.TrackerTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class StrangeUtil {
    public static final String COLLECTORS_ITEM_TAG  = Id.of("collectors_item").toString();
    public static final String HAS_ALL_TRACKERS_TAG = Id.of("has_all_trackers").toString();

    /**
     * Gets the keys from an NBT Compound, sorted from the highest to lowest value.
     * @param nbtCompound An NBT Compound where each value is an integer.
     * @return The sorted keys of the given NBT Compound.
     */
    public static List<String> getSortedKeys(CompoundTag nbtCompound) {
        List<String> sorted = new java.util.ArrayList<>(List.of());
        List<String> unsorted = nbtCompound.keySet().stream().toList();
        for (String key : unsorted) {
           sorted.add(key);
           int value = nbtCompound.getIntOr(key, 0);
           if (sorted.size() > 1) {
               while (true) {
                   int index = sorted.indexOf(key);
                   String key_ahead;
                   try {
                       key_ahead = sorted.get(index - 1);
                   } catch (IndexOutOfBoundsException e) {
                       break;
                   }
                   int value_ahead = nbtCompound.getInt(key_ahead).orElseThrow();
                   if (value > value_ahead) {
                       sorted.remove(index);
                       sorted.add(index - 1, key);
                   } else {
                       break;
                   }
               }
           }
        }
        return sorted;
    }

    /**
     * Check if the specified keybinding is currently being pressed or not.
     * @param key The keybinding to check for.
     * @return <code>true</code> if the given key is currently held down;
     * <code>false</code> if it isn't.
     */
    public static boolean isKeyDown(KeyMapping key) {
        Window handle = Minecraft.getInstance().getWindow();
        int key_code = ((KeyBindAccessor)key).getKey().getValue();
        return InputConstants.isKeyDown(handle, key_code);
    }

    public static void addItemIdToTooltip(ItemStack stack, Consumer<Component> tooltip, TooltipFlag type) {
        if (type.isAdvanced()) {
            tooltip.accept(Component.literal(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
            if (StrangeItems.componentless_installed) {
                tooltip.accept(Component.literal("stop it, componentless"));
            }
        }
    }

    public static boolean canSwap(ItemStack stack, ItemStack stack_wearing, Player player) {
        return (
        !EnchantmentHelper.has(stack_wearing, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)
            || player.isCreative()
        ) && !ItemStack.isSameItemSameComponents(stack, stack_wearing);
    }

    /**
     * A method to check if Tooltip Scroll is installed, respecting the users settings on how it should handle this check.
     */
    public static boolean isTooltipScrollInstalled() {
        boolean result = false;
        if (StrangeConfig.check_for_tooltipscroll) {
            result = StrangeItems.tooltipscroll_installed;
        }
        if (StrangeConfig.invert_tooltipscroll_check_value) {
            return !result;
        }
        return result;
    }

    public static List<Tracker> getListOfTrackers() {
        return StrangeRegistries.TRACKER.stream().toList();
    }

    @Deprecated
    public static List<Identifier> get_list_of_ids() {
        return StrangeRegistries.TRACKER.keySet().stream().toList();
    }

    public static void addAllTrackerTooltips(Item.TooltipContext context, Consumer<Component> textConsumer, ItemStack stack) {
        textConsumer.accept(Component.translatable("tooltip.strangeitems.strange_trackers").append(":").withStyle(ChatFormatting.GRAY));
        HolderSet<Tracker> entryList = getTooltipOrder(context.registries(), StrangeRegistryKeys.TRACKER, TrackerTags.TOOLTIP_ORDER);
        for (Holder<Tracker> registryEntry : entryList) {
            if (StrangeConfig.HIDDEN_TRACKERS.shouldShowForItem(BuiltInRegistries.ITEM.wrapAsHolder(stack.getItem()), registryEntry)) {
                registryEntry.value().appendTooltip(stack, textConsumer);
            }
        }

        for (Tracker tracker : getListOfTrackers()) {
            if (!entryList.contains(StrangeRegistries.TRACKER.wrapAsHolder(tracker))) {
                if (StrangeConfig.HIDDEN_TRACKERS.shouldShowForItem(stack, tracker)) {
                    tracker.appendTooltip(stack, textConsumer);
                }
            }
        }
    }

    public static HolderSet<Tracker> getTooltipOrder(@Nullable HolderLookup.Provider registries, ResourceKey<Registry<Tracker>> key, TagKey<Tracker> tag) {
        if (registries != null) {
            Optional<HolderSet.Named<Tracker>> optional = registries.lookupOrThrow(key).get(tag);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return HolderSet.direct();
    }

    public static boolean isCollectors(ItemStack itemStack) {
        if (itemStack.has(StrangeItemsComponents.COLLECTORS_ITEM)) return true;
        if (!itemStack.has(DataComponents.CUSTOM_DATA)) return false;
        CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBooleanOr(COLLECTORS_ITEM_TAG, false);
    }

    public static boolean hasAllTrackers(ItemStack itemStack) {
        if (itemStack.has(StrangeItemsComponents.HAS_ALL_TRACKERS)) return true;
        if (!itemStack.has(DataComponents.CUSTOM_DATA)) return false;
        CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
        return data != null && data.copyTag().getBooleanOr(HAS_ALL_TRACKERS_TAG, false);
    }
}
