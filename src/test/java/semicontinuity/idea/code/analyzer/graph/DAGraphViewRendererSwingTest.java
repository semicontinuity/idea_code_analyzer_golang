package semicontinuity.idea.code.analyzer.graph;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.jupiter.api.Test;
import semicontinuity.idea.code.analyzer.graph.viewModel.swing.SwingViewFactory;

class DAGraphViewRendererSwingTest implements DAGraphImplTestData1, DAGraphImplTestData3, DAGraphImplTestData4 {
    static {
        System.setProperty("sun.java2d.uiScale", "4");
    }

    @Test
    void render4() throws InterruptedException {
        render(exampleGraph4());
    }

    private void render(DAGraph<String> graph) throws InterruptedException {
        var renderer = new DAGraphViewRenderer<>(graph, new SwingViewFactory(), (String id) -> id);
        var frame = frame(renderer.render());
//        Thread.sleep(10000);
        while (frame.isVisible()) Thread.sleep(100);
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
