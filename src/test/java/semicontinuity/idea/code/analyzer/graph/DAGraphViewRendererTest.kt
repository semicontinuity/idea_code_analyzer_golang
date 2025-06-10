package semicontinuity.idea.code.analyzer.graph

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VComponent
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VFactory
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VIndependentComponents
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VLayer
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VMember
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VSplit
import java.util.function.Function

internal class DAGraphViewRendererTest : DAGraphImplTestData1, DAGraphImplTestData3, DAGraphImplTestData4 {
    @Test
    @Throws(JsonProcessingException::class)
    fun render1() {
        render(exampleGraph1())
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun render3() {
        render(exampleGraph3())
    }

    @Test
    @Throws(JsonProcessingException::class)
    fun render4() {
        render(exampleGraph4())
    }

    @Throws(JsonProcessingException::class)
    private fun render(graph: DAGraph<String>) {
        val renderer =
            DAGraphViewRenderer(
                graph,
                VFactory(),
                { id: String -> id },
                { s: String -> s },
                DAGraphViewRendererDelegate(
                    VFactory(),
                    { id: String -> id },
                    { s: String -> s },
                )::doRender
            )

        val render = renderer.render()
        println(ObjectMapper().writeValueAsString(render))
    }
}
