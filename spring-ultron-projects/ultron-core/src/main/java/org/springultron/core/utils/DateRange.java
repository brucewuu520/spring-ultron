package org.springultron.core.utils;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 计算两个日期之间的所有日期
 *
 * @author brucewuu
 * @date 2019/11/5 14:10
 */
public class DateRange implements Iterable<LocalDate> {
    /**
     * 开始日期
     */
    private final LocalDate startDate;
    /**
     * 截止日期
     */
    private final LocalDate endDate;

    public static DateRange of(LocalDate startDate, LocalDate endDate) {
        return new DateRange(startDate, endDate);
    }

    private DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @NonNull
    @Override
    public Iterator<LocalDate> iterator() {
        return stream().iterator();
    }

    public Stream<LocalDate> stream() {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1);
    }

    public List<LocalDate> toList() {
        return stream().collect(Collectors.toList());
    }

    public void consumer(Consumer<Stream<LocalDate>> consumer) {
        consumer.accept(stream());
    }
}