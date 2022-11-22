package semicontinuity.idea.code.analyzer.graph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VFactory;

class DAGraphViewRendererTest implements DAGraphImplTestData1, DAGraphImplTestData3, DAGraphImplTestData4 {

    @Test
    void render1() throws JsonProcessingException {
        render(exampleGraph1());
    }

    @Test
    void render3() throws JsonProcessingException {
        render(exampleGraph3());
    }

    @Test
    void render4() throws JsonProcessingException {
        render(exampleGraph4());
    }

    private void render(DAGraph<String> graph) throws JsonProcessingException {
        var renderer = new DAGraphViewRenderer<>(graph, new VFactory<>(), (String id) -> id);

        var render = renderer.render();
        System.out.println(new ObjectMapper().writeValueAsString(render));
    }
}
