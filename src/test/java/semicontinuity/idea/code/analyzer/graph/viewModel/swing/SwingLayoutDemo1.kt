package semicontinuity.idea.code.analyzer.graph.viewModel.swing

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JFrame

class SwingLayoutDemo1 {
    private var mainFrame: JFrame? = null

    init {
        prepareGUI()
    }

    private fun prepareGUI() {
        mainFrame = JFrame("Java SWING Examples")
        mainFrame!!.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(windowEvent: WindowEvent) {
                System.exit(0)
            }
        })
        mainFrame!!.setSize(400, 400)

        mainFrame!!.add(
            Box.createVerticalBox().apply {
                add(Box.createVerticalGlue())
                add(
                    Box.createVerticalBox().apply {
                        add(JButton("Hi"))
                        add(JButton("Hi"))
                    }
                )
                add(Box.createVerticalGlue())
            }
        )
    }

    private fun demo() {
        mainFrame!!.isVisible = true
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SwingLayoutDemo1().demo()
        }
    }
}