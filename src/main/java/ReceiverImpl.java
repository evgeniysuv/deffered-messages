import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import util.PairComparator;

import java.lang.Thread.State;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Component
public class ReceiverImpl implements Runnable, Receiver<Boolean> {
    private static Logger log = Logger.getLogger(ReceiverImpl.class);

    private static int INITIAL_QUEUE_CAPACITY = 16;
    private PriorityBlockingQueue<ImmutablePair<LocalDateTime, Callable<Boolean>>> queue =
            new PriorityBlockingQueue<>(INITIAL_QUEUE_CAPACITY, new PairComparator());

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
    private CopyOnWriteArraySet results = new CopyOnWriteArraySet();
    private volatile boolean isActive = true;
    private Thread thread;

    @Override
    public void start() {
        if (thread != null && thread.getState().equals(State.RUNNABLE)) {
            log.warn("Receiver already started");
        } else if (thread == null || thread.getState().equals(State.NEW)) {
            thread = new Thread(this, "Message receiver");
            thread.start();
            log.info("Message Receiver online");
        } else
            throw new IllegalStateException("Receiver terminated");
    }

    @Override
    public void receive(LocalDateTime dateTime, Callable<Boolean> task) {
        queue.add(new ImmutablePair<>(dateTime, task));
    }

    @Override
    public void run() {
        while (isActive) {
            ImmutablePair<LocalDateTime, Callable<Boolean>> pair;
            try {
                System.out.println("Receiving...");
                pair = queue.take();
                System.out.println("Pair received");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ScheduledFuture<Boolean> future = executor.schedule(pair.getRight(), 1, TimeUnit.SECONDS);
            if (future.isDone()) {
                try {
                    results.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void shutdown() {
        System.out.println("Shutdown...");
        isActive = false;
        executor.shutdown();
        thread.interrupt();
    }
}
