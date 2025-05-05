package semicontinuity.idea.code.analyzer.golang

import semicontinuity.idea.code.analyzer.graph.DAGraph
import java.util.function.Supplier

/**
 * Splits call graph into several sub-graphs, one per struct.
 */
object CallGraphSplitter {

    fun split(graph: DAGraph<Member>, subGraphFactory: Supplier<DAGraph<Member>>): Map<String, DAGraph<Member>> {
        val subGraphs = HashMap<String, DAGraph<Member>>()

        graph.forEachVertex { m: Member ->
            subGraphs.computeIfAbsent(group(m)) { k: String? -> subGraphFactory.get() }.addVertex(m)
        }

        graph.forEachEdge { m1: Member, m2: Member ->
            if (inSameSubgraph(m1, m2)) {
                subGraphs[group(m1)]?.addEdge(m1, m2)
            }
        }

        return subGraphs
    }

    private fun inSameSubgraph(n1: Member, n2: Member) =
        n1.qualifier.isNotEmpty()
            && n2.qualifier.isNotEmpty()
            && n1.qualifier == n2.qualifier

    private fun group(m: Member): String = m.qualifier
}
