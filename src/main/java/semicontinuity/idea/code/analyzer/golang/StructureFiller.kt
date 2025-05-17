package semicontinuity.idea.code.analyzer.golang

import com.goide.psi.GoFile
import com.goide.psi.GoInterfaceType
import com.intellij.psi.PsiDirectory
import semicontinuity.idea.code.analyzer.golang.logic.GoFileScanner
import semicontinuity.idea.code.analyzer.golang.logic.findGoFiles
import semicontinuity.idea.code.analyzer.golang.logic.goInterfaceTypes
import semicontinuity.idea.code.analyzer.graph.DAGraph
import semicontinuity.idea.code.analyzer.graph.DAGraphImpl
import semicontinuity.idea.code.analyzer.util.Context
import java.util.function.Consumer

object StructureFiller {
    @JvmStatic
    fun fillCallGraph(goFile: GoFile): DAGraph<Member> {
        val context = Context()

        val interfaces: List<GoInterfaceType> = goFile.parent?.let { goInterfaceTypes(findGoFiles(it)) } ?: listOf()

        val structure = DAGraphImpl<Member>()
        context.log.accept("")
        context.log.accept("")
        context.log.accept("")
        context.log.accept("Populating entities")
        process(
            goFile.parent, structure,
            context, interfaces
        ) { obj: GoFileScanner -> obj.registerEntities() }

        context.log.accept("")
        context.log.accept("")
        context.log.accept("")
        context.log.accept("Populating calls")
        process(
            goFile.parent, structure,
            context, interfaces
        ) { obj: GoFileScanner -> obj.scanCalls() }

        return structure
    }

    private fun process(
        dir: PsiDirectory?,
        structure: DAGraph<Member>,
        context: Context,
        interfaces: List<GoInterfaceType>,
        sink: Consumer<GoFileScanner>,
    ) {
        if (dir == null) return
        val files = dir.files
        for (goFile in files) {
            if (goFile is GoFile) {
                context.log.accept("")
                context.log.accept("  Processing file " + goFile.getName())
                val goFileScanner = GoFileScanner(
                    goFile, context,
                    { vertex: Member -> structure.addVertex(vertex) },
                    { src: Member, dst: Member -> structure.addEdge(src, dst) },
                    // PsiTreeUtil.collectElementsOfType(goFile, GoInterfaceType::class.java)
                    interfaces,
                )
                sink.accept(goFileScanner)
            }
        }
    }
}
