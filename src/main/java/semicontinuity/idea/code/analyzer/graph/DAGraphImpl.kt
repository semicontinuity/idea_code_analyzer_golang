package semicontinuity.idea.code.analyzer.graph

import java.util.function.BiConsumer
import java.util.function.Consumer

class DAGraphImpl<N> : DAGraph<N> {
    private var hasEdges: Boolean = false

    private val vertices = linkedSetOf<N>()

    private val fwdEdges = linkedMapOf<N, MutableSet<N>>()
    private val revEdges = linkedMapOf<N, MutableSet<N>>()

    override fun clear() {
        hasEdges = false
        vertices.clear()
        fwdEdges.clear()
        revEdges.clear()
    }

    override fun addVertex(vertex: N) {
        fwdEdges.computeIfAbsent(vertex) { linkedSetOf() }
        revEdges.computeIfAbsent(vertex) { linkedSetOf() }
        vertices.add(vertex)
    }

    override fun size() = vertices.size

    override fun hasVertices() = vertices.isNotEmpty()

    override fun vertices() = vertices

    override fun containsVertex(vertex: N) = vertices.contains(vertex)

    override fun forEachVertex(consumer: Consumer<N>) {
        vertices.forEach(consumer)
    }

    override fun addEdge(src: N, dst: N) {
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

    override fun forEachEdge(consumer: BiConsumer<N, N>) {
        for ((key, value) in fwdEdges) {
            for (n in value) {
                consumer.accept(key, n)
            }
        }
    }

    override fun incomingEdgeCount(vertex: N) = revEdges[vertex]?.size ?: 0

    override fun forEachPredecessor(vertex: N, consumer: Consumer<N>) {
        revEdges[vertex]!!.forEach(consumer)
    }

    override fun forEachFollower(vertex: N, consumer: Consumer<N>) {
        fwdEdges[vertex]!!.forEach(consumer)
    }

    override fun rootList(): List<N> =
        revEdges.entries
            .filter { it.value.isEmpty() }
            .map { it.key }

    override fun nonRootList(): List<N> =
        revEdges.entries
            .filter { it.value.isNotEmpty() }
            .map { it.key }

    override fun followers(vertex: N) =
        fwdEdges[vertex] ?: setOf()

    override fun followersOf(vertices: Collection<N>): Set<N> {
        val result = linkedSetOf<N>()
        vertices.forEach { node ->
            forEachFollower(node) {
                result.add(it)
            }
        }
        return result
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
