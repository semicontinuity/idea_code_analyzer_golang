package semicontinuity.idea.code.analyzer.graph;

public interface DAGraphImplTestData3 {

    default DAGraph<String> exampleGraph3() {
        //
        //    ,-> n0
        //   /
        // r0
        //   `--> nX --> [nY]
        //             /
        // r1---------^
        //   `--> n1
        //
        // r2-->  n20
        //   \->  n21

        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("r0", "n0");
        g.addEdge("r0", "nX");
        g.addEdge("nX", "nY");
        g.addEdge("r1", "nY");
        g.addEdge("r1", "n1");
        g.addEdge("r2", "n20");
        g.addEdge("r2", "n21");
        return g;
    }

    default DAGraph<String> exampleGraph3SubgraphR0R1() {
        // n0
        // nX --> [nY]
        // n1

        DAGraph<String> g = new DAGraphImpl<>();
        g.addVertex("n0");
        g.addVertex("n1");
        g.addEdge("nX", "nY");
        return g;
    }

    default DAGraph<String> exampleGraph3SubgraphR2() {
        // n20
        // n21

        DAGraph<String> g = new DAGraphImpl<>();
        g.addVertex("n20");
        g.addVertex("n21");
        return g;
    }
}
