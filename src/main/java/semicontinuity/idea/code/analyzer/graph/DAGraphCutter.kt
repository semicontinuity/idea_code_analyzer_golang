package semicontinuity.idea.code.analyzer.graph

import java.util.function.Supplier

class DAGraphCutter<V>(
    private val graph: DAGraph<V>,
) {
    private val subGraphFactory = Supplier<DAGraph<V>> { DAGraphImpl() }

    /**
     * Cuts the graph into independent sub-graphs (so that one sub-graph cannot be reached from another).
     *
     * This implementation identifies strongly connected components and treats each as a separate subgraph.
     */
    fun cut(): List<DAGraph<V>> {
        // If the graph is empty, return an empty list
        if (!graph.hasVertices()) {
            return emptyList()
        }

        val visited = mutableSetOf<V>()
        val subGraphs = mutableListOf<DAGraph<V>>()

        // Process all vertices to find connected components
        for (vertex in graph.vertices()) {
            if (vertex in visited) continue

            val subGraph = newSubGraph()
            val subGraphVertices = mutableSetOf<V>()

            // Find all vertices connected to this vertex (in both directions)
            findConnectedComponent(vertex, subGraphVertices, visited)

            // Add all vertices to the subgraph
            for (v in subGraphVertices) {
                subGraph.addVertex(v)
            }

            // Add edges between vertices in this subgraph
            for (v in subGraphVertices) {
                for (follower in graph.followers(v)) {
                    if (follower in subGraphVertices) {
                        subGraph.addEdge(v, follower)
                    }
                }
            }

            subGraphs.add(subGraph)
        }

        return subGraphs
    }

    /**
     * Finds all vertices that are part of the same connected component as the starting vertex.
     * This includes vertices connected by both incoming and outgoing edges.
     */
    private fun findConnectedComponent(vertex: V, componentVertices: MutableSet<V>, visited: MutableSet<V>) {
        if (vertex in visited) return

        visited.add(vertex)
        componentVertices.add(vertex)

        // Explore all outgoing edges (followers)
        for (follower in graph.followers(vertex)) {
            findConnectedComponent(follower, componentVertices, visited)
        }

        // Explore all incoming edges (predecessors)
        for (predecessor in graph.predecessors(vertex)) {
            findConnectedComponent(predecessor, componentVertices, visited)
        }
    }

    private fun newSubGraph(): DAGraph<V> = subGraphFactory.get()
}
