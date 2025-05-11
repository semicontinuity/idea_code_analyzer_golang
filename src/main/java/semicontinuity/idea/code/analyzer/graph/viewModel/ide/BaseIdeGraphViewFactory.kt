package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JComponent

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

    override fun newSplit(items: List<JComponent>, subLayer: JComponent): JComponent {
        val box = Box.createHorizontalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.YELLOW)
        } else {
            box.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        }

        box.add(itemsBox(items))
        box.add(subLayer)
        box.add(Box.createHorizontalStrut(20))
        box.add(Box.createGlue())
        return box
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
