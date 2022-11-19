package semicontinuity.idea.code.analyzer.graph;

import java.util.Map;

public interface DAGraphImplTestData2 {

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
