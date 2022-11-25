package semicontinuity.idea.code.analyzer.graph.viewModel.ide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import com.intellij.psi.PsiElement;

public class IdeButton extends JButton implements ActionListener {
    protected PsiElement psiElement;

    protected IdeButton(final PsiElement element, String text, Icon icon) {
        super(text, icon);
        psiElement = element;
    }

    public void actionPerformed(ActionEvent e) {
    }
}
