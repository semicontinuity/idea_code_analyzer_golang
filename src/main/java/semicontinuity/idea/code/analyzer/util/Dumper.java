package semicontinuity.idea.code.analyzer.util;

import java.io.FileWriter;
import java.io.IOException;

public class Dumper implements CloseableConsumer<String> {
    final FileWriter writer;

    public Dumper() {
        try {
            writer = new FileWriter(
                    System.getProperty("user.home") + "/tasks/analyzer/" + System.currentTimeMillis() + ".log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public void accept(String s) {
        doWrite(s);
        doWrite(s);
    }

    private void doWrite(String s) {
        try {
            writer.write(s);
            writer.write('\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
