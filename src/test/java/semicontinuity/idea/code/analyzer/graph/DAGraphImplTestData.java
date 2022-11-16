package semicontinuity.idea.code.analyzer.graph;

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
        //    _ n0
        //   /
        // r0
        //   \_ nX
        //   /
        // r1
        //   \_ n1

        DAGraph<String> g = new DAGraphImpl<>();
        g.addNode("n0");
        g.addNode("nX");
        g.addNode("n1");
//        g.addEdge("r0", "n0");
//        g.addEdge("r0", "nX");
//        g.addEdge("r1", "nX");
//        g.addEdge("r1", "n1");
        return g;
    }

    default DAGraph<String> exampleGraph1SubgraphR2() {
        // r2-->n20
        //   \->n21

        DAGraph<String> g = new DAGraphImpl<>();
        g.addNode("n20");
        g.addNode("n21");
//        g.addEdge("r2", "n20");
//        g.addEdge("r2", "n21");
        return g;
    }
}
