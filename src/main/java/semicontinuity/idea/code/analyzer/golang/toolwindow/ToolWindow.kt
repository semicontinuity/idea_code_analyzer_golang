package semicontinuity.idea.code.analyzer.golang.toolwindow

import com.goide.psi.GoFile
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.util.ui.UIUtil
import org.apache.log4j.Logger
import semicontinuity.idea.code.analyzer.golang.CallGraphSplitter
import semicontinuity.idea.code.analyzer.golang.CallGraphSplitter.split
import semicontinuity.idea.code.analyzer.golang.StructureFiller
import semicontinuity.idea.code.analyzer.graph.DAGraphImpl
import semicontinuity.idea.code.analyzer.graph.DAGraphViewRenderer
import semicontinuity.idea.code.analyzer.graph.viewModel.ide.CoarseGraphViewFactory
import semicontinuity.idea.code.analyzer.graph.viewModel.ide.IdeButtonHighlightingDispatcher
import semicontinuity.idea.code.analyzer.graph.viewModel.ide.MembersGraphViewFactory
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.util.Objects
import java.util.function.Function
import javax.swing.Box
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingUtilities

@Suppress("HardCodedStringLiteral")
class ToolWindow @Suppress("HardCodedStringLiteral") constructor(private val myProject: Project) :
    ProjectComponent {
    private var subGraphViewFactory: MembersGraphViewFactory? = null
    private var myContentPanel: JPanel? = null
    private var ideButtonHighlightingDispatcher: IdeButtonHighlightingDispatcher? = null

    override fun projectOpened() {
        initToolWindow()
    }

    override fun projectClosed() {
        unregisterToolWindow()
    }

    override fun initComponent() {
    }

    override fun disposeComponent() {
    }

    override fun getComponentName(): String {
        return COMPONENT_NAME
    }

    private fun initToolWindow() {
        val toolWindowManager = ToolWindowManager.getInstance(myProject)
        toolWindowManager.invokeLater {
            toolWindowManager.registerToolWindow(
                TOOL_WINDOW_ID,
                toolWindowContent(),
                ToolWindowAnchor.RIGHT
            )
        }
    }

    private fun toolWindowContent(): JPanel {
        val panel = JPanel(BorderLayout())
        panel.add(JScrollPane(myContentPanel()), BorderLayout.CENTER)
        panel.add(controlPanel(panel), BorderLayout.NORTH)

        return panel
    }

    private fun controlPanel(pluginPanel: JPanel): JPanel {
        val constructors = JCheckBox("constructors")

        val panel = JPanel(BorderLayout())
        panel.add(resetButton(pluginPanel), BorderLayout.EAST)
        panel.add(repaintButton(pluginPanel, constructors), BorderLayout.CENTER)
        panel.add(constructors, BorderLayout.WEST)
        return panel
    }

    private fun repaintButton(panel: JPanel, constructors: JCheckBox): JButton {
        val button = JButton("Repaint!")
        button.icon = ImageIcon(Objects.requireNonNull(ToolWindow::class.java.getResource("/icons/refresh.png")))
        button.addActionListener { e: ActionEvent? ->
            repaintUI()
            panel.invalidate() // TODO: make it work...
            panel.validate() // TODO: make it work...
        }
        return button
    }

    private fun resetButton(panel: JPanel): JButton {
        val button = JButton("Reset")

        button.addActionListener { e: ActionEvent? ->
            SwingUtilities.invokeLater {
                LOGGER.warn("DESELECT")
                ideButtonHighlightingDispatcher!!.deselectAll()
                panel.invalidate() // TODO: make it work...
                panel.validate() // TODO: make it work...
            }
        }
        return button
    }

    private fun myContentPanel(): JPanel {
        val myContentPanel = JPanel()
        myContentPanel.layout = BorderLayout()
        myContentPanel.background = UIUtil.getTreeBackground()
        this.myContentPanel = myContentPanel
        return myContentPanel
    }

    fun repaintUI() {
        val fileEditorManager = FileEditorManager.getInstance(myProject)
        val selectedTextEditor = fileEditorManager.selectedTextEditor
            ?: return  // TODO: we can track notifications from EditorManager

        repaintUI(selectedTextEditor.document)
    }

    private fun repaintUI(document: Document) {
        val psiDocMgr = PsiDocumentManager.getInstance(myProject)
        val psiFile = psiDocMgr.getCachedPsiFile(document) ?: return

        if (psiFile is GoFile) {
            repaintUI(psiFile)
        }
    }

    private fun repaintUI(goFile: GoFile) {
//        if (goFile.equals(lastGoFile)) return;
//        lastGoFile = goFile;

        myContentPanel!!.removeAll()
        val globalCallGraph = StructureFiller.fillCallGraph(goFile)
        val dispatcher = IdeButtonHighlightingDispatcher(globalCallGraph)
        ideButtonHighlightingDispatcher = dispatcher
        subGraphViewFactory = MembersGraphViewFactory(dispatcher)
        val split = split(globalCallGraph, { DAGraphImpl() })
        myContentPanel!!.add(
            JPanel(BorderLayout()).apply {
                add(coarseGraphView(split), BorderLayout.WEST)
            },
            BorderLayout.NORTH
        )
    }

    private fun coarseGraphView(split: CallGraphSplitter.Split): JComponent {
        val coarseGraphViewFactory = CoarseGraphViewFactory(
            split.simpleVertices, split.subGraphs, subGraphViewFactory!!,
        )

        val content = DAGraphViewRenderer(
            split.coarseGraph, coarseGraphViewFactory, Function.identity()
        ) { it }.render()
        return Box.createVerticalBox().also { it.add(content) }
    }

    private fun unregisterToolWindow() {
        val toolWindowManager = ToolWindowManager.getInstance(myProject)
        toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID)
    }

    fun selectPsiElement(e: PsiElement) {
        ideButtonHighlightingDispatcher!!.selectPsiElement(e)
        myContentPanel!!.invalidate()
        myContentPanel!!.validate()
        myContentPanel!!.repaint()
    }

    companion object {
        @Suppress("HardCodedStringLiteral")
        const val TOOL_WINDOW_ID: String = "Code Analysis"
        const val COMPONENT_NAME: String = "Cat.Plugin"
        val LOGGER: Logger = Logger.getLogger(ToolWindow::class.java)
    }
}
