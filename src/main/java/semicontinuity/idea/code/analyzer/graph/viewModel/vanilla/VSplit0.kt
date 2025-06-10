package semicontinuity.idea.code.analyzer.graph.viewModel.vanilla
import com.fasterxml.jackson.annotation.JsonProperty

class VSplit0(
    @field:JsonProperty private val left: VComponent?,
    @field:JsonProperty private val right: VComponent?
) : VComponent()
