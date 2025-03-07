package br.com.rsdconsultoria.utils;

import java.security.SecureRandom;
import java.time.Instant;

/**
 * Classe para geração de identificadores únicos escaláveis e eficientes.
 * Este gerador utiliza timestamp, aleatoriedade segura e namespaces opcionais para garantir
 * unicidade, compactação e flexibilidade em sistemas distribuídos ou centralizados.
 */
public class ScalableUniqueIDGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Gera um identificador único no formato "NAMESPACE-timestamp-randomString".
     *         // Optional namespace or prefix for contextual differentiation
     *         String namespace = "SYS"; // Example: "SYS" for system-generated IDs
     *
     * @return Uma string representando o identificador único gerado.
     *         Exemplo: "SYS-1745023987654-AB3F2X8K".
     */
    public static String generateUniqueID(String namespace) {
        // Timestamp in nanoseconds for high precision and uniqueness
        long timestamp = Instant.now().toEpochMilli();

        // Random alphanumeric part (8 characters for compactness)
        String randomPart = generateRandomAlphanumeric(8);

        // Combine all components to form the unique ID
        return String.format("%s-%d-%s", namespace, timestamp, randomPart);
    }

    /**
     * Gera uma string aleatória alfanumérica com o comprimento especificado.
     * Utiliza uma fonte de aleatoriedade segura para garantir imprevisibilidade.
     *
     * @param length O tamanho da string aleatória.
     * @return Uma string alfanumérica aleatória de comprimento especificado.
     */
    private static String generateRandomAlphanumeric(int length) {
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(index));
        }
        return randomString.toString();
    }
}
