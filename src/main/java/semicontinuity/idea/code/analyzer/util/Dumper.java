package semicontinuity.idea.code.analyzer.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jetbrains.annotations.NotNull;

public class Dumper implements CloseableConsumer<String> {
    final PrintWriter printWriter;

    public Dumper() {
        printWriter = new PrintWriter(newFileWriter());
    }

    @NotNull
    private FileWriter newFileWriter() {
        try {
            return new FileWriter(
                    System.getProperty("user.home") + "/tasks/analyzer/" + System.currentTimeMillis() + ".log");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        printWriter.flush();
        printWriter.close();
    }

    @Override
    public void accept(String s) {
        printWriter.println(s);
    }
}
