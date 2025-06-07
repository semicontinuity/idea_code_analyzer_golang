package semicontinuity.idea.code.analyzer.util

import java.io.Closeable
import java.util.function.Consumer

interface CloseableConsumer<T> : Closeable, Consumer<T> {
    companion object {
        fun <T> noOp(): CloseableConsumer<T> = object : CloseableConsumer<T> {
            override fun close() {
            }

            override fun accept(t: T) {
            }
        }
    }
}
