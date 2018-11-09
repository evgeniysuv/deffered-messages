import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public interface Receiver<V> {
    void start();

    void receive(LocalDateTime dateTime, Callable<V> task);

    void shutdown();
}
