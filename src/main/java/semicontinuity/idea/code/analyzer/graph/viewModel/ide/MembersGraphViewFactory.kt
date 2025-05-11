package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import semicontinuity.idea.code.analyzer.golang.Member
import semicontinuity.idea.code.analyzer.graph.viewModel.ide.RenderHelper.icon
import semicontinuity.idea.code.analyzer.graph.viewModel.ide.RenderHelper.ideButton
import javax.swing.Box
import javax.swing.ImageIcon

open class MembersGraphViewFactory(private val ideButtonHighlightingDispatcher: IdeButtonHighlightingDispatcher) :
    BaseIdeGraphViewFactory<Member>() {

    override fun newVertex(vertex: Member) =
        Box.createHorizontalBox().apply {
            add(methodButton(vertex))
            add(Box.createHorizontalGlue())
        }

    private fun methodButton(vertex: Member) = ideButton(
        vertex,
        ideButtonHighlightingDispatcher,
        methodIcon
    )

    private val methodIcon: ImageIcon? = icon("/icons/method.png")
}
