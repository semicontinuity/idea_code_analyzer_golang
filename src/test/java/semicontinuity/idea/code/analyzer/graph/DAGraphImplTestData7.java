package semicontinuity.idea.code.analyzer.graph;

public interface DAGraphImplTestData7 {

    default DAGraph<String> exampleGraph7() {
        DAGraph<String> g = new DAGraphImpl<>();
        g.addEdge("R", "A");
        g.addEdge("A", "AA");

        g.addEdge("R", "B");
        g.addEdge("B", "BB");
        return g;
    }
}
