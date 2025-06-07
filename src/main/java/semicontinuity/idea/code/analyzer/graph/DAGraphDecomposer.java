package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

public class DAGraphDecomposer<V> {

    private final DAGraph<V> graph;
    private final Supplier<DAGraph<V>> subGraphFactory = DAGraphImpl::new;

    public DAGraphDecomposer(DAGraph<V> graph) {
        this.graph = graph;
    }

    public Map<Set<V>, DAGraph<V>> decompose() {
        var roots = graph.rootList();
        System.out.println("| decompose: " + roots.size() + " roots = " + roots);
        return decomposeFrom(roots);
    }

    public Map<Set<V>, DAGraph<V>> decomposeFrom(List<V> roots) {
        var colors = paintWithRootColors(roots);

        // Traverse the graph, detecting mis-colorings:
        // If the color of a vertex differs form the color of its root, then it has been re-painted from another root.
        // It means, that sub-graphs, starting at these roots, are connected.
        // Will track this connectedness in the 'parent' hash map.
        var parents = computeParents(roots, colors);

        var realParents = unionSetFind(roots, parents);

        System.out.println("| decomposeFrom: realParents");
        dumpMap(realParents);

        var rootGroups = invertToSets(realParents);

        return subGraphsForRoots(rootGroups);
    }

    private static <V> Map<V, Set<V>> invertToSets(HashMap<V, V> realParents) {
        return realParents
                .entrySet()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Map.Entry::getValue,
                                Collectors.mapping(Map.Entry::getKey, Collectors.toSet())
                        )
                );
    }

    private HashMap<Set<V>, DAGraph<V>> subGraphsForRoots(Map<V, Set<V>> rootGroups) {
        System.out.println("| decomposeFrom " + rootGroups.size() + " rootGroups");
        var result = new HashMap<Set<V>, DAGraph<V>>();
        for (Set<V> rootGroup : rootGroups.values()) {
            System.out.println("| decomposeFrom: rootGroup=" + rootGroup);

            var subGraph = subGraphFactory.get();
            for (V root : rootGroup) {
                for (V nextVertex : graph.followers(root)) {
                    fillSubGraphFrom(nextVertex, subGraph, new HashSet<>());
                }
            }
            result.put(rootGroup, subGraph);
        }

        return result;
    }

    private static <V> void dumpMap(HashMap<V, V> realParents) {
        realParents.forEach((key, value) -> System.out.println("  key=" + key + "\tvalue=" + value));
    }

    private static <V> @NotNull HashMap<V, V> unionSetFind(List<V> roots, HashMap<V, V> parents) {
        // "Union set find"
        var realParents = new HashMap<V, V>();
        roots.forEach(v -> {
            var curN = v;
            while (true) {
                var parent = parents.get(curN);
                if (parent == curN) break;
                curN = parent;
            }
            realParents.put(v, curN);
        });
        return realParents;
    }

    private @NotNull HashMap<V, V> computeParents(List<V> roots, HashMap<V, V> colors) {
        var parents = new HashMap<V, V>();
        for (V root : roots) {
            parents.put(root, root);
        }
        for (V root : roots) {
            checkColorFrom(root, root, colors, parents);
        }

        System.out.println("| decomposeFrom: parents");
        dumpMap(parents);
        System.out.println("| decomposeFrom...");
        return parents;
    }

    private @NotNull HashMap<V, V> paintWithRootColors(List<V> roots) {
        // Paint every vertex with the 'color' of its root.
        var colors = new HashMap<V, V>();
        for (V root : roots) {
            paintFrom(root, root, colors);
        }
        System.out.println("| decomposeFrom: " + roots.size() + " roots: colors=" + colors);
        return colors;
    }

    private void paintFrom(V vertex, V color, HashMap<V, V> colors) {
        for (V follower : graph.followers(vertex)) {
            if (colors.putIfAbsent(follower, color) == null) {
                paintFrom(follower, color, colors);
            }
        }
    }

    private void checkColorFrom(V vertex, V color, HashMap<V, V> colors, HashMap<V, V> parents) {
        doCheckColorFrom(vertex, color, colors, parents, new HashSet<>());
    }

    private void doCheckColorFrom(V vertex, V color, HashMap<V, V> colors, HashMap<V, V> parents, Set<V> visited) {
        if (visited.contains(vertex)) return;
        visited.add(vertex);

        for (V follower : graph.followers(vertex)) {
            var aColor = colors.get(follower);
            System.out.println("  | " + vertex + "[color:" + color + "] ->" + follower + "[color: " + aColor + "]");
            if (!color.equals(aColor)) {
                parents.put(color, aColor);
            }
            doCheckColorFrom(follower, color, colors, parents, visited);
        }
    }

    void fillSubGraphFrom(V vertex, DAGraph<V> sink, Set<V> visited) {
        if (visited.contains(vertex)) return;
        visited.add(vertex);

        sink.addVertex(vertex);
        for (V follower : graph.followers(vertex)) {
            sink.addEdge(vertex, follower);
            fillSubGraphFrom(follower, sink, visited);
        }
    }
}
