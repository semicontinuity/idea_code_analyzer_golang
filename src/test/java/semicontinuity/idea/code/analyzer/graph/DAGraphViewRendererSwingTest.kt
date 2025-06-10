package semicontinuity.idea.code.analyzer.graph;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.jupiter.api.Test;
import semicontinuity.idea.code.analyzer.graph.viewModel.swing.SwingViewFactory;

class DAGraphViewRendererSwingTest
        implements DAGraphImplTestData4, DAGraphImplTestData5, DAGraphImplTestData6, DAGraphImplTestData7 {
    static {
        System.setProperty("sun.java2d.uiScale", "4");
    }

    @Test
    void render4() throws InterruptedException {
        show(exampleGraph4());
    }

    @Test
    void render5() throws InterruptedException {
        show(exampleGraph5());
    }

    @Test
    void render6() throws InterruptedException {

        // WEIRD
        // See platform/services/sd/internal/storage
        // Correctly rendered here.
        // Incorrectly in GoLand.
        show(exampleGraph6());
    }

    @Test
    void render7() throws InterruptedException {
        show(exampleGraph7());
    }

    private void show(DAGraph<String> graph) throws InterruptedException {
        var frame = frame(render(graph));
        while (frame.isVisible()) Thread.sleep(100);
    }

    private static JComponent render(DAGraph<String> graph) {
        return new DAGraphViewRenderer<>(graph, new SwingViewFactory(), (String id) -> id, (String s) -> s).render();
    }

    private static JFrame frame(JComponent contents) {
        var f = new JFrame("test");
        f.setContentPane(contents);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setVisible(true);
        return f;
    }
}
