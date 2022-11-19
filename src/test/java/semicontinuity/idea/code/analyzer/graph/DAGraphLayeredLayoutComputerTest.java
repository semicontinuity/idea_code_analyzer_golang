package semicontinuity.idea.code.analyzer.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphLayeredLayoutComputerTest implements DAGraphImplTestData2 {

    @Test
    void layout() {
        Assertions.assertEquals(exampleGraph2Depths(), new DAGraphLayeredLayoutComputer<>(exampleGraph2()).layout());
    }
}
