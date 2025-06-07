package semicontinuity.idea.code.analyzer.graph

import java.util.function.BiConsumer
import java.util.function.Consumer

class DAGraphImpl<V> : DAGraph<V> {
    private var hasEdges: Boolean = false

    private val vertices = linkedSetOf<V>()

    private val fwdEdges = linkedMapOf<V, MutableSet<V>>()
    private val revEdges = linkedMapOf<V, MutableSet<V>>()


    override fun clear() {
        hasEdges = false
        vertices.clear()
        fwdEdges.clear()
        revEdges.clear()
    }

    override fun addVertex(vertex: V) {
        fwdEdges.computeIfAbsent(vertex) { linkedSetOf() }
        revEdges.computeIfAbsent(vertex) { linkedSetOf() }
        vertices.add(vertex)
    }

    override fun vertexCount() = vertices.size

    override fun hasVertices() = vertices.isNotEmpty()

    override fun vertices() = vertices

    override fun containsVertex(vertex: V) = vertices.contains(vertex)

    override fun forEachVertex(consumer: Consumer<V>) {
        vertices.forEach(consumer)
    }


    override fun rootList(): List<V> =
        revEdges.entries
            .filter { it.value.isEmpty() }
            .map { it.key }

    override fun nonRootList(): List<V> =
        revEdges.entries
            .filter { it.value.isNotEmpty() }
            .map { it.key }


    override fun addEdge(src: V, dst: V) {
        if (src == dst) {
            // not supported
        } else {
            addVertex(src)
            addVertex(dst)
            fwdEdges.computeIfAbsent(src) { linkedSetOf() }.add(dst)
            revEdges.computeIfAbsent(dst) { linkedSetOf() }.add(src)
            hasEdges = true
        }
    }

    override fun hasEdges() = hasEdges

    override fun forEachEdge(consumer: BiConsumer<V, V>) {
        for ((key, value) in fwdEdges) {
            for (n in value) {
                consumer.accept(key, n)
            }
        }
    }

    override fun followers(vertex: V) =
        fwdEdges[vertex] ?: setOf()

    override fun followersOf(vertices: Collection<V>): Set<V> {
        val result = linkedSetOf<V>()
        vertices.forEach { node ->
            forEachFollower(node) {
                result.add(it)
            }
        }
        return result
    }

    override fun forEachFollower(vertex: V, consumer: Consumer<V>) {
        fwdEdges[vertex]!!.forEach(consumer)
    }


    override fun predecessors(vertex: V) =
        revEdges[vertex] ?: setOf()

    override fun predecessorCount(vertex: V) = revEdges[vertex]?.size ?: 0

    override fun forEachPredecessor(vertex: V, consumer: Consumer<V>) {
        revEdges[vertex]!!.forEach(consumer)
    }


    override fun toString() =
        fwdEdges.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val graph = other as DAGraphImpl<*>

        if (fwdEdges != graph.fwdEdges) {
            return false
        }
        return revEdges == graph.revEdges
    }

    override fun hashCode(): Int {
        var result = fwdEdges.hashCode()
        result = 31 * result + revEdges.hashCode()
        return result
    }
}
