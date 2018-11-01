import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class MessagePusher<V> {
    private int messageCount;
    private Receiver<Boolean> receiver;

    public MessagePusher(int messageCount, @NotNull Receiver<Boolean> receiver) {
        this.messageCount = messageCount;
        this.receiver = receiver;
    }

    public void generateMessages() {
        for (int i = 1; i <= messageCount; i++) {
            int finalI = i;
            receiver.receive(LocalDateTime.now(), () -> {
                Thread.sleep(2000L);
                System.out.println("Working..." + finalI);
                return true;
            });
        }
    }
}
