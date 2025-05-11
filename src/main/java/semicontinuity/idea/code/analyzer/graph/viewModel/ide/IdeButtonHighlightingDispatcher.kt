package semicontinuity.idea.code.analyzer.graph.viewModel.ide

import com.goide.psi.GoFunctionDeclaration
import com.goide.psi.GoMethodDeclaration
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import semicontinuity.idea.code.analyzer.golang.Member
import semicontinuity.idea.code.analyzer.golang.typeName
import semicontinuity.idea.code.analyzer.graph.DAGraph
import java.util.function.Consumer

class IdeButtonHighlightingDispatcher(private val callGraph: DAGraph<Member>) :
    Consumer<Member> {
    private val mapping = HashMap<Member, IdeButton>()

    fun register(vertex: Member, button: IdeButton) {
        mapping[vertex] = button
    }

    override fun accept(vertex: Member) {
        deselectAll()
        mapping[vertex]!!.select(MemberHighlightingKind.SUBJECT)

        callGraph.forEachUpstreamVertex(vertex) { caller: Member ->
            mapping[caller]!!
                .select(MemberHighlightingKind.CALLER)
        }
        callGraph.forEachDownstreamVertex(vertex) { callee: Member ->
            mapping[callee]!!
                .select(MemberHighlightingKind.CALLEE)
        }
    }

    fun deselectAll() {
        mapping.values.forEach(Consumer { obj: IdeButton -> obj.deselect() })
    }

    fun selectPsiElement(psiElement: PsiElement) {
        vertex(psiElement)?.let { accept(it) }
    }

    fun vertex(psiElement: PsiElement): Member? {
        val function = psiElement.parentOfType<GoFunctionDeclaration>()
        if (function != null) {
            return Member("", function.name!!, psiElement)
        }

        val method = psiElement.parentOfType<GoMethodDeclaration>()
        if (method != null) {
            val receiver = method.getReceiver()
            if (receiver != null) {
                return Member(
                    typeName(receiver),
                    method.name!!,
                    psiElement
                )
            }
        }

        return null
    }
}
