package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import semicontinuity.idea.code.analyzer.golang.Member
import semicontinuity.idea.code.analyzer.graph.DAGraph
import javax.swing.JButton
import javax.swing.JComponent

class CoarseGraphViewFactory(
    private val members: Map<String, Member>,
    private val subGraphs: Map<String, DAGraph<Member>>,
    private val subGraphViewFactory: MembersGraphViewFactory,
    private val ideButtonHighlightingDispatcher: IdeButtonHighlightingDispatcher,
) : BaseIdeGraphViewFactory<String>() {

    override fun newVertex(vertex: String): JComponent {
        val subGraph = subGraphs[vertex]
        val member = members[vertex]
        return if (subGraph == null) {
            if (member != null) {
                subGraphViewFactory.newVertex(member)
            } else {
                JButton(vertex)
            }
        } else {
            RenderHelper.structView(member!!, vertex, subGraph, subGraphViewFactory, ideButtonHighlightingDispatcher)
        }
    }
}
