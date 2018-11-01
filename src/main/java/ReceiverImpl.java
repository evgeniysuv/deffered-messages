import org.apache.commons.lang3.tuple.ImmutablePair;
import util.PairComparator;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public class ReceiverImpl implements Receiver<Boolean> {
    private static int INITIAL_QUEUE_CAPACITY = 16;
    private PriorityBlockingQueue<ImmutablePair<LocalDateTime, Callable<Boolean>>> blockingQueue =
            new PriorityBlockingQueue<>(INITIAL_QUEUE_CAPACITY, new PairComparator());

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
    private CopyOnWriteArraySet results = new CopyOnWriteArraySet();

    public ReceiverImpl() {
        ImmutablePair<LocalDateTime, Callable<Boolean>> pair = blockingQueue.peek();
    }

    @Override
    public void receive(LocalDateTime dateTime, Callable<Boolean> task) {
        blockingQueue.add(new ImmutablePair<>(dateTime, task));
    }

    public void run() {
        while (!blockingQueue.isEmpty()) {
            ImmutablePair<LocalDateTime, Callable<Boolean>> pair = blockingQueue.peek();
            ScheduledFuture<Boolean> future = executor.schedule(pair.getRight(), 3, TimeUnit.MILLISECONDS);
            if (future.isDone()) {
                try {
                    results.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
