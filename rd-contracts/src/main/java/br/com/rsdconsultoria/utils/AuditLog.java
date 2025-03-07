package br.com.rsdconsultoria.utils;

import java.time.Instant;

public class AuditLog {
    public static void info(String correlationId, String message) {
        System.out.println("%s  INFO  [%s]: %s".formatted(Instant.now(), correlationId, message));
    }

    public static void info(String correlationId, String process, String message) {
        System.out.println("%s  INFO  [%s] [%s]: %s".formatted(Instant.now(), correlationId, process.toUpperCase().replaceAll(" ", "_"), message));
    }

    public static void info(String process, String step, String correlationId, String message) {
        System.out.println("%s  INFO  [%s] [%s] [%s]: %s".formatted(Instant.now(), correlationId, process,
                step, message));
    }

    public static void warn(String correlationId, String process, String message) {
        System.out.println("%s  WARN  [%s] [%s]: %s".formatted(Instant.now(), correlationId, process.toUpperCase().replaceAll(" ", "_"), message));
    }

    public static void warn(String process, String step, String correlationId, String message) {
        System.out.println("%s  WARN  [%s] [%s] [%s]: %s".formatted(Instant.now(), correlationId, process,
                step, message));
    }

    public static void error(String process, String step, String correlationId, String message) {
        System.out.println("%s  ERROR [%s] [%s] [%s]: %s".formatted(Instant.now(), correlationId, process,
                step, message));
    }
}
