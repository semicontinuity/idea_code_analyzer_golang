package semicontinuity.idea.code.analyzer.golang;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Structure {
    private final Consumer<String> log;
    private final Map<String, Set<Node>> structMethods = new HashMap<>();

    final Map<Node, Set<Node>> calls = new HashMap<>();

    public Structure(Consumer<String> log) {
        this.log = log;
    }

    public void addNode(Node node) {
        log.accept("    Adding " + node);
        structMethods
                .computeIfAbsent(node.getQualifier(), (k) -> new HashSet<>())
                .add(node);
    }

    public void addEdge(Node from, Node to) {
        if (containsNode(from) && containsNode(to)) {
            log.accept("      Adding call " + from + "->" + to);
            calls.computeIfAbsent(from, (k) -> new HashSet<>()).add(to);
        } else {
            log.accept("      Out of scope: " + from + " or " + to);
        }
    }

    public void forEachNode(Consumer<Node> c) {
        structMethods.forEach(
                (String struct, Set<Node> methods) -> methods.forEach(c)
        );
    }

    public void forEachEdge(BiConsumer<Node, Node> c) {
        calls.forEach((from, toSet) -> toSet.forEach(to -> c.accept(from, to)));
    }

    boolean containsNode(Node node) {
        return structMethods.containsKey(node.getQualifier()) && structMethods.get(node.getQualifier()).contains(node);
    }
}
