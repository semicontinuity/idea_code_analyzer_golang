package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;

public class IdeButton extends JButton implements ActionListener {
    protected PsiElement psiElement;

    protected IdeButton(final PsiElement element, String text, Icon icon) {
        super(text, icon);
        addActionListener(this);
        psiElement = element;
        setBorder(BorderFactory.createEmptyBorder());
        setUI(new BasicButtonUI());
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("ACTION: " + psiElement);
        TextRange textRange = psiElement.getTextRange();
        final int endOffset = textRange.getStartOffset();
        final Project project = psiElement.getProject();
        final VirtualFile virtualFile = psiElement.getContainingFile().getVirtualFile();
        FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, virtualFile, endOffset), true);
    }
}
