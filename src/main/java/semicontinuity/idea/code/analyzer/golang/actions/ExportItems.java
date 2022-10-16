package semicontinuity.idea.code.analyzer.golang.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class ExportItems extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        System.out.println("anActionEvent = " + anActionEvent);
    }
}
