import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public interface Receiver<V> {
    public void receive(LocalDateTime dateTime, Callable<V> task);
}
