package semicontinuity.idea.code.analyzer.graph;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphImplTest implements DAGraphImplTestData1, DAGraphImplTestData2, DAGraphImplTestData3 {

    @Test
    void findRoots() {
        Assertions.assertEquals(Set.of("r0", "r1", "r2"), new HashSet<>(exampleGraph1().findRoots()));
    }

}
