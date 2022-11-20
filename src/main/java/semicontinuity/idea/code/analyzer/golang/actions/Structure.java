package semicontinuity.idea.code.analyzer.golang.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Structure {
    final Map<String, Set<String>> structMethods = new HashMap<>();
    final Set<String> functionNames = new HashSet<>();

    final Map<QualifiedName, Set<QualifiedName>> calls = new HashMap<>();

    public void addCall(QualifiedName from, QualifiedName to) {
        if (contains(from) && contains(to)) {
            System.out.println("Adding call " + from + "->" + to);
            calls.computeIfAbsent(from, (k) -> new HashSet<>()).add(to);
        } else {
            System.out.println("Out of scope: " + from + " or " + to);
        }
    }

    boolean contains(QualifiedName qName) {
        if (qName.getQualifier().isEmpty()) {
            return functionNames.contains(qName.getName());
        } else {
            return structMethods.containsKey(qName.getQualifier()) && structMethods.get(qName.getQualifier()).contains(qName.getName());
        }
    }
}
