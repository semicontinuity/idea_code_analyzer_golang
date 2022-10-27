package semicontinuity.idea.code.analyzer.golang.actions;

import com.goide.psi.GoFile;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;


public class ExportItems extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        var goFile = (GoFile) anActionEvent.getData(CommonDataKeys.PSI_FILE);
        if (goFile == null) {
            return;
        }
        new GoFileScanner(goFile, new Structure()).scan();
    }
}
