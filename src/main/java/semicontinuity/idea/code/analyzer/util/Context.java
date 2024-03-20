package semicontinuity.idea.code.analyzer.util;

import java.io.Closeable;
import java.io.IOException;

public class Context implements Closeable {

    public final CloseableConsumer<String> log = new Dumper();

    @Override
    public void close() throws IOException {
        log.close();
    }
}
