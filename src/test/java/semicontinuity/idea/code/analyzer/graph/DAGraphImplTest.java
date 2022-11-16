package semicontinuity.idea.code.analyzer.graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphImplTest {

    @Test
    void findRoots() {
        Assertions.assertEquals(Set.of("r0", "r1", "r2"), new HashSet<>(exampleGraph1().findRoots()));
    }

    @Test
    void rootsWithSubgraph() {
        Assertions.assertEquals(
                Map.of(
                        Set.of("r0", "r1"), exampleGraph1SubgraphR0R1(),
                        Set.of("r2"), exampleGraph1SubgraphR2()
                ),
                exampleGraph1().rootsWithSubgraph()
        );
    }

    private static DAGraph<String> exampleGraph1() {
        //
        //    _ n0
        //   /
        // r0
        //   \_ nX
        //   /
        // r1
        //   \_ n1
        //
        // r2-->n2

        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("r0", "n0");
        g.addEdge("r0", "nX");
        g.addEdge("r1", "nX");
        g.addEdge("r1", "n1");
        g.addEdge("r2", "n2");
        return g;
    }

    private static DAGraph<String> exampleGraph1SubgraphR0R1() {
        //    _ n0
        //   /
        // r0
        //   \_ nX
        //   /
        // r1
        //   \_ n1

        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("r0", "n0");
        g.addEdge("r0", "nX");
        g.addEdge("r1", "nX");
        g.addEdge("r1", "n1");
        return g;
    }

    private static DAGraph<String> exampleGraph1SubgraphR2() {
        // r2-->n2

        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("r2", "n2");
        return g;
    }
}
