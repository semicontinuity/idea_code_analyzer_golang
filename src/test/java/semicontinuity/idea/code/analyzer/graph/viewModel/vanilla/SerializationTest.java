package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class SerializationTest {

    @Test
    public void serialize() throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(exampleViewModel1()));
    }

    private Object exampleViewModel1() {
        return new VIndependentComponents(
                List.of(
                        new VNode<>("n20"),
                        new VNode<>("n21")
                )
        );
    }
}
