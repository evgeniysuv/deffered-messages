import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class Application {

    private static Map<LocalDateTime, Callable> examples = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {
        initExamples();
        System.out.println("Main thread started!");
        ReceiverImpl receiver = new ReceiverImpl();
        receiver.start();
//        System.out.println("Main thread sleeping...");
        examples.forEach(receiver::receive);
        Thread.sleep(10000);
//        receiver.shutdown();
        System.out.println("Main thread finished!");

    }

    private static void initExamples() {
        examples.put(LocalDateTime.now(), () -> "Message 1");
    }

}
