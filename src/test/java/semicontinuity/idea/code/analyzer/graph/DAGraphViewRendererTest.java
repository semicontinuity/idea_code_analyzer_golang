package semicontinuity.idea.code.analyzer.graph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VFactory;

class DAGraphViewRendererTest implements DAGraphImplTestData1, DAGraphImplTestData3 {

    @Test
    void render1() throws JsonProcessingException {
        var renderer = new DAGraphViewRenderer<>(new VFactory<>(), (String id) -> id);

        var render = renderer.render(exampleGraph1());
        System.out.println(new ObjectMapper().writeValueAsString(render));
    }

    @Test
    void render3() throws JsonProcessingException {
        var renderer = new DAGraphViewRenderer<>(new VFactory<>(), (String id) -> id);

        var render = renderer.render(exampleGraph3());
        System.out.println(new ObjectMapper().writeValueAsString(render));
    }
}
