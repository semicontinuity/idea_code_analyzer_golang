package semicontinuity.idea.code.analyzer.golang.actions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goide.psi.GoFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import semicontinuity.idea.code.analyzer.golang.Node;
import semicontinuity.idea.code.analyzer.graph.DAGraph;

import static semicontinuity.idea.code.analyzer.golang.StructureFiller.fillCallGraph;


public class DumpStructCallGraph extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        var goFile = (GoFile) anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (goFile == null) {
            return;
        }
        DAGraph<Node> structure = fillCallGraph(goFile);
        writeDebugGraph(toDebugGraph(structure));
    }

    private ArrayList<List<?>> toDebugGraph(DAGraph<Node> s) {
        var out = new HashSet<List<?>>();
        s.forEachEdge((from, to) -> {
            if (!from.getQualifier().equals(to.getQualifier())) {
                out.add(List.of(from.getQualifier(), to.getQualifier()));
            }
        });
        return new ArrayList<>(out);
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
