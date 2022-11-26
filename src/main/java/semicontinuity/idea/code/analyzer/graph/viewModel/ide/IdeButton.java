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
import semicontinuity.idea.code.analyzer.golang.Node;

public class IdeButton extends JButton implements ActionListener {
    private final Node node;
    private final Consumer<Node> actionConsumer;

    private final PsiElement psiElement;

    private final Color regularBackground;


    protected IdeButton(final PsiElement element, String text, Icon icon, Node node, Consumer<Node> actionConsumer) {
        super(text, icon);
        addActionListener(this);
        setBorder(BorderFactory.createEmptyBorder());
        setUI(new BasicButtonUI());

        this.node = node;
        this.actionConsumer = actionConsumer;
        psiElement = element;
        regularBackground = getBackground();
    }

    public void actionPerformed(ActionEvent e) {
        actionConsumer.accept(node);
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


    public void select(NodeHighlightingKind kind) {
        setBackground(background(kind));
    }

    public void deselect() {
        setBackground(regularBackground);
    }

    private Color background(NodeHighlightingKind kind) {
        if (kind == NodeHighlightingKind.SUBJECT) {
            return Color.GREEN;
        } else if (kind == NodeHighlightingKind.CALLEE) {
            return Color.BLUE;
        } else {
            return Color.ORANGE;
        }
    }
}
