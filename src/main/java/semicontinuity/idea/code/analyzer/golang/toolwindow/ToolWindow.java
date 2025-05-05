package semicontinuity.idea.code.analyzer.golang.toolwindow;

import java.awt.BorderLayout;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

import com.goide.psi.GoFile;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.UIUtil;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import semicontinuity.idea.code.analyzer.golang.Member;
import semicontinuity.idea.code.analyzer.golang.CallGraphSplitter;
import semicontinuity.idea.code.analyzer.graph.DAGraph;
import semicontinuity.idea.code.analyzer.graph.DAGraphImpl;
import semicontinuity.idea.code.analyzer.graph.DAGraphViewRenderer;
import semicontinuity.idea.code.analyzer.graph.viewModel.ide.IdeButtonHighlightingDispatcher;
import semicontinuity.idea.code.analyzer.graph.viewModel.ide.IdeViewFactory;

import static semicontinuity.idea.code.analyzer.golang.StructureFiller.fillCallGraph;

@SuppressWarnings({"HardCodedStringLiteral"})
public class ToolWindow implements ProjectComponent {

    @SuppressWarnings({"HardCodedStringLiteral"})
    public static final String TOOL_WINDOW_ID = "Code Analysis";
    public static final String COMPONENT_NAME = "Cat.Plugin";
    public static final Logger LOGGER = Logger.getLogger(ToolWindow.class);


    private final Project myProject;
    private JPanel myContentPanel;
    private IdeButtonHighlightingDispatcher ideButtonHighlightingDispatcher;


    @SuppressWarnings({"HardCodedStringLiteral", "StringConcatenation", "MagicCharacter", "ObjectToString"})
    public ToolWindow(final Project project) {
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
        return COMPONENT_NAME;
    }


    private void initToolWindow() {
        var toolWindowManager = ToolWindowManager.getInstance(myProject);
        toolWindowManager.invokeLater(
                () -> toolWindowManager.registerToolWindow(
                        TOOL_WINDOW_ID, toolWindowContent(), ToolWindowAnchor.RIGHT)
        );
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
        button.setIcon(new ImageIcon(Objects.requireNonNull(ToolWindow.class.getResource("/icons/refresh.png"))));
        button.addActionListener(
                e -> {
                    repaintUI();
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
                    ideButtonHighlightingDispatcher.deselectAll();
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


    public void repaintUI() {
        final FileEditorManager fileEditorManager = FileEditorManager.getInstance(myProject);
        final Editor selectedTextEditor = fileEditorManager.getSelectedTextEditor();
        if (selectedTextEditor == null) {
            return; // TODO: we can track notifications from EditorManager
        }

        repaintUI(selectedTextEditor.getDocument());
    }


    private void repaintUI(final Document document) {
        final PsiDocumentManager psiDocMgr = PsiDocumentManager.getInstance(myProject);
        final PsiFile psiFile = psiDocMgr.getCachedPsiFile(document);
        if (psiFile == null) {
            return;
        }

        if (psiFile instanceof GoFile) {
            repaintUI((GoFile) psiFile);
        }
    }

    private void repaintUI(final GoFile goFile) {
//        if (goFile.equals(lastGoFile)) return;
//        lastGoFile = goFile;

        myContentPanel.removeAll();
        var globalCallGraph = fillCallGraph(goFile);
        var callGraphs = CallGraphSplitter.INSTANCE.split(globalCallGraph, DAGraphImpl::new);
        ideButtonHighlightingDispatcher = new IdeButtonHighlightingDispatcher(globalCallGraph);
        myContentPanel.add(structsView(callGraphs));
    }

    private JComponent structsView(Map<String, DAGraph<Member>> structGraphs) {
        var verticalBox = Box.createVerticalBox();
        structGraphs
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach((Map.Entry<String, DAGraph<Member>> e) -> verticalBox.add(
                                structView(e.getKey(), e.getValue())
                        )
                );
        return verticalBox;
    }

    private JPanel structView(String struct, DAGraph<Member> structGraph) {
        // just pass one viewFactory?
        var viewFactory = new IdeViewFactory(ideButtonHighlightingDispatcher);

        var structView = new JPanel();
        structView.setLayout(new BorderLayout());

        structView.setBorder(BorderFactory.createRaisedBevelBorder());

        structView.add(structButton(struct), BorderLayout.NORTH);

        JComponent contents = render(structGraph, viewFactory);
        contents.setBorder(BorderFactory.createLoweredBevelBorder());
        structView.add(contents, BorderLayout.CENTER);

        return structView;
    }

    private static JButton structButton(String struct) {
        JButton button = new JButton(struct);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setUI(new BasicButtonUI());
        button.setHorizontalAlignment(SwingConstants.LEFT);
        return button;
    }

    private static JComponent render(DAGraph<Member> graph, IdeViewFactory viewFactory) {
        return new DAGraphViewRenderer<>(graph, viewFactory, Function.identity(), Member::getName).render();
    }

    private void unregisterToolWindow() {
        final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(myProject);
        toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
    }

    public void selectPsiElement(@NotNull PsiElement e) {
        ideButtonHighlightingDispatcher.selectPsiElement(e);
        myContentPanel.invalidate();
        myContentPanel.validate();
        myContentPanel.repaint();
    }
}
