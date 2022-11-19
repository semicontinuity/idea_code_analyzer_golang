package semicontinuity.idea.code.analyzer.graph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VFactory;

class DAGraphViewRenderer2Test implements DAGraphImplTestData1, DAGraphImplTestData3 {

    @Test
    void render1() throws JsonProcessingException {
        render(exampleGraph1());
    }

    @Test
    void render3() throws JsonProcessingException {
        render(exampleGraph3());
    }

    private void render(DAGraph<String> graph) throws JsonProcessingException {
        var renderer = new DAGraphViewRenderer2<>(graph, new VFactory<>(), (String id) -> id);

        var render = renderer.render();
        System.out.println(new ObjectMapper().writeValueAsString(render));
    }
}
