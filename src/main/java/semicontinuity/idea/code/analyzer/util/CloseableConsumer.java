package semicontinuity.idea.code.analyzer.util;

import java.io.Closeable;
import java.util.function.Consumer;

public interface CloseableConsumer<T> extends Closeable, Consumer<T> {
}
