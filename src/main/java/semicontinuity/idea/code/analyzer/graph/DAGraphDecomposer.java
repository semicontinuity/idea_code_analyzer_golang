package semicontinuity.idea.code.analyzer.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DAGraphDecomposer<N> {

    private final DAGraph<N> graph;
    private final Supplier<DAGraph<N>> subGraphFactory = DAGraphImpl::new;

    public DAGraphDecomposer(DAGraph<N> graph) {
        this.graph = graph;
    }

    public Map<Set<N>, DAGraph<N>> decompose() {
        var roots = graph.findRoots();
        System.out.println("| decompose: " + roots.size() + " roots = " + roots);
        return decomposeFrom(roots);
    }

    public Map<Set<N>, DAGraph<N>> decomposeFrom(List<N> roots) {
        // Paint every node with the 'color' of its root.
        var colors = new HashMap<N, N>();
        for (N root : roots) {
            paintFrom(root, root, colors);
        }
        System.out.println("| decomposeFrom: " + roots.size() + " roots: colors=" + colors);

        // Traverse the graph, detecting mis-colorings:
        // If the color of a node differs form the color of its root, then it has been re-painted from another root.
        // It means, that sub-graphs, starting at these roots, are connected.
        // Will track this connectedness in the 'parent' hash map.
        var parents = new HashMap<N, N>();
        for (N root : roots) {
            parents.put(root, root);
        }
        for (N root : roots) {
            checkColorFrom(root, root, colors, parents);
        }

        System.out.println("| decomposeFrom: parents");
        parents.forEach((key, value) -> System.out.println("  key=" + key + "\tvalue=" + value));
        System.out.println("| decomposeFrom...");

        // "Union set find"
        var realParents = new HashMap<N, N>();
        roots.forEach(n -> {
            var curN = n;
            while (true) {
                var parent = parents.get(curN);
                if (parent == curN) break;
                curN = parent;
            }
            realParents.put(n, curN);
        });

        System.out.println("| decomposeFrom: realParents");
        realParents.forEach((key, value) -> System.out.println("  key=" + key + "\tvalue=" + value));
        System.out.println("| decomposeFrom...");

        // Invert 'parents' to produce 'rootGroups'
        var rootGroups = realParents.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toSet())));

        // Create sub-graphs for each root group
        System.out.println("| decomposeFrom " + rootGroups.size() + " rootGroups");
        var result = new HashMap<Set<N>, DAGraph<N>>();
        for (Set<N> rootGroup : rootGroups.values()) {
            System.out.println("| decomposeFrom: rootGroup=" + rootGroup);

            var subGraph = subGraphFactory.get();
            for (N root : rootGroup) {
                for (N nextNode : graph.followers(root)) {
                    fillSubGraphFrom(nextNode, subGraph);
                }
            }
            result.put(rootGroup, subGraph);
        }

        return result;
    }

    private void paintFrom(N node, N color, HashMap<N, N> colors) {
        for (N nextNode : graph.followers(node)) {
            colors.put(nextNode, color);
            paintFrom(nextNode, color, colors);
        }
    }

    private void checkColorFrom(N node, N color, HashMap<N, N> colors, HashMap<N, N> parents) {
        for (N nextNode : graph.followers(node)) {
            var aColor = colors.get(nextNode);
            System.out.println("  | " + node + "[color:" + color + "] ->" + nextNode + "[color: " + aColor + "]");
            if (!color.equals(aColor)) {
                parents.put(color, aColor);
//                parents.put(aColor, color);
            }
            checkColorFrom(nextNode, color, colors, parents);
        }
    }

    void fillSubGraphFrom(N node, DAGraph<N> sink) {
        sink.addNode(node);
        for (N nextNode : graph.followers(node)) {
            sink.addEdge(node, nextNode);
            fillSubGraphFrom(nextNode, sink);
        }
    }
}
