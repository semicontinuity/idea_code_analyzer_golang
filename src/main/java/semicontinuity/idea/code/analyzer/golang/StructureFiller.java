package semicontinuity.idea.code.analyzer.golang;

import java.util.function.Consumer;

import com.goide.psi.GoFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import semicontinuity.idea.code.analyzer.graph.DAGraph;
import semicontinuity.idea.code.analyzer.graph.DAGraphImpl;
import semicontinuity.idea.code.analyzer.util.Context;

public class StructureFiller {

    public static DAGraph<Node> fillStructure(GoFile goFile) {
        Context context = new Context();

        var structure = new DAGraphImpl<Node>();
        context.log.accept("");
        context.log.accept("");
        context.log.accept("");
        context.log.accept("Populating entities");
        process(goFile.getParent(), structure, GoFileScanner::registerEntities, context);

        context.log.accept("");
        context.log.accept("");
        context.log.accept("");
        context.log.accept("Populating calls");
        process(goFile.getParent(), structure, GoFileScanner::scanCalls, context);

        return structure;
    }

    private static void process(PsiDirectory dir, DAGraph<Node> structure, Consumer<GoFileScanner> sink, Context context) {
        if (dir == null) return;
        var files = dir.getFiles();
        for (PsiFile file : files) {
            if (file instanceof GoFile) {
                context.log.accept("");
                context.log.accept("  Processing file " + file.getName());
                GoFileScanner goFileScanner = new GoFileScanner(((GoFile) file), context, structure::addNode, structure::addEdge);
                sink.accept(goFileScanner);
            }
        }
    }
}
