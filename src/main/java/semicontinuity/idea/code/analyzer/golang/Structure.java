package semicontinuity.idea.code.analyzer.golang;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Structure {
    private final Map<String, Set<String>> structMethods = new HashMap<>();

    final Map<QualifiedName, Set<QualifiedName>> calls = new HashMap<>();

    public void add(QualifiedName node) {
        System.out.println("Adding " + node);
        structMethods
                .computeIfAbsent(node.getQualifier(), (k) -> new HashSet<>())
                .add(node.getName());
    }

    public void addCall(QualifiedName from, QualifiedName to) {
        if (contains(from) && contains(to)) {
            System.out.println("Adding call " + from + "->" + to);
            calls.computeIfAbsent(from, (k) -> new HashSet<>()).add(to);
        } else {
            System.out.println("Out of scope: " + from + " or " + to);
        }
    }

    public void forEachNode(Consumer<QualifiedName> c) {
        structMethods.forEach((String struct, Set<String> methods) -> methods.forEach(method -> c.accept(new QualifiedName(struct, method))));
    }

    public void forEachCall(BiConsumer<QualifiedName, QualifiedName> c) {
        calls.forEach((from, toSet) -> toSet.forEach(to -> c.accept(from, to)));
    }

    boolean contains(QualifiedName qName) {
        return structMethods.containsKey(qName.getQualifier()) && structMethods.get(qName.getQualifier()).contains(qName.getName());
    }

    public Set<String> structNames() {
        return structMethods.keySet();
    }
}
