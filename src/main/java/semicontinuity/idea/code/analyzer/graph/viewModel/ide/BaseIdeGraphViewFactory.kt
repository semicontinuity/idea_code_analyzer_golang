package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JComponent

abstract class BaseIdeGraphViewFactory<VERTEX_PAYLOAD> :
    Factory<VERTEX_PAYLOAD, JComponent, JComponent, JComponent, JComponent, JComponent, JComponent> {

    override fun newIndependentComponents(components: List<JComponent?>): JComponent {
        val box = Box.createVerticalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.BLUE)
        } else {
            box.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        }

        box.add(Box.createVerticalGlue())
        for (component in components) {
            box.add(component)
        }
        box.add(Box.createVerticalGlue())
        return box
    }

    override fun newSplit(items: List<JComponent>, subLayer: JComponent) =
        Box.createHorizontalBox().apply {
            add(itemsBox(items))
            add(Box.createHorizontalStrut(4))
            add(subLayer)
            add(Box.createHorizontalGlue())

            border = if (showDebugBorders) {
                BorderFactory.createLineBorder(Color.YELLOW)
            } else {
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
            }
        }

    override fun newSplit(left: JComponent, right: JComponent) =
        Box.createHorizontalBox().apply {
            add(left)
            add(verticalBox(Color.GREEN, true, right))
            add(Box.createHorizontalGlue())
        }

    override fun newLayer(directDeps: JComponent?, sharedDeps: JComponent?): JComponent {
        val box = Box.createVerticalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
        } else {
            box.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        }

        box.add(Box.createVerticalGlue())

        if (directDeps != null) {
            box.add(directDepsBox(directDeps))
        }
        if (sharedDeps != null) {
            box.add(sharedDepsBox(sharedDeps))
        }

        box.add(Box.createVerticalGlue())

        return box
    }

    fun itemsBox(items: List<JComponent>): JComponent {
        val box = Box.createVerticalBox()
        if (showDebugBorders) {
            box.border = BorderFactory.createLineBorder(Color.PINK)
        } else {
            box.border = BorderFactory.createEmptyBorder(1, 1, 1, 1)
        }

        box.add(Box.createVerticalGlue())
        for (component in items) {
            box.add(component)
        }
        box.add(Box.createVerticalGlue())
        return box
    }

    fun directDepsBox(contents: JComponent) = verticalBox(Color.GREEN, false, contents)

    fun sharedDepsBox(contents: JComponent) = verticalBox(Color.RED, true, contents)

    private fun verticalBox(debugBorderColor: Color?, loweredBevel: Boolean, contents: JComponent): Box {
        val box = Box.createVerticalBox()
        box.border =
            if (showDebugBorders) {
                BorderFactory.createLineBorder(debugBorderColor)
            } else {
                when (loweredBevel) {
                    true -> BorderFactory.createLoweredBevelBorder()
                    false -> BorderFactory.createEmptyBorder(1, 1, 1, 1)
                }
            }

        box.add(Box.createVerticalGlue())
        box.add(contents)
        box.add(Box.createVerticalGlue())
        return box
    }

    companion object {
        var showDebugBorders: Boolean = false
    }
}
