package embinmc.mod.strangeitems.util;

import embinmc.mod.strangeitems.StrangeItems;
import net.minecraft.resources.Identifier;

public class Id {
    @Deprecated
    public Identifier convertNamespace(String namespace) {
        String[] splitted = namespace.split(":");
        if (splitted.length == 1) {
            return Identifier.fromNamespaceAndPath(StrangeItems.MOD_ID, namespace);
        }
        return Identifier.fromNamespaceAndPath(splitted[0],splitted[1]);
    }

    public static Identifier of(String namespace) {
        String[] splitted = namespace.split(":");
        if (splitted.length == 1) {
            return Identifier.fromNamespaceAndPath(StrangeItems.MOD_ID, namespace);
        }
        return Identifier.fromNamespaceAndPath(splitted[0],splitted[1]);
    }
}
