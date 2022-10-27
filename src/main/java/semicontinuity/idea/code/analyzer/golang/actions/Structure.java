package semicontinuity.idea.code.analyzer.golang.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Structure {
    final Map<String, String> structMethods = new HashMap<>();
    final Set<String> functions = new HashSet<>();

    final Map<QualifiedName, QualifiedName> calls = new HashMap<>();
}
