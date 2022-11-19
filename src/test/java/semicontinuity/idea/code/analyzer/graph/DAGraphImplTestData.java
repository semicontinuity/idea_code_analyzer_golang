package semicontinuity.idea.code.analyzer.graph;

import java.util.Map;

public interface DAGraphImplTestData {

    default DAGraph<String> exampleGraph1() {
        //
        //    _ n0
        //   /
        // r0
        //   \_ nX
        //   /
        // r1
        //   \_ n1
        //
        // r2-->n20
        //   \->n21

        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("r0", "n0");
        g.addEdge("r0", "nX");
        g.addEdge("r1", "nX");
        g.addEdge("r1", "n1");
        g.addEdge("r2", "n20");
        g.addEdge("r2", "n21");
        return g;
    }

    default DAGraph<String> exampleGraph1SubgraphR0R1() {
        // n0
        // nX
        // n1

        DAGraph<String> g = new DAGraphImpl<>();
        g.addNode("n0");
        g.addNode("nX");
        g.addNode("n1");
        return g;
    }

    default DAGraph<String> exampleGraph1SubgraphR2() {
        // n20
        // n21

        DAGraph<String> g = new DAGraphImpl<>();
        g.addNode("n20");
        g.addNode("n21");
        return g;
    }

    default DAGraphImpl<String> exampleGraph2() {

        // r0 --> m1 -     --> f1
        //            \   /
        // r1 --------> d1 --> f2

        var g = new DAGraphImpl<String>();
        g.addEdge("r0", "m1");
        g.addEdge("r1", "d1");
        g.addEdge("m1", "d1");
        g.addEdge("d1", "f1");
        g.addEdge("d1", "f2");
        return g;
    }

    default Map<String, Integer> exampleGraph2Depths() {

        // r0 --> m1 -     --> f1
        //            \   /
        // r1 --------> d1 --> f2

        return Map.of(
            "r0", 0,
            "r1", 0,
            "m1", 1,
            "d1", 2,
            "f1", 3,
            "f2", 3
        );
    }
}
