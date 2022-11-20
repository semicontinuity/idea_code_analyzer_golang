package semicontinuity.idea.code.analyzer.graph;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphSplitterTest implements DAGraphImplTestData1 {

    @Test
    void run() {
        var sut = new DAGraphSplitter<>(exampleGraph1(), s -> exampleGraph1SubgraphR0R1Nodes().contains(s) ? 0 : 1);
        Assertions.assertEquals(
                Map.of(
                        0, exampleGraph1SubgraphR0R1(),
                        1, exampleGraph1SubgraphR2()
                ),
                sut.run()
        );
    }
}
