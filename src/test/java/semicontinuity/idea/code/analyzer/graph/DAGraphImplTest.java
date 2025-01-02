package semicontinuity.idea.code.analyzer.graph;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DAGraphImplTest implements DAGraphImplTestData1, DAGraphImplTestData5 {

    @Test
    void findRoots1() {
        Assertions.assertEquals(Set.of("r0", "r1", "r2"), new HashSet<>(exampleGraph1().findRoots()));
    }

    @Test
    void findRoots5() {
        Assertions.assertEquals(Set.of("A", "B", "C"), new HashSet<>(exampleGraph5().findRoots()));
    }
}
