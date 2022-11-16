package semicontinuity.idea.code.analyzer.graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphImplTest implements DAGraphImplTestData {

    @Test
    void findRoots() {
        Assertions.assertEquals(Set.of("r0", "r1", "r2"), new HashSet<>(exampleGraph1().findRoots()));
    }

    @Test
    void decompose() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph1SubgraphR0R1(),
                        Set.of("r2"), exampleGraph1SubgraphR2()
                ),
                exampleGraph1().decompose()
        );
    }
}
