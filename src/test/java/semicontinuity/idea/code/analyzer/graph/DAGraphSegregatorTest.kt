package semicontinuity.idea.code.analyzer.graph

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DAGraphSegregatorTest : DAGraphImplTestData2 {

    @Test
    fun testSegregation() {
        val result = DAGraphSegregator(exampleGraph2()).segregate()

        Assertions.assertEquals(exampleGraph2Front() to exampleGraph2Back(), result);
    }
}
