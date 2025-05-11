package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import com.intellij.util.ui.UIUtil
import semicontinuity.idea.code.analyzer.golang.Member
import semicontinuity.idea.code.analyzer.graph.DAGraph
import semicontinuity.idea.code.analyzer.graph.DAGraphViewRenderer
import java.awt.Dimension
import java.util.function.Function
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicButtonUI

object RenderHelper {

    fun structView(
        member: Member,
        struct: String,
        structGraph: DAGraph<Member>,
        subGraphViewFactory: MembersGraphViewFactory?,
        ideButtonHighlightingDispatcher: IdeButtonHighlightingDispatcher
    ) =
        Box.createVerticalBox().apply {
            add(Box.createVerticalGlue())
            add(structViewContents(member, struct, structGraph, subGraphViewFactory, ideButtonHighlightingDispatcher))
            add(Box.createVerticalGlue())
        }

    private fun structViewContents(
        member: Member,
        struct: String,
        structGraph: DAGraph<Member>,
        subGraphViewFactory: MembersGraphViewFactory?,
        ideButtonHighlightingDispatcher: IdeButtonHighlightingDispatcher,
    ) =
        Box.createVerticalBox().apply {
            add(Box.createVerticalGlue())

            add(
                Box.createHorizontalBox().apply {
                    add(structButton(struct))
/*
                    add(
                        ideButton(
                            member,
                            ideButtonHighlightingDispatcher,
                            null,
                        ).apply {
                            horizontalAlignment = SwingConstants.LEFT
                            maximumSize = Dimension(Int.MAX_VALUE, getPreferredSize().height)
                            foreground = UIUtil.getToolTipBackground()
                            background = UIUtil.getToolTipForeground()
                        }
                    )
*/
                }
            )

            add(
                render(structGraph, subGraphViewFactory!!).apply {
                    border = BorderFactory.createLoweredBevelBorder()
                },
            )

            add(Box.createVerticalGlue())
        }

    private fun structButton(struct: String) =
        JButton(struct).apply {
            setUI(BasicButtonUI())
            horizontalAlignment = SwingConstants.LEFT
            maximumSize = Dimension(Int.MAX_VALUE, getPreferredSize().height)
            foreground = UIUtil.getToolTipBackground()
            background = UIUtil.getToolTipForeground()
            border = BorderFactory.createEmptyBorder()
        }

    private fun render(graph: DAGraph<Member>, viewFactory: MembersGraphViewFactory) =
        DAGraphViewRenderer(
            graph, viewFactory, Function.identity()
        ) { obj: Member -> obj.name }.render()

    fun ideButton(
        vertex: Member, ideButtonHighlightingDispatcher: IdeButtonHighlightingDispatcher, icon: ImageIcon?
    ) = IdeButton(
        vertex.psiElement,
        vertex.name,
        icon,
        vertex,
        ideButtonHighlightingDispatcher
    ).also {
        ideButtonHighlightingDispatcher.register(vertex, it)
    }

    fun icon(iconResource: String): ImageIcon? {
        val resource = IdeButton::class.java.getResource(iconResource) ?: return null
        return ImageIcon(resource)
    }
}