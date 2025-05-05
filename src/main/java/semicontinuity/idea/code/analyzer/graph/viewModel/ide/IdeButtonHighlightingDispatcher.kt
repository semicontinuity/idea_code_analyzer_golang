package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import com.goide.psi.GoFile
import com.goide.psi.GoFunctionDeclaration
import com.goide.psi.GoMethodDeclaration
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.parentOfType
import semicontinuity.idea.code.analyzer.golang.Node
import semicontinuity.idea.code.analyzer.golang.typeName
import semicontinuity.idea.code.analyzer.graph.DAGraph
import java.util.function.Consumer

class IdeButtonHighlightingDispatcher(private val callGraph: DAGraph<Node>) :
    Consumer<Node> {
    private val mapping = HashMap<Node, IdeButton>()

    fun register(node: Node, button: IdeButton) {
        mapping[node] = button
    }

    override fun accept(node: Node) {
        deselectAll()
        mapping[node]!!.select(NodeHighlightingKind.SUBJECT)

        callGraph.forEachUpstreamNode(node) { caller: Node ->
            mapping[caller]!!
                .select(NodeHighlightingKind.CALLER)
        }
        callGraph.forEachDownstreamNode(node) { callee: Node ->
            mapping[callee]!!
                .select(NodeHighlightingKind.CALLEE)
        }
    }

    fun deselectAll() {
        mapping.values.forEach(Consumer { obj: IdeButton -> obj.deselect() })
    }

    fun selectPsiElement(psiElement: PsiElement) {
        node(psiElement)?.let { accept(it) }
    }

    fun node(psiElement: PsiElement): Node? {
        val function = psiElement.parentOfType<GoFunctionDeclaration>()
        if (function != null) {
            return Node("", function.name, psiElement)
        }

        val method = psiElement.parentOfType<GoMethodDeclaration>()
        if (method != null) {
            val receiver = method.getReceiver()
            if (receiver != null) {
                return Node(typeName(receiver), method.name, psiElement)
            }
        }

        return null
    }
}
