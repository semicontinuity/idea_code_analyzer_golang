package semicontinuity.idea.code.analyzer.graph;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphDecomposerTest implements DAGraphImplTestData1, DAGraphImplTestData2, DAGraphImplTestData3, DAGraphImplTestData4 {

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
}
