package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import com.intellij.util.ui.UIUtil
import semicontinuity.idea.code.analyzer.golang.Member
import semicontinuity.idea.code.analyzer.graph.DAGraph
import semicontinuity.idea.code.analyzer.graph.DAGraphViewRenderer
import java.awt.BorderLayout
import java.util.function.Function
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicButtonUI

object RenderHelper {

    fun structView(struct: String, structGraph: DAGraph<Member>, subGraphViewFactory: MembersGraphViewFactory?): JPanel {
        val structView = JPanel()
        structView.layout = BorderLayout()

        structView.border = BorderFactory.createRaisedBevelBorder()

        structView.add(structButton(struct), BorderLayout.NORTH)

        val contents = render(structGraph, subGraphViewFactory!!)
        contents.border = BorderFactory.createLoweredBevelBorder()
        structView.add(contents, BorderLayout.CENTER)

        return structView
    }

    private fun structButton(struct: String): JButton {
        val button = JButton(struct)
        button.foreground = UIUtil.getToolTipBackground()
        button.background = UIUtil.getToolTipForeground()
        button.border = BorderFactory.createEmptyBorder()
        button.setUI(BasicButtonUI())
        button.horizontalAlignment = SwingConstants.LEFT
        return button
    }

    private fun render(graph: DAGraph<Member>, viewFactory: MembersGraphViewFactory): JComponent {
        return DAGraphViewRenderer(
            graph, viewFactory, Function.identity()
        ) { obj: Member -> obj.name }.render()
    }
}