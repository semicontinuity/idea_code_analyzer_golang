package semicontinuity.idea.code.analyzer.golang.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class Structure {
    private final Map<String, Set<String>> structMethods = new HashMap<>();
    private final Set<String> functionNames = new HashSet<>();

    final Map<QualifiedName, Set<QualifiedName>> calls = new HashMap<>();

    void addFunction(String name) {
        System.out.println("Adding function = " + name);
//        functionNames.add(name);
        structMethods
                .computeIfAbsent("", (k) -> new HashSet<>())
                .add(name);
    }

    void addMethod(String structName, String name) {
        System.out.println("Adding method = " + structName + "." + name);
        structMethods
                .computeIfAbsent(structName, (k) -> new HashSet<>())
                .add(name);
    }

    public void addCall(QualifiedName from, QualifiedName to) {
        if (contains(from) && contains(to)) {
            System.out.println("Adding call " + from + "->" + to);
            calls.computeIfAbsent(from, (k) -> new HashSet<>()).add(to);
        } else {
            System.out.println("Out of scope: " + from + " or " + to);
        }
    }

    public void forEachCall(BiConsumer<QualifiedName, QualifiedName> c) {
        calls.forEach((from, toSet) -> toSet.forEach(to -> c.accept(from, to)));
    }

    boolean contains(QualifiedName qName) {
        if (qName.getQualifier().isEmpty()) {
            return functionNames.contains(qName.getName());
        } else {
            return structMethods.containsKey(qName.getQualifier()) && structMethods.get(qName.getQualifier()).contains(qName.getName());
        }
    }
}
