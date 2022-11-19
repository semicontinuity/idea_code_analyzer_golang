package semicontinuity.idea.code.analyzer.graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphImplTest implements DAGraphImplTestData1, DAGraphImplTestData2, DAGraphImplTestData3 {

    @Test
    void findRoots() {
        Assertions.assertEquals(Set.of("r0", "r1", "r2"), new HashSet<>(exampleGraph1().findRoots()));
    }

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
    void decompose_3() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph3SubgraphR0R1(),
                        Set.of("r2"), exampleGraph3SubgraphR2()
                ),
                exampleGraph3().decompose()
        );
    }

    @Test
    void layout() {
        Assertions.assertEquals(exampleGraph2Depths(), exampleGraph2().layout());
    }
}
