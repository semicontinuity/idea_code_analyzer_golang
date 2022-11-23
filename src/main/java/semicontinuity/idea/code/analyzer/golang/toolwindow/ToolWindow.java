package semicontinuity.idea.code.analyzer.golang.toolwindow;

import java.awt.BorderLayout;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.goide.psi.GoFile;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.UIUtil;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import semicontinuity.idea.code.analyzer.golang.StructureSplitter;
import semicontinuity.idea.code.analyzer.graph.DAGraph;
import semicontinuity.idea.code.analyzer.graph.DAGraphImpl;

import static semicontinuity.idea.code.analyzer.golang.StructureFiller.fillStructure;

@SuppressWarnings({"HardCodedStringLiteral"})
public class ToolWindow implements ProjectComponent {
//    static {
//        BasicConfigurator.configure();
//    }
    private GoFile lastGoFile;

    @SuppressWarnings({"HardCodedStringLiteral"})
    public static final String TOOL_WINDOW_ID = "Code Analysis";
    public static final Logger LOGGER = Logger.getLogger(ToolWindow.class);


    private Project myProject;
    private JPanel myContentPanel;

    private boolean includeConstructors;


    @SuppressWarnings({"HardCodedStringLiteral", "StringConcatenation", "MagicCharacter", "ObjectToString"})
    public ToolWindow(final Project project) {
        LOGGER.debug("NEW PLUGIN" + ' ' + this + ' ' + project);
        myProject = project;
    }

    public void projectOpened() {
        initToolWindow();
    }

    public void projectClosed() {
        unregisterToolWindow();
    }

    public final void initComponent() {
    }

    public final void disposeComponent() {
    }

    public String getComponentName() {
        return "Cat.Plugin";
    }



    private void initToolWindow() {
        final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
        toolWindowManager.registerToolWindow(
                TOOL_WINDOW_ID, toolWindowContent(), ToolWindowAnchor.RIGHT);

//        FileEditorManager fileEditorManager = FileEditorManager.getInstance (myProject);
//        fileEditorManager.addFileEditorManagerListener (this);
    }


    private JPanel toolWindowContent() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(myContentPanel()), BorderLayout.CENTER);
        panel.add(controlPanel(panel), BorderLayout.NORTH);

        return panel;
    }

    private JPanel controlPanel(final JPanel pluginPanel) {
        JCheckBox constructors = new JCheckBox("constructors");

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(resetButton(pluginPanel), BorderLayout.EAST);
        panel.add(repaintButton(pluginPanel, constructors), BorderLayout.CENTER);
        panel.add(constructors, BorderLayout.WEST);
        return panel;
    }


    private JButton repaintButton(final JPanel panel, JCheckBox constructors) {
        final JButton button = new JButton("Repaint!");
        //noinspection HardcodedFileSeparator
        button.setIcon(new ImageIcon(Objects.requireNonNull(ToolWindow.class.getResource("/actions/refresh.png"))));
        button.addActionListener(
                e -> {
                    includeConstructors = constructors.isSelected();
                    updateUI();
                    panel.invalidate();   // TODO: make it work...
                    panel.validate();   // TODO: make it work...
                });
        return button;
    }

    private JButton resetButton(final JPanel panel) {
        final JButton button = new JButton("Reset");

        button.addActionListener(
                e -> SwingUtilities.invokeLater(() -> {
                    LOGGER.warn("DESELECT");
                    panel.invalidate();   // TODO: make it work...
                    panel.validate();   // TODO: make it work...
                }));
        return button;
    }


    private JPanel myContentPanel() {
        final JPanel myContentPanel = new JPanel();
        myContentPanel.setLayout(new BorderLayout());
        myContentPanel.setBackground(UIUtil.getTreeTextBackground());
        this.myContentPanel = myContentPanel;

        return myContentPanel;
    }


    public void updateUI() {
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(myProject);
        final Editor selectedTextEditor = fileEditorManager.getSelectedTextEditor();
        if (selectedTextEditor == null) return; // TODO: we can track notifications from EditorManager

        updateUI(selectedTextEditor.getDocument());
    }


    private void updateUI(final Document document) {
        final PsiDocumentManager psiDocMgr = PsiDocumentManager.getInstance(myProject);
        final PsiFile psiFile = psiDocMgr.getCachedPsiFile(document);
        if (psiFile == null) return;

        if (psiFile instanceof GoFile) {
            updateUI((GoFile) psiFile);
        }
    }

    private void updateUI(final GoFile goFile) {
        if (goFile.equals(lastGoFile)) return;
        lastGoFile = goFile;

        myContentPanel.removeAll();
        var structure = fillStructure(goFile);
        var structGraphs = StructureSplitter.split(structure, DAGraphImpl::new);
        myContentPanel.add(structsView(structGraphs));
    }

    private JComponent structsView(Map<String, DAGraph<String>> structGraphs) {
        var verticalBox = Box.createVerticalBox();
        structGraphs.forEach((struct, structGraph) -> {
            JPanel structView = structsView(struct);
            verticalBox.add(structView);
        });
        return verticalBox;
    }

    @NotNull
    private static JPanel structsView(String struct) {
        var structView = new JPanel();
        structView.setBorder(BorderFactory.createTitledBorder(struct));
        structView.add(new JButton("TODO"));
        return structView;
    }

    private void unregisterToolWindow() {
        final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
        toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
    }
}
