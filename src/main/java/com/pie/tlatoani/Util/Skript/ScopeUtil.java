package com.pie.tlatoani.Util.Skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.TriggerSection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pie.tlatoani.Core.Skript.CustomScope;

/**
 * Created by Tlatoani on 5/4/17.
 */
public class ScopeUtil {

    public static Optional<TriggerItem> loadSectionNode(SectionNode sectionNode, TriggerSection parent) {
        if (parent != null) {
        	CustomScope.getCurrentSections().add(parent);
        }
        List<TriggerItem> triggerItems = ScriptLoader.loadItems(sectionNode);
        if (parent != null && !triggerItems.isEmpty()) {
            triggerItems.get(triggerItems.size() - 1).setNext(parent.getNext());
            for (TriggerItem triggerItem : triggerItems) {
                triggerItem.setParent(parent);
            }
            CustomScope.getCurrentSections().remove(parent);
        }
        return triggerItems.isEmpty() ? Optional.empty() : Optional.of(triggerItems.get(0));
    }

    public static TriggerItem loadSectionNodeOrDummy(SectionNode sectionNode, TriggerSection parent) {
        return loadSectionNode(sectionNode, parent).orElse(new DummyTriggerItem());
    }

    public static void removeSubNodes(SectionNode sectionNode) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : sectionNode) {
            nodes.add(node);
        }
        nodes.forEach(Node::remove);
    }
    
}
