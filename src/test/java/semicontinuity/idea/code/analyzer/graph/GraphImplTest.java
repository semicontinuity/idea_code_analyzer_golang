package semicontinuity.idea.code.analyzer.graph;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GraphImplTest {

    @Test
    void findRootEdges() {
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
        Graph<String, String> g = new GraphImpl<>();
        g.addNode("r0", "r0");
        g.addNode("r1", "r1");
        g.addNode("r2", "r2");
        g.addNode("n0", "n0");
        g.addNode("nX", "nX");
        g.addNode("n1", "n1");
        g.addNode("n2", "n2");

        g.addEdge("r0", "n0");
        g.addEdge("r0", "nX");
        g.addEdge("r1", "nX");
        g.addEdge("r1", "n1");
        g.addEdge("r2", "n2");

        var rootEdges = g.findRootEdges();
        Assertions.assertEquals(Set.of("r0", "r1", "r2"), rootEdges);
    }
}
