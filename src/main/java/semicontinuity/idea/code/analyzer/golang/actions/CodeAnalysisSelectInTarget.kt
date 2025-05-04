package semicontinuity.idea.code.analyzer.golang.actions

import com.intellij.ide.SelectInContext
import com.intellij.ide.SelectInTarget
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiElement
import kotlinx.coroutines.Runnable
import semicontinuity.idea.code.analyzer.golang.toolwindow.ToolWindow

class CodeAnalysisSelectInTarget : SelectInTarget {
    override fun canSelect(context: SelectInContext): Boolean {
        // Enable for any file
        return context.virtualFile != null
    }

    override fun selectIn(context: SelectInContext, requestFocus: Boolean) {
        val file = context.virtualFile
        val project = context.project

        if (file == null || project == null) {
            return
        }

        val windowManager = ToolWindowManager.getInstance(context.project)
        if (requestFocus) {
            val window = windowManager.getToolWindow(this.toolWindowId)
            window?.activate(Runnable {
                val toolWindowGraph = project.getComponent(ToolWindow.COMPONENT_NAME) as? ToolWindow
                toolWindowGraph?.repaintUI()

                (context.selectorInFile as? PsiElement)?.let { toolWindowGraph?.selectPsiElement(it) }
            })
        }
    }

    override fun getToolWindowId(): String {
        return ToolWindow.TOOL_WINDOW_ID
    }

    override fun getMinorViewId(): String? {
        return null
    }

    override fun getWeight(): Float {
        return 9.0f
    }

    override fun toString(): String {
        return "Code Analysis"
    }
}
