package semicontinuity.idea.code.analyzer.graph

import org.junit.jupiter.api.Test
import semicontinuity.idea.code.analyzer.graph.viewModel.swing.SwingViewFactory
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.WindowConstants

internal class DAGraphViewRendererSwingTest

    : DAGraphImplTestData4, DAGraphImplTestData5, DAGraphImplTestData6, DAGraphImplTestData7 {
    @Test
    @Throws(InterruptedException::class)
    fun render4() {
        show(exampleGraph4())
    }

    @Test
    @Throws(InterruptedException::class)
    fun render5() {
        show(exampleGraph5())
    }

    @Test
    @Throws(InterruptedException::class)
    fun render6() {
        // WEIRD
        // See platform/services/sd/internal/storage
        // Correctly rendered here.
        // Incorrectly in GoLand.

        show(exampleGraph6())
    }

    @Test
    @Throws(InterruptedException::class)
    fun render7() {
        show(exampleGraph7())
    }

    @Throws(InterruptedException::class)
    private fun show(graph: DAGraph<String>) {
        val frame = frame(render(graph))
        while (frame.isVisible) Thread.sleep(100)
    }

    companion object {
        init {
            System.setProperty("sun.java2d.uiScale", "4")
        }

        private fun render(graph: DAGraph<String>): JComponent =
            DAGraphViewRenderer1(
                SwingViewFactory(), { id: String -> id },
                { s: String -> s },
            ).render(graph)

        private fun frame(contents: JComponent): JFrame {
            val f = JFrame("test")
            f.contentPane = contents
            f.pack()
            f.setLocationRelativeTo(null)
            f.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            f.isVisible = true
            return f
        }
    }
}
