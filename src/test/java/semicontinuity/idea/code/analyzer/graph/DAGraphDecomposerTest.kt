package semicontinuity.idea.code.analyzer.graph

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DAGraphDecomposerTest : DAGraphImplTestData1, DAGraphImplTestData2, DAGraphImplTestData3,
    DAGraphImplTestData4, DAGraphImplTestData5, DAGraphImplTestData6 {

    @Test
    fun decompose_1() {
        Assertions.assertEquals(
            mapOf(
                setOf("r0", "r1") to exampleGraph1SubgraphR0R1Back(),
                setOf("r2") to exampleGraph1SubgraphR2Back()
            ),
            DAGraphDecomposer(exampleGraph1()).decompose()
        )
    }

    @Test
    fun decompose_2() {
        Assertions.assertEquals(
            mapOf(
                setOf("r0", "r1") to exampleGraph2SubgraphR0R1()
            ),
            DAGraphDecomposer(exampleGraph2()).decompose()
        )
    }

    @Test
    fun decompose_3() {
        Assertions.assertEquals(
            mapOf(
                setOf("r0", "r1") to exampleGraph3SubgraphR0R1(),
                setOf("r2") to exampleGraph3SubgraphR2()
            ),
            DAGraphDecomposer(exampleGraph3()).decompose()
        )
    }

    @Test
    fun decompose_4() {
        Assertions.assertEquals(
            mapOf(
                setOf("r0", "r1") to exampleGraph4SubgraphR0R1(),
                setOf("r2") to exampleGraph4SubgraphR2()
            ),
            DAGraphDecomposer(exampleGraph4()).decompose()
        )
    }

    @Test
    fun decompose_5() {
        Assertions.assertEquals(
            mapOf(
                setOf("A", "B", "C") to exampleGraph5SubgraphABC()
            ),
            DAGraphDecomposer(exampleGraph5()).decompose()
        )
    }

    @Test
    fun decompose_6() {
        Assertions.assertEquals(
            mapOf(
                setOf("ListDeactivatedTargetsByDC") to DAGraphImpl<Any>(),

                setOf(
                    "GetAllInstancesByTarget",
                    "IsDCEnabled",
                    "UpdateInstance",
                    "DeactivateDC",
                    "GetInstances",
                    "ActivateDC"
                ) to subgraph6_1(),

                setOf(
                    "GetConfig",
                    "SaveConfig"
                ) to subgraph6_2(),

                setOf(
                    "DeleteDCsFromRegion",
                    "GetRegion",
                    "AddDCsToRegion"
                ) to subgraph6_3(),

            ),
            DAGraphDecomposer(exampleGraph6()).decompose()
        )
    }

    companion object {
        private fun subgraph6_1(): DAGraphImpl<String> {
            val g = DAGraphImpl<String>()
            g.addVertex("getTarget")
            g.addVertex("processGetInstancesResult")
            g.addVertex("getDisabledDatacenterTargets")
            g.addVertex("getCurrentTime")
            g.addVertex("GetAllTargets")

            g.addEdge("processGetInstancesResult", "getDisabledDatacenterTargets")
            g.addEdge("processGetInstancesResult", "getCurrentTime")
            return g
        }

        private fun subgraph6_2(): DAGraphImpl<String> {
            val g = DAGraphImpl<String>()
            g.addVertex("getConfigTarget")
            return g
        }

        private fun subgraph6_3(): DAGraphImpl<String> {
            val g = DAGraphImpl<String>()
            g.addVertex("getDCsFromRegion")
            g.addVertex("getRegionTarget")
            return g
        }
    }
}
