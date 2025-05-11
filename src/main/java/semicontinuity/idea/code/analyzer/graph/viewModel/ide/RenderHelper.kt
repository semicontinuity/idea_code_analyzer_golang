package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import com.intellij.util.ui.UIUtil
import semicontinuity.idea.code.analyzer.golang.Member
import semicontinuity.idea.code.analyzer.graph.DAGraph
import semicontinuity.idea.code.analyzer.graph.DAGraphViewRenderer
import java.util.function.Function
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JButton
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicButtonUI

object RenderHelper {

    fun structView(struct: String, structGraph: DAGraph<Member>, subGraphViewFactory: MembersGraphViewFactory?) =
        Box.createVerticalBox().apply {
            add(Box.createVerticalGlue())
            add(structViewContents(struct, structGraph, subGraphViewFactory))
            add(Box.createVerticalGlue())
        }

    private fun structViewContents(
        struct: String,
        structGraph: DAGraph<Member>,
        subGraphViewFactory: MembersGraphViewFactory?
    ) =
        Box.createVerticalBox().apply {
            add(Box.createVerticalGlue())

            add(
                Box.createHorizontalBox().apply {
                    add(structButton(struct))
                    add(Box.createHorizontalGlue())
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
            foreground = UIUtil.getToolTipBackground()
            background = UIUtil.getToolTipForeground()
            border = BorderFactory.createEmptyBorder()
            setUI(BasicButtonUI())
            horizontalAlignment = SwingConstants.LEFT
        }

    private fun render(graph: DAGraph<Member>, viewFactory: MembersGraphViewFactory) =
        DAGraphViewRenderer(
            graph, viewFactory, Function.identity()
        ) { obj: Member -> obj.name }.render()
}