package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.psi.PsiElement
import semicontinuity.idea.code.analyzer.golang.Member
import java.awt.Color
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.function.Consumer
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.plaf.basic.BasicButtonUI

class IdeButton(
    element: PsiElement,
    text: String?,
    icon: Icon?,
    vertex: Member,
    actionConsumer: Consumer<Member>
) :
    JButton(text, icon), ActionListener {
    private val vertex: Member
    private val actionConsumer: Consumer<Member>

    private val psiElement: PsiElement

    private val regularBackground: Color

    init {
        addActionListener(this)
        border = BorderFactory.createEmptyBorder()
        setUI(BasicButtonUI())

        this.vertex = vertex
        this.actionConsumer = actionConsumer
        psiElement = element
        regularBackground = background
    }

    override fun actionPerformed(e: ActionEvent) {
        actionConsumer.accept(vertex)
        navigateToSource()
    }

    private fun navigateToSource() {
        println("ACTION: $psiElement")
        val textRange = psiElement.textRange
        val endOffset = textRange.startOffset
        val project = psiElement.project
        val virtualFile = psiElement.containingFile.virtualFile
        FileEditorManager.getInstance(project).openTextEditor(OpenFileDescriptor(project, virtualFile, endOffset), true)
    }

    fun select(kind: MemberHighlightingKind) {
        background = background(kind)
    }

    fun deselect() {
        background = regularBackground
    }

    private fun background(kind: MemberHighlightingKind) =
        if (kind == MemberHighlightingKind.SUBJECT) {
            Color(192, 255, 192)
        } else if (kind == MemberHighlightingKind.CALLEE) {
            Color(192, 192, 255)
        } else {
            Color(255, 192, 192)
        }
}
