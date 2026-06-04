package embinmc.mod.strangeitems.tracker;

import embinmc.mod.strangeitems.StrangeItems;
import embinmc.mod.strangeitems.StrangeRegistries;
import embinmc.mod.strangeitems.StrangeRegistryKeys;
import embinmc.mod.strangeitems.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class Trigger {
    public static final Trigger NEVER = register("never");
    public static final Trigger BLOCK_MINED = register("block_mined");
    public static final Trigger ITEM_DROPPED = register("item_dropped");
    public static final Trigger TICK_WEAR_ARMOR = register("tick/wear_armor");
    public static final Trigger TICK_UNDERWATER = register("tick/underwater");
    public static final Trigger TICK_IN_LAVA = register("tick/in_lava");
    public static final Trigger TICK_SNEAK = register("tick/sneak");
    public static final Trigger TICK_GLIDING = register("tick/gliding");
    public static final Trigger CREATE_PATH_BLOCK = register("create_path_block");
    public static final Trigger PUT_OUT_CAMPFIRE = register("put_out_campfire");
    public static final Trigger SHEAR_SHEEP = register("shear_sheep");
    public static final Trigger TRIM_PLANT = register("trim_plant");
    public static final Trigger THROW_TRIDENT = register("throw_trident");
    public static final Trigger CARVE_PUMPKIN = register("carve_pumpkin");
    public static final Trigger JUMP = register("jump");
    public static final Trigger TILL_DIRT = register("till_dirt");
    public static final Trigger IGNITE_FIRE = register("ignite_fire");
    public static final Trigger RELIGHT_CAMPFIRE = register("relight_campfire");
    public static final Trigger CAST_FISHING_ROD = register("cast_fishing_rod");
    public static final Trigger REEL_IN_FISHING_ROD = register("reel_in_fishing_rod");
    public static final Trigger CATCH_ITEM_WITH_FISHING_ROD = register("catch_item_with_fishing_rod");
    public static final Trigger CATCH_FISH_WITH_FISHING_ROD = register("catch_fish_with_fishing_rod");
    public static final Trigger EQUIP_ITEM = register("equip_item");
    public static final Trigger SHOOT_ARROW = register("shoot_arrow");
    public static final Trigger ARROW_HIT_MOB = register("arrow_hit_mob");
    //public static final Trigger ARROW_KILL_MOB = register("arrow_kill_mob"); // just use kill_mob
    public static final Trigger ATTEMPT_BRUSH_BLOCK = register("attempt_brush_block");
    public static final Trigger BRUSH_BLOCK_SUCCEEDS = register("brush_block_succeeds");
    public static final Trigger STRIP_BLOCK_WITH_AXE = register("strip_block_with_axe");
    public static final Trigger BRUSH_ARMADILLO = register("brush_armadillo");
    public static final Trigger HIT_MOB = register("hit_mob");
    public static final Trigger KILL_MOB = register("kill_mob");
    public static final Trigger TAKE_DAMAGE = register("take_damage");
    public static final Trigger DAMAGE_BLOCKED_BY_SHIELD = register("damage_blocked_by_shield");
    public static final Trigger DEAL_DAMAGE = register("deal_damage");
    public static final Trigger FALL = register("fall");

    private final Identifier id;

    public Trigger(Identifier id) {
        this.id = id;
    }

    private static Trigger register(final String id) {
        Identifier identifier = Id.of(id);
        return Registry.register(StrangeRegistries.TRIGGER, identifier, new Trigger(identifier));
    }

    public void appendWithData(final RegistryAccess registryAccess, final ItemStack itemStack, final int amount, final @Nullable Identifier data) {
        if (itemStack == null) {
            StrangeItems.LOGGER.warn("Trigger {} attempted to append trackers to null item stack", this.id);
            return;
        }
        if (itemStack.isEmpty())
            return;
        Registry<Tracker> registry = registryAccess.lookupOrThrow(StrangeRegistryKeys.TRACKER_NEW);
        registry.stream().filter(tracker -> tracker.getTrigger() == this).forEach(tracker -> {
            tracker.appendWithData(registryAccess, itemStack, amount, data);
        });
    }

    public void append(final RegistryAccess registryAccess, final ItemStack itemStack) {
        this.appendWithData(registryAccess, itemStack, 1, null);
    }

    /// Appends tracker to stack with an amount of 1, and the supplied data being the ID of the supplied entity's current dimension.
    public void appendWithDimension(final Entity entity, final ItemStack itemStack) {
        this.appendWithData(entity.registryAccess(), itemStack, 1, entity.level().dimension().identifier());
    }

    public void appendWithDimension(final Entity entity, final ItemStack... itemStacks) {
        if (itemStacks.length < 1)
            return;
        RegistryAccess registryAccess = entity.registryAccess();
        Registry<Tracker> registry = registryAccess.lookupOrThrow(StrangeRegistryKeys.TRACKER_NEW);
        registry.stream().filter(tracker -> tracker.getTrigger() == this).forEach(tracker -> {
            for (ItemStack itemStack : itemStacks) {
                if (itemStack.isEmpty())
                    continue;
                tracker.appendWithData(registryAccess, itemStack, 1, entity.level().dimension().identifier());
            }
        });
    }
}
