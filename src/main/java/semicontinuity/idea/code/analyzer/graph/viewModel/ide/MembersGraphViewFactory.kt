package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import semicontinuity.idea.code.analyzer.golang.Member
import java.awt.BorderLayout
import javax.swing.Box
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JPanel

open class MembersGraphViewFactory(private val ideButtonHighlightingDispatcher: IdeButtonHighlightingDispatcher) :
    BaseIdeGraphViewFactory<Member>() {

    override fun newVertex(vertex: Member) =
        JPanel(BorderLayout()).apply {
            add(ideButton(vertex), BorderLayout.WEST)
        }

    private fun ideButton(vertex: Member) = IdeButton(
        vertex.psiElement,
        vertex.name,
        methodIcon,
        vertex,
        ideButtonHighlightingDispatcher
    ).also {
        ideButtonHighlightingDispatcher.register(vertex, it)
    }

    companion object {
        var methodIcon: ImageIcon? = icon("/icons/method.png")

        protected fun icon(iconResource: String): ImageIcon? {
            val resource = IdeButton::class.java.getResource(iconResource) ?: return null
            return ImageIcon(resource)
        }
    }
}
