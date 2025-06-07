package semicontinuity.idea.code.analyzer.graph

import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Direct acyclic graph abstraction.
 * @param <V> The type of the vertex in the graph. Must be hashable.
 *            It can be just the ID of the vertex, with the node payload kept separately.
</V> */
interface DAGraph<V> {
    /**
     * Obtains the number of vertices in the graph
     */
    fun size(): Int

    /**
     * Clears the graph (removes all vertices and edges)
     */
    fun clear()

    fun addVertex(vertex: V)

    fun hasVertices(): Boolean

    fun vertices(): Set<V>

    fun containsVertex(vertex: V): Boolean

    fun forEachVertex(consumer: Consumer<V>)


    /**
     * Obtains the "roots" of the graph (vertices, that do not have inbound edges)
     */
    fun rootList(): List<V>

    fun nonRootList(): List<V>


    fun addEdge(src: V, dst: V)

    fun hasEdges(): Boolean

    fun forEachEdge(consumer: BiConsumer<V, V>)


    /**
     * Obtains "followers" of the given vertex (vertices, to which edges from this vertex point)
     */
    fun followers(vertex: V): Set<V>

    fun followersOf(vertices: Collection<V>): Set<V>

    fun forEachFollower(vertex: V, consumer: Consumer<V>)


    /**
     * Obtains "predecessors" of the given vertex (vertices, that point to this vertex)
     */
    fun predecessors(vertex: V): Set<V>

    fun predecessorCount(vertex: V): Int

    fun forEachPredecessor(vertex: V, consumer: Consumer<V>)
}
