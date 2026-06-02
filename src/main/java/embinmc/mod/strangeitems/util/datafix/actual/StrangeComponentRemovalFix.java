package embinmc.mod.strangeitems.util.datafix.actual;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import embinmc.mod.strangeitems.util.StrangeUtil;
import net.minecraft.util.datafix.fixes.References;

public class StrangeComponentRemovalFix extends DataFix {
    public StrangeComponentRemovalFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.writeFixAndRead(
                "Remove Strange Item components",
                this.getInputSchema().getType(References.DATA_COMPONENTS),
                this.getOutputSchema().getType(References.DATA_COMPONENTS),
                components -> {
                    boolean isCollectors = components.get("strangeitems:collectors_item").result().isPresent();
                    boolean isTrackAll = components.get("strangeitems:has_all_trackers").result().isPresent();
                    if (!isCollectors && !isTrackAll)
                        return components;
                    var customData = components.get("minecraft:custom_data").orElseEmptyMap();
                    if (isCollectors) {
                        customData = customData.set(StrangeUtil.COLLECTORS_ITEM_TAG, customData.createBoolean(true));
                        components = components.remove("strangeitems:collectors_item");
                    }
                    if (isTrackAll) {
                        customData = customData.set(StrangeUtil.HAS_ALL_TRACKERS_TAG, customData.createBoolean(true));
                        components = components.remove("strangeitems:has_all_trackers");
                    }
                    return components.set("minecraft:custom_data", customData);
                }
        );
    }
}
