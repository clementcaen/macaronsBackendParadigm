package fr.clementjaminion.macaronsbackend.config;

import java.util.function.Consumer;

public class ExceptionUtil {
    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws Exception;
    }

    public static <T> Consumer<T> wrap(ThrowingConsumer<T> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}

