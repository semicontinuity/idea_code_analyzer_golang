package semicontinuity.idea.code.analyzer.golang;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Structure {
    private final Map<String, Set<Node>> structMethods = new HashMap<>();

    final Map<Node, Set<Node>> calls = new HashMap<>();

    public void add(Node node) {
        System.out.println("Adding " + node);
        structMethods
                .computeIfAbsent(node.getQualifier(), (k) -> new HashSet<>())
                .add(node);
    }

    public void addCall(Node from, Node to) {
        if (contains(from) && contains(to)) {
            System.out.println("Adding call " + from + "->" + to);
            calls.computeIfAbsent(from, (k) -> new HashSet<>()).add(to);
        } else {
            System.out.println("Out of scope: " + from + " or " + to);
        }
    }

    public void forEachNode(Consumer<QualifiedName> c) {
        structMethods.forEach(
                (String struct, Set<Node> methods) -> methods.forEach(method -> c.accept(new QualifiedName(struct,
                        method.getName())))
        );
    }

    public void forEachCall(BiConsumer<QualifiedName, QualifiedName> c) {
        calls.forEach((from, toSet) -> toSet.forEach(to -> c.accept(from.toQualifiedName(), to.toQualifiedName())));
    }

    boolean contains(Node node) {
        return structMethods.containsKey(node.getQualifier()) && structMethods.get(node.getQualifier()).contains(node);
    }
}
