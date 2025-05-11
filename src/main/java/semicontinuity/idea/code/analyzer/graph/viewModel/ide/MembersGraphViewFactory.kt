package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import semicontinuity.idea.code.analyzer.golang.Member
import javax.swing.Box
import javax.swing.ImageIcon
import javax.swing.JComponent

open class MembersGraphViewFactory(private val ideButtonHighlightingDispatcher: IdeButtonHighlightingDispatcher) :
    BaseIdeGraphViewFactory<Member>() {

    override fun newVertex(vertex: Member): JComponent {
        val box = Box.createHorizontalBox()
        val ideButton = IdeButton(
            vertex.psiElement,
            vertex.name,
            methodIcon,
            vertex,
            ideButtonHighlightingDispatcher
        )
        ideButtonHighlightingDispatcher.register(vertex, ideButton)

        box.add(ideButton)
        // box.add(Box.createHorizontalGlue())
        // box.add(Box.createHorizontalGlue())
        box.add(Box.createHorizontalStrut(10))
        return box
    }

    companion object {
        var methodIcon: ImageIcon? = icon("/icons/method.png")

        protected fun icon(iconResource: String): ImageIcon? {
            val resource = IdeButton::class.java.getResource(iconResource) ?: return null
            return ImageIcon(resource)
        }
    }
}
