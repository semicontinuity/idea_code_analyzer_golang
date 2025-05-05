package semicontinuity.idea.code.analyzer.util;

import java.io.Closeable;
import java.io.IOException;

import semicontinuity.idea.code.analyzer.golang.Member;

public class Context implements Closeable {

    public final CloseableConsumer<String> log = new Dumper();

    @Override
    public void close() throws IOException {
        log.close();
    }


    public void logEdge(Member from, Member to) {
        log.accept("        EDGE " + from.getQualifier() + '.' + from.getName() + " -> " + to.getQualifier() + "." + to.getName());
    }
}
