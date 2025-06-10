package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import semicontinuity.idea.code.analyzer.graph.DAGraph
import semicontinuity.idea.code.analyzer.graph.DAGraphImplTestData7
import semicontinuity.idea.code.analyzer.graph.DAGraphViewRenderer
import semicontinuity.idea.code.analyzer.graph.DAGraphViewRendererDelegate1

class DAGraphSerializationTest : DAGraphImplTestData7 {
    @Test
    @Throws(JsonProcessingException::class)
    fun serialize() {
        println(ObjectMapper().writeValueAsString(render(exampleGraph7())))
    }

    companion object {
        private fun render(graph: DAGraph<String>): VComponent? {
            return DAGraphViewRenderer(
                graph,
                VFactory(),
                { id: String -> id },
                { s: String -> s },
                DAGraphViewRendererDelegate1(
                    VFactory(),
                    { id: String -> id },
                    { s: String -> s },
                )::doRender

            ).render()
        }
    }
}
