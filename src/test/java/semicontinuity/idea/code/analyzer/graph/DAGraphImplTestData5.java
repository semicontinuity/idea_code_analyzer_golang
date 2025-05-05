package semicontinuity.idea.code.analyzer.graph;

public interface DAGraphImplTestData5 {

    default DAGraph<String> exampleGraph5() {
        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("A", "a");
        g.addEdge("A", "b");

        g.addEdge("B", "a");
        g.addEdge("B", "b");

        g.addEdge("C", "a");
        g.addEdge("C", "b");
        g.addEdge("C", "c");
        return g;
    }

    default DAGraph<String> exampleGraph5SubgraphABC() {
        DAGraph<String> g = new DAGraphImpl<>();
        g.addVertex("a");
        g.addVertex("b");
        g.addVertex("c");
        return g;
    }
}
