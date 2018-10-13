package net.stuha.messages;

import org.junit.Test;

import java.time.LocalDateTime;

public class MessageServiceImplTest {
    @Test
    public void checkRanicek() throws Exception {
        final LocalDateTime todaysMorningStart = LocalDateTime.now().withHour(5).withMinute(30);
        final LocalDateTime todaysMorningEnd = LocalDateTime.now().withHour(11).withMinute(0);

        final boolean isMorning = LocalDateTime.now().isAfter(todaysMorningStart) && LocalDateTime.now().isBefore(todaysMorningEnd);

    }

}