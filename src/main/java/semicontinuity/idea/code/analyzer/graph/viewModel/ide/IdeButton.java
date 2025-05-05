package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

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
import semicontinuity.idea.code.analyzer.golang.Member;

public class IdeButton extends JButton implements ActionListener {
    private final Member vertex;
    private final Consumer<Member> actionConsumer;

    private final PsiElement psiElement;

    private final Color regularBackground;


    protected IdeButton(final PsiElement element, String text, Icon icon, Member vertex, Consumer<Member> actionConsumer) {
        super(text, icon);
        addActionListener(this);
        setBorder(BorderFactory.createEmptyBorder());
        setUI(new BasicButtonUI());

        this.vertex = vertex;
        this.actionConsumer = actionConsumer;
        psiElement = element;
        regularBackground = getBackground();
    }

    public void actionPerformed(ActionEvent e) {
        actionConsumer.accept(vertex);
        navigateToSource();
    }

    private void navigateToSource() {
        System.out.println("ACTION: " + psiElement);
        TextRange textRange = psiElement.getTextRange();
        final int endOffset = textRange.getStartOffset();
        final Project project = psiElement.getProject();
        final VirtualFile virtualFile = psiElement.getContainingFile().getVirtualFile();
        FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, virtualFile, endOffset), true);
    }


    public void select(MemberHighlightingKind kind) {
        setBackground(background(kind));
    }

    public void deselect() {
        setBackground(regularBackground);
    }

    private Color background(MemberHighlightingKind kind) {
        if (kind == MemberHighlightingKind.SUBJECT) {
            return new Color(192, 255, 192);
        } else if (kind == MemberHighlightingKind.CALLEE) {
            return new Color(192, 192, 255);
        } else {
            return new Color(255, 192, 192);
        }
    }
}
