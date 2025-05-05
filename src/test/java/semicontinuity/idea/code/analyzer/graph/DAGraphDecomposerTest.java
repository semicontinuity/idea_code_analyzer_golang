package semicontinuity.idea.code.analyzer.graph;

import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphDecomposerTest implements
        DAGraphImplTestData1, DAGraphImplTestData2, DAGraphImplTestData3, DAGraphImplTestData4, DAGraphImplTestData5, DAGraphImplTestData6 {

    @Test
    void decompose_1() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph1SubgraphR0R1Back(),
                        Set.of("r2"), exampleGraph1SubgraphR2Back()
                ),
                new DAGraphDecomposer<>(exampleGraph1()).decompose()
        );
    }

    @Test
    void decompose_2() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph2SubgraphR0R1()
                ),
                new DAGraphDecomposer<>(exampleGraph2()).decompose()
        );
    }

    @Test
    void decompose_3() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph3SubgraphR0R1(),
                        Set.of("r2"), exampleGraph3SubgraphR2()
                ),
                new DAGraphDecomposer<>(exampleGraph3()).decompose()
        );
    }

    @Test
    void decompose_4() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph4SubgraphR0R1(),
                        Set.of("r2"), exampleGraph4SubgraphR2()
                ),
                new DAGraphDecomposer<>(exampleGraph4()).decompose()
        );
    }

    @Test
    void decompose_5() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("A", "B", "C"), exampleGraph5SubgraphABC()
                ),
                new DAGraphDecomposer<>(exampleGraph5()).decompose()
        );
    }

    @Test
    void decompose_6() {
        Assertions.assertEquals(
                Map.ofEntries(
                        Map.entry(
                                Set.of("ListDeactivatedTargetsByDC"),
                                new DAGraphImpl<>()
                        ),
                        Map.entry(
                                Set.of(
                                        "GetAllInstancesByTarget",
                                        "IsDCEnabled",
                                        "UpdateInstance",
                                        "DeactivateDC",
                                        "GetInstances",
                                        "ActivateDC"
                                ),
                                subgraph6_1()
                        ),
                        Map.entry(
                                Set.of(
                                        "GetConfig",
                                        "SaveConfig"
                                ),
                                subgraph6_2()
                        ),
                        Map.entry(
                                Set.of(
                                        "DeleteDCsFromRegion",
                                        "GetRegion",
                                        "AddDCsToRegion"
                                ),
                                subgraph6_3()
                        )
                ),
                new DAGraphDecomposer<>(exampleGraph6()).decompose()
        );
    }

    @NotNull
    private static DAGraphImpl<String> subgraph6_1() {
        var g = new DAGraphImpl<String>();
        g.addVertex("getTarget");
        g.addVertex("processGetInstancesResult");
        g.addVertex("getDisabledDatacenterTargets");
        g.addVertex("getCurrentTime");
        g.addVertex("GetAllTargets");

        g.addEdge("processGetInstancesResult", "getDisabledDatacenterTargets");
        g.addEdge("processGetInstancesResult", "getCurrentTime");
        return g;
    }

    @NotNull
    private static DAGraphImpl<String> subgraph6_2() {
        var g = new DAGraphImpl<String>();
        g.addVertex("getConfigTarget");
        return g;
    }

    @NotNull
    private static DAGraphImpl<String> subgraph6_3() {
        var g = new DAGraphImpl<String>();
        g.addVertex("getDCsFromRegion");
        g.addVertex("getRegionTarget");
        return g;
    }
}
