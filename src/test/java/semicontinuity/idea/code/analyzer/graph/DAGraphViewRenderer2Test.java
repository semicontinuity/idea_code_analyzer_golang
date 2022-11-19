package semicontinuity.idea.code.analyzer.graph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import semicontinuity.idea.code.analyzer.graph.viewModel.vanilla.VFactory;

class DAGraphViewRenderer2Test implements DAGraphImplTestData3 {

    @Test
    void render() throws JsonProcessingException {
        var renderer = new DAGraphViewRenderer2<>(new VFactory<>(), (String id) -> id);

        var render = renderer.render(exampleGraph3());
        System.out.println(new ObjectMapper().writeValueAsString(render));
    }
}
