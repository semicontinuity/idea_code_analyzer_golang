package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JComponent
import javax.swing.JPanel

abstract class BaseIdeGraphViewFactory<VERTEX_PAYLOAD> :
    Factory<VERTEX_PAYLOAD, JComponent, JComponent, JComponent, JComponent, JComponent> {

    override fun newIndependentComponents(components: List<JComponent>): JComponent {
        val box = Box.createVerticalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.BLUE)
        } else {
            box.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        }

        for (component in components) {
            box.add(component)
        }
        return box
    }

    override fun newSplit(items: List<JComponent>, subLayer: JComponent) =
        JPanel(BorderLayout()).apply {
            border = if (showDebugBorders) {
                BorderFactory.createLineBorder(Color.YELLOW)
            } else {
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
            }

            add(
                Box.createHorizontalBox().apply {
                    add(itemsBox(items))
                    add(Box.createHorizontalStrut(4))
                    add(subLayer)
                    add(Box.createGlue())
                }
            )
        }

    override fun newLayer(directDeps: JComponent?, sharedDeps: JComponent?): JComponent {
        val box = Box.createVerticalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
        } else {
            box.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        }

        if (directDeps != null) {
            box.add(directDepsBox(directDeps))
        }
        if (sharedDeps != null) {
            box.add(sharedDepsBox(sharedDeps))
        }
        return box
    }

    fun itemsBox(items: List<JComponent>): JComponent {
        val box = Box.createVerticalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.PINK)
        } else {
            box.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        }

        for (component in items) {
            box.add(component)
        }
        return box
    }

    fun directDepsBox(contents: JComponent?): JComponent {
        val box = Box.createVerticalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.GREEN)
        } else {
            box.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        }

        box.add(contents)
        return box
    }

    fun sharedDepsBox(contents: JComponent?): JComponent {
        val box = Box.createVerticalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.RED)
        } else {
            box.border = BorderFactory.createLoweredBevelBorder()
        }

        box.add(contents)
        return box
    }

    companion object {
        var showDebugBorders: Boolean = false
    }
}
