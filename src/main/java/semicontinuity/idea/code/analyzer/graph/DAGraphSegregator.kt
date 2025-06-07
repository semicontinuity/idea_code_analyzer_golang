package semicontinuity.idea.code.analyzer.graph

class DAGraphSegregator<V>(
    private val graph: DAGraph<V>,
) {

    /**
     * Segregate the given graph into two sub-graphs:
     * "front", that contains all "roots" of the graph,
     * and "simple" vertices, reachable from roots.
     * "Simple" vertices are vertices that have just one incoming edge.
     * "Complex" vertices, and vertices, reachable via their edges, go to the "back" graph.
     */
    fun segregate(): Pair<DAGraph<V>, DAGraph<V>> {
        val front = DAGraphImpl<V>()
        val back = DAGraphImpl<V>()

        // First identify all complex vertices (with more than one incoming edge)
        val complexVertices = mutableSetOf<V>()
        for (vertex in graph.vertices()) {
            if (graph.predecessorCount(vertex) > 1) {
                complexVertices.add(vertex)
            }
        }

        // Find all vertices reachable from complex vertices (including the complex vertices themselves)
        val backVertices = mutableSetOf<V>()
        val toVisit = ArrayDeque<V>()
        toVisit.addAll(complexVertices)

        while (toVisit.isNotEmpty()) {
            val vertex = toVisit.removeFirst()
            if (backVertices.add(vertex)) {
                // If vertex was not already in backVertices
                graph.forEachFollower(vertex) { follower ->
                    toVisit.add(follower)
                }
            }
        }

        // Add vertices to appropriate subgraphs
        for (vertex in graph.vertices()) {
            if (backVertices.contains(vertex)) {
                back.addVertex(vertex)
            } else {
                front.addVertex(vertex)
            }
        }

        // Add edges within front graph
        for (vertex in front.vertices()) {
            graph.forEachFollower(vertex) { follower ->
                if (front.containsVertex(follower)) {
                    front.addEdge(vertex, follower)
                }
            }
        }

        // Add edges within back graph
        for (vertex in back.vertices()) {
            graph.forEachFollower(vertex) { follower ->
                if (back.containsVertex(follower)) {
                    back.addEdge(vertex, follower)
                }
            }
        }

        return Pair(front, back)
    }
}
