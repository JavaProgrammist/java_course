package ru.otus.handler;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.InfoAboutExceptionInAction;
import ru.otus.model.Message;
import ru.otus.listener.Listener;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.Processor;
import ru.otus.processor.ProcessorWithException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ComplexProcessorTest {

    private final static LocalDateTime TIME_WITH_EVEN_SECONDS = LocalDateTime.of(2021, 12, 13, 1, 1, 10);
    private final static LocalDateTime TIME_WITH_ODD_SECONDS = LocalDateTime.of(2021, 12, 13, 1, 1, 11);

    @Test
    @DisplayName("Тестируем вызовы процессоров")
    void handleProcessorsTest() {
        //given
        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
        });

        //when
        var result = complexProcessor.handle(message);

        //then
        verify(processor1).process(message);
        verify(processor2).process(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        //given
        var message = new Message.Builder(1L).field8("field8").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        //when
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));

        //then
        verify(processor1, times(1)).process(message);
        verify(processor2, never()).process(message);
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        //given
        var message = new Message.Builder(1L).field9("field9").build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), (ex) -> {
        });

        complexProcessor.addListener(listener);

        //when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        //then
        verify(listener, times(1)).onUpdated(message);
    }

    private InfoAboutExceptionInAction processMessageByProcessorWithException(DateTimeProvider dateTimeProvider,
                                                                              Message message) {
        var processor = new ProcessorWithException(dateTimeProvider);
        LocalDateTime processStartTime = dateTimeProvider.getDate();
        boolean isExceptionThrown = false;
        try {
            processor.process(message);
        } catch (Exception e) {
            isExceptionThrown = true;
        }
        return new InfoAboutExceptionInAction(processStartTime, isExceptionThrown);
    }

    @Test
    @DisplayName("Тестируем вызор процессора, бросающего исключение")
    void handleProcessorWithExceptionTest() {
        var message = new Message.Builder(1).build();
        InfoAboutExceptionInAction infoAboutExceptionInAction =
                processMessageByProcessorWithException(() -> TIME_WITH_EVEN_SECONDS, message);
        assertThat(infoAboutExceptionInAction).matches(InfoAboutExceptionInAction::isExceptionThrown);
        infoAboutExceptionInAction =
                processMessageByProcessorWithException(() -> TIME_WITH_ODD_SECONDS, message);
        assertThat(infoAboutExceptionInAction).matches(item -> !item.isExceptionThrown());
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}