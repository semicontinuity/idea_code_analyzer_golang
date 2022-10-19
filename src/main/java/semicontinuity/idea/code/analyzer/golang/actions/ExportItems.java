package semicontinuity.idea.code.analyzer.golang.actions;

import java.util.Collection;

import com.goide.psi.GoFile;
import com.goide.psi.GoFunctionDeclaration;
import com.goide.psi.GoMethodDeclaration;
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

        for (GoMethodDeclaration method : goFile.getMethods()) {
            var receiver = method.getReceiver();
            System.out.println("=================");
            System.out.println("receiver = " + receiver);
            if (receiver != null) {
                System.out.println(receiver.getType().getText() + '.' + method.getName());
            }
        }

        System.out.println();

        Collection<? extends GoFunctionDeclaration> functions = goFile.getFunctions();
        for (GoFunctionDeclaration function : functions) {
            System.out.println("function = " + function);
        }
    }
}
