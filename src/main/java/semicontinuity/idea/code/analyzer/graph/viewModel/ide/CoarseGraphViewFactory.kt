package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import semicontinuity.idea.code.analyzer.golang.Member
import semicontinuity.idea.code.analyzer.graph.DAGraph
import javax.swing.JComponent

class CoarseGraphViewFactory(
    private val simpleVertices: Map<String, Member>,
    private val subGraphs: Map<String, DAGraph<Member>>,
    private val subGraphViewFactory: MembersGraphViewFactory,
) : BaseIdeGraphViewFactory<String>() {

    override fun newVertex(vertex: String): JComponent {
        val member = simpleVertices[vertex]
        return if (member != null) {
            subGraphViewFactory.newVertex(member)
        } else {
            RenderHelper.structView(vertex, subGraphs[vertex]!!, subGraphViewFactory)
        }
    }
}
