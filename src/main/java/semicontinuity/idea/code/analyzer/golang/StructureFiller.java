package semicontinuity.idea.code.analyzer.golang;

import java.util.function.Consumer;

import com.goide.psi.GoFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

public class StructureFiller {

    public static Structure fillStructure(GoFile goFile) {
        var structure = new Structure();
        System.out.println("Populating entities");
        process(goFile.getParent(), structure, GoFileScanner::registerEntities);
        System.out.println("Populating calls");
        process(goFile.getParent(), structure, GoFileScanner::scanCalls);
        return structure;
    }

    private static void process(PsiDirectory dir, Structure structure, Consumer<GoFileScanner> f) {
        if (dir == null) return;

        var files = dir.getFiles();
        for (PsiFile file : files) {
            if (file instanceof GoFile) {
                System.out.println("######## Processing file " + file.getName());
                GoFileScanner goFileScanner = new GoFileScanner(((GoFile) file), structure::add, structure::addCall);
                f.accept(goFileScanner);
            }
        }
    }
}
