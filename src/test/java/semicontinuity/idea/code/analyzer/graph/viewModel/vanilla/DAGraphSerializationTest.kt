package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import semicontinuity.idea.code.analyzer.graph.DAGraph;
import semicontinuity.idea.code.analyzer.graph.DAGraphImplTestData7;
import semicontinuity.idea.code.analyzer.graph.DAGraphViewRenderer;

public class DAGraphSerializationTest implements DAGraphImplTestData7 {

    @Test
    public void serialize() throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(render(exampleGraph7())));
    }

    private static Object render(DAGraph<String> graph) {
        return new DAGraphViewRenderer<>(graph, new VFactory<>(), (String id) -> id, (String s) -> s).render();
    }
}
