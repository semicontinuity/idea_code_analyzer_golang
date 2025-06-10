package semicontinuity.idea.code.analyzer.graph.viewModel.swing

import com.intellij.ui.JBColor
import semicontinuity.idea.code.analyzer.graph.viewModel.Factory
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JComponent

class SwingViewFactory : Factory<String, JComponent, JComponent, JComponent, JComponent, JComponent, JComponent> {

    override fun newVertex(payload: String): JComponent {
        val box = Box.createHorizontalBox()
        box.add(JButton(payload))
        box.add(Box.createHorizontalGlue())
        return box
    }

    override fun newIndependentComponents(components: List<JComponent?>): JComponent {
        val box = Box.createVerticalBox()
        box.foreground = Color.YELLOW
        //        box.setBorder(BorderFactory.createEmptyBorder(4, 1, 4, 1));
        box.border = BorderFactory.createLineBorder(Color.BLUE)
        for (component in components) {
            box.add(component)
        }
        return box
    }

    override fun newSplit(items: List<JComponent>, subLayer: JComponent): JComponent {
        val box = Box.createHorizontalBox()
        box.border = BorderFactory.createLineBorder(Color.YELLOW)
        box.add(itemsBox(items))
        box.add(subLayer)
        return box
    }

    override fun newSplit(left: JComponent, right: JComponent) =
        Box.createHorizontalBox().apply {
            setBorder(BorderFactory.createLineBorder(JBColor.YELLOW))
            add(left)
            add(right)
        }

    override fun newLayer(directDeps: JComponent?, sharedDeps: JComponent?): JComponent {
        val box = Box.createVerticalBox()
        box.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
        if (directDeps != null) box.add(directDepsBox(directDeps))
        if (sharedDeps != null) box.add(sharedDepsBox(sharedDeps))
        return box
    }

    fun itemsBox(items: List<JComponent>): JComponent {
        val box = Box.createVerticalBox()
        box.border = BorderFactory.createLineBorder(Color.PINK)
        for (component in items) {
            box.add(component)
        }
        return box
    }

    fun directDepsBox(contents: JComponent?): JComponent {
        val box = Box.createVerticalBox()
        //        box.setBackground(Color.GREEN);
        box.border = BorderFactory.createLineBorder(Color.GREEN)
        //        box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        box.add(contents)
        return box
    }

    fun sharedDepsBox(contents: JComponent?): JComponent {
        val box = Box.createVerticalBox()
        box.border = BorderFactory.createLineBorder(Color.RED)
        //        box.setBorder(BorderFactory.createLoweredBevelBorder());
        box.add(contents)
        return box
    }
}
