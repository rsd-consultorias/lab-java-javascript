package br.com.rsdconsultoria.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe para gerar identificadores semelhantes aos números de pedidos da
 * Amazon.
 * Os identificadores incluem um prefixo, informações temporais e uma parte
 * aleatória alfanumérica.
 */
public class AmazonStyleOrderIDGenerator {
    private static final SecureRandom random = new SecureRandom();

    /**
     * Gera um identificador único no formato "PREFIX-timestamp-randomString".
     *
     * @return Uma string representando o identificador gerado.
     *         Exemplo: "AMZ-20250304144215-X7F2K9".
     */
    public static String generateOrderID() {
        // Prefixo (exemplo: "AMZ")
        String prefix = "AMZ";

        // Data no formato ano-mês-dia-hora-minuto-segundo
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);

        // Parte aleatória (6 caracteres alfanuméricos)
        String randomPart = generateRandomAlphanumeric(6);

        // Combinar todas as partes no formato final
        return String.format("%s-%s-%s", prefix, timestamp, randomPart);
    }

    /**
     * Gera uma string alfanumérica aleatória de um tamanho especificado.
     *
     * @param length O tamanho da string aleatória.
     * @return Uma string alfanumérica aleatória.
     */
    private static String generateRandomAlphanumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }
}
