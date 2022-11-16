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
        //
        //    _ n0
        //   /
        // r0
        //   \_ nX
        //   /
        // r1
        //   \_ n1
        //
        // r2-->n20
        //    \_n21

        return new VIndependentComponents(
                List.of(
                        new VDependentComponents<>(
                                List.of(
                                        new VNode<>("r0"),
                                        new VNode<>("r1")
                                ),
                                new VIndependentComponents(
                                        List.of(
                                                new VNode<>("n0"),
                                                new VNode<>("nX"),
                                                new VNode<>("n1")
                                        )
                                )
                        ),
                        new VFanOut<>(
                                new VNode<>("r2"),
                                List.of(
                                        new VNode<>("n20"),
                                        new VNode<>("n21")
                                )
                        )
                )
        );
    }
}
