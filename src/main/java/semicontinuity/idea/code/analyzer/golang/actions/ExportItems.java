package semicontinuity.idea.code.analyzer.golang.actions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goide.psi.GoFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;


public class ExportItems extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        var goFile = (GoFile) anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (goFile == null) {
            return;
        }
        var structure = new Structure();
        System.out.println("Populating entities");
        process(goFile.getParent(), structure, GoFileScanner::registerEntities);
        System.out.println("Populating calls");
        process(goFile.getParent(), structure, GoFileScanner::scanCalls);
        writeDebugGraph(toDebugGraph(structure));
    }

    private static void process(PsiDirectory dir, Structure structure, Consumer<GoFileScanner> f) {
        if (dir == null) return;

        var files = dir.getFiles();
        for (PsiFile file : files) {
            if (file instanceof GoFile) {
                System.out.println("######## Processing file " + file.getName());
                GoFileScanner goFileScanner = new GoFileScanner(((GoFile) file), structure);
                f.accept(goFileScanner);
            }
        }
    }

    private ArrayList<List<?>> toDebugGraph(Structure s) {
        var out = new ArrayList<List<?>>();
        s.forEachCall((from, to) -> out.add(List.of(from.toString(), to.toString())));
        return out;
    }

    private void writeDebugGraph(ArrayList<List<?>> o) {
        try {
            try (var w = new FileWriter(System.getProperty("user.home") + "/tasks/analyzer/" + System.currentTimeMillis() +
                    ".json")) {
                new ObjectMapper().writeValue(w, o);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
