package semicontinuity.idea.code.analyzer.graph

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DAGraphCutterTest : DAGraphImplTestData1 {

    @Test
    fun testCut() {
        // Get the example graph from the test data
        val graph = exampleGraph1()

        // Create a DAGraphCutter instance
        val cutter = DAGraphCutter(graph)

        // Cut the graph into independent subgraphs
        val subGraphs = cutter.cut()

        // Verify the number of subgraphs
        // From the test data visualization, we expect 2 independent subgraphs:
        // 1. r0, r1, n0, nX, n1
        // 2. r2, n20, n21
        Assertions.assertEquals(2, subGraphs.size, "Number of subgraphs should be 2")

        // Find which subgraph contains "r0" (should be the first one in our expected result)
        val subGraph1 = subGraphs.find { it.containsVertex("r0") }
        val subGraph2 = subGraphs.find { it.containsVertex("r2") }

        Assertions.assertNotNull(subGraph1, "Subgraph containing r0 should exist")
        Assertions.assertNotNull(subGraph2, "Subgraph containing r2 should exist")

        // Verify the first subgraph contains the expected vertices
        subGraph1?.let {
            val expectedVertices1 = setOf("r0", "r1", "n0", "nX", "n1")
            Assertions.assertEquals(5, it.vertexCount(), "First subgraph should have 5 vertices")

            for (vertex in expectedVertices1) {
                Assertions.assertTrue(it.containsVertex(vertex), "Subgraph 1 should contain vertex $vertex")
            }

            // Verify the edges in the first subgraph
            val edgesMap1 = mutableMapOf<String, MutableSet<String>>()
            it.forEachEdge { src, dst ->
                edgesMap1.getOrPut(src) { mutableSetOf() }.add(dst)
            }

            Assertions.assertEquals(setOf("n0", "nX"), edgesMap1["r0"])
            Assertions.assertEquals(setOf("nX", "n1"), edgesMap1["r1"])
        }

        // Verify the second subgraph contains the expected vertices
        subGraph2?.let {
            val expectedVertices2 = setOf("r2", "n20", "n21")
            Assertions.assertEquals(3, it.vertexCount(), "Second subgraph should have 3 vertices")

            for (vertex in expectedVertices2) {
                Assertions.assertTrue(it.containsVertex(vertex), "Subgraph 2 should contain vertex $vertex")
            }

            // Verify the edges in the second subgraph
            val edgesMap2 = mutableMapOf<String, MutableSet<String>>()
            it.forEachEdge { src, dst ->
                edgesMap2.getOrPut(src) { mutableSetOf() }.add(dst)
            }

            Assertions.assertEquals(setOf("n20", "n21"), edgesMap2["r2"])
        }
    }

    @Test
    fun testCutWithCycle() {
        // Create a graph with a cycle
        val graph = DAGraphImpl<String>()

        // Create a cycle: a -> b -> c -> a
        graph.addEdge("a", "b")
        graph.addEdge("b", "c")
        graph.addEdge("c", "a")

        // Add another independent component: d -> e
        graph.addEdge("d", "e")

        val cutter = DAGraphCutter(graph)
        val subGraphs = cutter.cut()

        // We should have 2 subgraphs
        Assertions.assertEquals(2, subGraphs.size, "Number of subgraphs should be 2")

        // Find which subgraph contains the cycle
        val cycleGraph = subGraphs.find { it.containsVertex("a") }
        val simpleGraph = subGraphs.find { it.containsVertex("d") }

        Assertions.assertNotNull(cycleGraph, "Subgraph containing cycle should exist")
        Assertions.assertNotNull(simpleGraph, "Subgraph containing d->e should exist")

        // Verify the cycle subgraph
        cycleGraph?.let {
            Assertions.assertEquals(3, it.vertexCount(), "Cycle subgraph should have 3 vertices")
            Assertions.assertTrue(it.containsVertex("a"))
            Assertions.assertTrue(it.containsVertex("b"))
            Assertions.assertTrue(it.containsVertex("c"))

            // Verify the edges
            val edgesMap = mutableMapOf<String, MutableSet<String>>()
            it.forEachEdge { src, dst ->
                edgesMap.getOrPut(src) { mutableSetOf() }.add(dst)
            }

            Assertions.assertEquals(setOf("b"), edgesMap["a"])
            Assertions.assertEquals(setOf("c"), edgesMap["b"])
            Assertions.assertEquals(setOf("a"), edgesMap["c"])
        }

        // Verify the simple subgraph
        simpleGraph?.let {
            Assertions.assertEquals(2, it.vertexCount(), "Simple subgraph should have 2 vertices")
            Assertions.assertTrue(it.containsVertex("d"))
            Assertions.assertTrue(it.containsVertex("e"))

            // Verify the edge
            val edgesMap = mutableMapOf<String, MutableSet<String>>()
            it.forEachEdge { src, dst ->
                edgesMap.getOrPut(src) { mutableSetOf() }.add(dst)
            }

            Assertions.assertEquals(setOf("e"), edgesMap["d"])
        }
    }

    @Test
    fun testEmptyGraph() {
        val graph = DAGraphImpl<String>()
        val cutter = DAGraphCutter(graph)
        val subGraphs = cutter.cut()

        Assertions.assertTrue(subGraphs.isEmpty(), "Empty graph should result in empty list of subgraphs")
    }
}
