package semicontinuity.idea.code.analyzer.graph;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphImplDecomposeTest implements DAGraphImplTestData1, DAGraphImplTestData2, DAGraphImplTestData3 {

    @Test
    void decompose_1() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph1SubgraphR0R1(),
                        Set.of("r2"), exampleGraph1SubgraphR2()
                ),
                exampleGraph1().decompose()
        );
    }
    @Test
    void decompose_2() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph2SubgraphR0R1()
                ),
                exampleGraph2().decompose()
        );
    }

    @Test
    void decompose_3() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph3SubgraphR0R1(),
                        Set.of("r2"), exampleGraph3SubgraphR2()
                ),
                exampleGraph3().decompose()
        );
    }
}
