package semicontinuity.idea.code.analyzer.graph

import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * Direct acyclic graph abstraction.
 * @param <V> the type of the vertex in the graph, must be hashable
 * it can be just the ID of the vertex, with the node payload kept separately.
</V> */
interface DAGraph<V> {
    fun clear()

    fun addVertex(vertex: V)

    fun hasVertices(): Boolean

    fun vertices(): Set<V>

    fun containsVertex(vertex: V): Boolean

    fun forEachVertex(consumer: Consumer<V>)

    fun addEdge(src: V, dst: V)

    fun hasEdges(): Boolean

    fun incomingEdgeCount(vertex: V): Int

    fun forEachEdge(consumer: BiConsumer<V, V>)

    fun forEachPredecessor(vertex: V, consumer: Consumer<V>)
    fun forEachFollower(vertex: V, consumer: Consumer<V>)

    fun rootList(): List<V>

    fun followers(vertex: V): Set<V>

    fun size(): Int

    fun nonRootList(): List<V>
    fun followersOf(vertices: Collection<V>): Set<V>
}
