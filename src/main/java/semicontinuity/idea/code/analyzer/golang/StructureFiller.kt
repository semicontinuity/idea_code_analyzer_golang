package semicontinuity.idea.code.analyzer.golang

import com.goide.psi.GoFile
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

        val interfaces = goFile.parent?.let { goInterfaceTypes(findGoFiles(it)) } ?: listOf()
        println("############################## interfaces = ${interfaces.size}")

        val structure = DAGraphImpl<Member>()
        context.log.accept("")
        context.log.accept("")
        context.log.accept("")
        context.log.accept("Populating entities")
        process(
            goFile.parent, structure,
            { obj: GoFileScanner -> obj.registerEntities() }, context
        )

        context.log.accept("")
        context.log.accept("")
        context.log.accept("")
        context.log.accept("Populating calls")
        process(
            goFile.parent, structure,
            { obj: GoFileScanner -> obj.scanCalls() }, context
        )

        return structure
    }

    private fun process(
        dir: PsiDirectory?,
        structure: DAGraph<Member>,
        sink: Consumer<GoFileScanner>,
        context: Context
    ) {
        if (dir == null) return
        val files = dir.files
        for (file in files) {
            if (file is GoFile) {
                context.log.accept("")
                context.log.accept("  Processing file " + file.getName())
                val goFileScanner = GoFileScanner(
                    file, context,
                    { vertex: Member -> structure.addVertex(vertex) },
                    { src: Member, dst: Member -> structure.addEdge(src, dst) })
                sink.accept(goFileScanner)
            }
        }
    }
}
