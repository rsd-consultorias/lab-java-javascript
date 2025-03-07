package br.com.rsdconsultoria.utils;

/**
 * Implementação do algoritmo Snowflake para geração de identificadores únicos.
 * Os identificadores são compostos por um timestamp, um ID de máquina e um número de sequência.
 */
public final class SnowflakeIDGenerator {
    private final long epoch = 1288834974657L; // Custom epoch (e.g., Twitter's epoch)
    private final long machineId;
    private final long machineIdBits = 10L;
    private final long sequenceBits = 12L;
    private final long maxMachineId = ~(-1L << machineIdBits);
    private final long maxSequence = ~(-1L << sequenceBits);

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    /**
     * Construtor para inicializar o gerador de IDs Snowflake.
     *
     * @param machineId O ID único da máquina (deve estar dentro do intervalo permitido).
     * @throws IllegalArgumentException se o machineId estiver fora do intervalo permitido.
     */
    public SnowflakeIDGenerator(long machineId) {
        if (machineId > maxMachineId || machineId < 0) {
            throw new IllegalArgumentException("Machine ID out of range");
        }
        this.machineId = machineId;
    }

    /**
     * Gera um identificador único baseado no algoritmo Snowflake.
     *
     * @return Um identificador único de 64 bits.
     * @throws RuntimeException se o relógio do sistema retroceder.
     */
    public synchronized long generateID() {
        long timestamp = currentTimestamp();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards!");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                timestamp = waitForNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return ((timestamp - epoch) << (machineIdBits + sequenceBits))
                | (machineId << sequenceBits)
                | sequence;
    }

    /**
     * Aguarda até que o próximo milissegundo esteja disponível.
     *
     * @param lastTimestamp O último timestamp gerado.
     * @return O próximo timestamp disponível.
     */
    private long waitForNextMillis(long lastTimestamp) {
        long timestamp = currentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimestamp();
        }
        return timestamp;
    }

    /**
     * Obtém o timestamp atual em milissegundos.
     *
     * @return O timestamp atual.
     */
    private long currentTimestamp() {
        return System.currentTimeMillis();
    }
}
