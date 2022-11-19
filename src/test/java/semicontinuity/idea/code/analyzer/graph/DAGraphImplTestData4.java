package semicontinuity.idea.code.analyzer.graph;

public interface DAGraphImplTestData4 {

    default DAGraph<String> exampleGraph4() {
        //
        //    ,-> n0  +----> nA
        //   /       +-----> nB
        // r0       /
        //   `--> nX +--+-> [nY]
        //            \/
        //            /\
        // r1--------+--+-> [nZ]
        //   `--> n1
        //
        // r2-->  n20 --> n3
        //   \->  n21 /

        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("r0", "n0");
        g.addEdge("r0", "nX");
        g.addEdge("nX", "nA");
        g.addEdge("nX", "nB");
        g.addEdge("nX", "nY");
        g.addEdge("nX", "nZ");
        g.addEdge("r1", "nY");
        g.addEdge("r1", "nZ");
        g.addEdge("r1", "n1");
        g.addEdge("r2", "n20");
        g.addEdge("r2", "n21");
        g.addEdge("n20", "n3");
        g.addEdge("n21", "n3");
        return g;
    }

    default DAGraph<String> exampleGraph4SubgraphR0R1() {
        DAGraph<String> g = new DAGraphImpl<>();
        g.addNode("n0");
        g.addNode("n1");
        g.addEdge("nX", "nA");
        g.addEdge("nX", "nB");
        g.addEdge("nX", "nY");
        g.addEdge("nX", "nZ");
        return g;
    }

    default DAGraph<String> exampleGraph4SubgraphR2() {
        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("n20", "n3");
        g.addEdge("n21", "n3");
        return g;
    }
}
