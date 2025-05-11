package semicontinuity.idea.code.analyzer.golang

import semicontinuity.idea.code.analyzer.graph.DAGraph
import semicontinuity.idea.code.analyzer.graph.DAGraphImpl
import java.util.function.Supplier

/**
 * Splits call graph into several sub-graphs, one per struct.
 */
object CallGraphSplitter {
    
    data class Split(
        // replace with DAGraph<Member>
        val coarseGraph: DAGraph<String>,
        val subGraphs: Map<String, DAGraph<Member>>,
        val members: Map<String, Member>,
    )

    fun split(graph: DAGraph<Member>, subGraphFactory: Supplier<DAGraph<Member>>): Split {
        val coarseGraph: DAGraph<String> = DAGraphImpl()
        val subGraphs = HashMap<String, DAGraph<Member>>()
        val members = HashMap<String, Member>()

        graph.forEachVertex { m: Member ->
            members[coarseGraphId(m)] = m
            coarseGraph.addVertex(coarseGraphId(m))
            if (isComplex(m) && m.name != "") {
                subGraphs.computeIfAbsent(coarseGraphId(m)) { subGraphFactory.get() }.addVertex(m)
            }
        }

        graph.forEachEdge { m1: Member, m2: Member ->
            val coarseGraphId1 = coarseGraphId(m1)
            val coarseGraphId2 = coarseGraphId(m2)
            if (coarseGraphId1 != coarseGraphId2) {
                coarseGraph.addEdge(coarseGraphId1, coarseGraphId2)
            }

            if (inSameSubgraph(m1, m2) && m2.name != "") {
                subGraphs[coarseGraphId(m1)]?.addEdge(m1, m2)
            }
        }

        return Split(coarseGraph, subGraphs, members)
    }

    private fun inSameSubgraph(n1: Member, n2: Member) =
        isComplex(n1) && isComplex(n2) && n1.qualifier == n2.qualifier

    private fun coarseGraphId(m: Member): String = if (isComplex(m)) m.qualifier else m.name

    private fun isComplex(m: Member) = m.qualifier.isNotEmpty()
}
