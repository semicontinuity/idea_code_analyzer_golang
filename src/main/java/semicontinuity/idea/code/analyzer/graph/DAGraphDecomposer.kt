package semicontinuity.idea.code.analyzer.graph

import semicontinuity.idea.code.analyzer.util.CloseableConsumer
import java.util.function.Consumer
import java.util.function.Supplier

class DAGraphDecomposer<V>(
    private val graph: DAGraph<V>,
    private val log: CloseableConsumer<String> = CloseableConsumer.noOp(),
) {
    private val subGraphFactory = Supplier<DAGraph<V>> { DAGraphImpl() }

    fun decompose(): Map<Set<V>, DAGraph<V>> {
        val roots = graph.rootList()
        println("| decompose: " + roots.size + " roots = " + roots)
        return decomposeFrom(roots)
    }

    fun decomposeFrom(roots: List<V>): Map<Set<V>, DAGraph<V>> {
        val colors = paintWithRootColors(roots)

        // Traverse the graph, detecting mis-colorings:
        // If the color of a vertex differs form the color of its root, then it has been re-painted from another root.
        // It means, that sub-graphs, starting at these roots, are connected.
        // Will track this connectedness in the 'parent' hash map.
        val parents = computeParents(roots, colors)

        val realParents = unionSetFind(roots, parents)
        dumpMap(realParents)

        val rootGroups = invertToSets(realParents)

        return subGraphsForRoots(rootGroups)
    }

    private fun subGraphsForRoots(rootGroups: Map<V, Set<V>>): HashMap<Set<V>, DAGraph<V>> {
        println("| decomposeFrom " + rootGroups.size + " rootGroups")
        val result = HashMap<Set<V>, DAGraph<V>>()
        for (rootGroup in rootGroups.values) {
            println("| decomposeFrom: rootGroup=$rootGroup")

            val subGraph = subGraphFactory.get()
            for (root in rootGroup) {
                for (nextVertex in graph.followers(root)) {
                    fillSubGraphFrom(nextVertex, subGraph, HashSet())
                }
            }
            result[rootGroup] = subGraph
        }

        return result
    }

    private fun computeParents(roots: List<V>, colors: HashMap<V, V>): HashMap<V, V> {
        val parents = HashMap<V, V>()
        for (root in roots) {
            parents[root] = root
        }
        for (root in roots) {
            checkColorFrom(root, root, colors, parents)
        }

        println("| decomposeFrom: parents")
        dumpMap(parents)
        println("| decomposeFrom...")
        return parents
    }

    private fun paintWithRootColors(roots: List<V>): HashMap<V, V> {
        // Paint every vertex with the 'color' of its root.
        val colors = HashMap<V, V>()
        for (root in roots) {
            paintFrom(root, root, colors)
        }
        println("| decomposeFrom: " + roots.size + " roots: colors=" + colors)
        return colors
    }

    private fun paintFrom(vertex: V, color: V, colors: HashMap<V, V>) {
        for (follower in graph.followers(vertex)) {
            if (colors.putIfAbsent(follower, color) == null) {
                paintFrom(follower, color, colors)
            }
        }
    }

    private fun checkColorFrom(vertex: V, color: V, colors: HashMap<V, V>, parents: HashMap<V, V>) {
        doCheckColorFrom(vertex, color, colors, parents, HashSet())
    }

    private fun doCheckColorFrom(
        vertex: V,
        color: V,
        colors: Map<V, V>,
        parents: MutableMap<V, V>,
        visited: MutableSet<V>
    ) {
        if (visited.contains(vertex)) return
        visited.add(vertex)

        for (follower in graph.followers(vertex)) {
            val aColor: V? = colors[follower]
            println("  | $vertex[color:$color] ->$follower[color: $aColor]")
            if (color != aColor && aColor != null) {
                parents[color] = aColor
            }
            doCheckColorFrom(follower, color, colors, parents, visited)
        }
    }

    fun fillSubGraphFrom(vertex: V, sink: DAGraph<V>, visited: MutableSet<V>) {
        if (visited.contains(vertex)) return
        visited.add(vertex)

        sink.addVertex(vertex)
        for (follower in graph.followers(vertex)) {
            sink.addEdge(vertex, follower)
            fillSubGraphFrom(follower, sink, visited)
        }
    }

    private fun unionSetFind(roots: List<V>, parents: Map<V, V>): Map<V, V> {
        val realParents = linkedMapOf<V, V>()
        roots.forEach(Consumer { n ->
            var curN = n
            while (true) {
                val parent = parents[curN]
                if (parent === curN) break
                curN = parent
            }
            realParents[n] = curN
        })
        return realParents
    }

    private fun <K, V> dumpMap(map: Map<K, V>) {
        map.forEach { (key, value) -> log.accept("  key=$key\tvalue=$value") }
    }
}
