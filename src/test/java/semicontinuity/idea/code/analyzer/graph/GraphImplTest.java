package semicontinuity.idea.code.analyzer.graph;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphImplTest {

    @Test
    void findRoots() {
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
        //
        Graph<String> g = new GraphImpl<>();

        g.addEdge("r0", "n0");
        g.addEdge("r0", "nX");
        g.addEdge("r1", "nX");
        g.addEdge("r1", "n1");
        g.addEdge("r2", "n2");

        Assertions.assertEquals(Set.of("r0", "r1", "r2"), new HashSet<>(g.findRoots()));
    }
}
