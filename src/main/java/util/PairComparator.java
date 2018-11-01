package util;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.concurrent.Callable;

public class PairComparator implements Comparator<ImmutablePair<LocalDateTime, Callable<Boolean>>> {
    @Override
    public int compare(ImmutablePair<LocalDateTime, Callable<Boolean>> o1,
                       ImmutablePair<LocalDateTime, Callable<Boolean>> o2) {

        return 0;
    }
}
