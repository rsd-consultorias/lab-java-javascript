package br.com.rsdconsultoria.model;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.rsdconsultoria.utils.AuditLog;
import br.com.rsdconsultoria.utils.HashUtil;

public class SmartContract {
    private UUID contractId;
    private Participant owner;
    private Balance balance;
    private Map<String, Balance> tokenBalances;
    private Map<String, Boolean> authorizedUsers;
    private List<String> auditLog; // Para fins de auditoria
    private Map<String, Boolean> votes; // Mapa para armazenar os votos de cada participante
    private int totalVotes; // Total de votos recebidos
    private Map<String, byte[]> signatures; // Armazena assinaturas dos participantes

    public SmartContract(Participant owner) {
        this.contractId = UUID.randomUUID();
        this.owner = owner;
        this.balance = new Balance(BigDecimal.ZERO);
        this.tokenBalances = new HashMap<>();
        this.authorizedUsers = new HashMap<>();
        this.auditLog = new ArrayList<String>();
        this.votes = new HashMap<>();
        this.totalVotes = 0;
        authorizedUsers.put(owner.getPublicKey(), true);
        this.signatures = new HashMap<>();
    }

    public UUID getContractId() {
        return contractId;
    }

    /** Função para obter o saldo do contrato */
    public Balance getBalance() {
        return balance;
    }

    /** Função para depositar fundos no contrato */
    public void deposit(Double amount) {
        if (amount > 0) {
            balance.add(BigDecimal.valueOf(amount));
            addAuditLog("Deposit", amount, owner.getPublicKey());
            AuditLog.info(this.getContractId().toString(), "DEPOSIT",
                    "Deposit successful! New balance: " + balance.getAmount());
        } else {
            AuditLog.warn(this.getContractId().toString(), "DEPOSIT", "Invalid deposit amount.");
        }
    }

    /** Função para retirar fundos do contrato */
    public void withdraw(Double amount) {
        if (amount > 0 && amount <= balance.getAmount().doubleValue()) {
            balance.subtract(BigDecimal.valueOf(amount));
            addAuditLog("Withdraw", amount, owner.getPublicKey());
            AuditLog.info(this.getContractId().toString(), "withdraw",
                    "Withdrawal successful! New balance: " + balance.getAmount());
        } else {
            addAuditLog("Withdraw REJECTED", amount, owner.getPublicKey());
            AuditLog.warn(this.getContractId().toString(), "withdraw", "Invalid withdrawal amount.");
        }
    }

    /** Função para transferir fundos para outro contrato */
    public void transfer(SmartContract recipient, Double amount) {
        if (amount > 0 && amount <= balance.getAmount().doubleValue()) {
            this.withdraw(amount);
            recipient.deposit(amount);
            addAuditLog("Transfer", amount, recipient.getOwner().getPublicKey());
            AuditLog.info(this.getContractId().toString(), "transfer", "Transfer successful!");
        } else {
            AuditLog.warn(this.getContractId().toString(), "transfer", "Invalid transfer amount.");
        }
    }

    /** Função para obter o proprietário do contrato */
    public Participant getOwner() {
        return owner;
    }

    /** Função para adicionar usuário autorizado */
    public void addAuthorizedUser(String user) {
        authorizedUsers.put(user, true);
        addAuditLog("AddAuthorizedUser", 0.0, user);
        AuditLog.info(this.getContractId().toString(), "addAuthorizedUser", "User " + user + " added as authorized.");
    }

    /** Função para remover usuário autorizado */
    public void removeAuthorizedUser(String user) {
        authorizedUsers.remove(user);
        addAuditLog("RemoveAuthorizedUser", 0.0, user);
        AuditLog.info(this.getContractId().toString(), "removeAuthorizedUser",
                "User " + user + " removed from authorized list.");
    }

    /** Função para verificar se um usuário é autorizado */
    public boolean isUserAuthorized(String user) {
        return authorizedUsers.getOrDefault(user, false);
    }

    /** Função para emitir tokens */
    @Deprecated(forRemoval = true, since = "1.0.0")
    public void issueTokens(String user, Double amount) {
        if (amount > 0) {
            Balance userBalance = tokenBalances.getOrDefault(user, new Balance(BigDecimal.ZERO));
            userBalance.add(BigDecimal.valueOf(amount));
            tokenBalances.put(user, userBalance);
            addAuditLog("IssueTokens", amount, user);
            AuditLog.info(this.getContractId().toString(), "issueTokens", "Issued " + amount + " tokens to " + user);
            AuditLog.info(this.getContractId().toString(), "issueTokens",
                    "New balance %s".formatted(getBalance().getAmount()));
        } else {
            AuditLog.warn(this.getContractId().toString(), "issueTokens", "Invalid token amount.");
        }
    }

    /** Função para queimar tokens */
    @Deprecated(forRemoval = true, since = "1.0.0")
    public void burnTokens(String user, Double amount) {
        Balance userBalance = tokenBalances.getOrDefault(user, new Balance(BigDecimal.ZERO));
        if (amount > 0 && amount <= userBalance.getAmount().doubleValue()) {
            userBalance.subtract(BigDecimal.valueOf(amount));
            tokenBalances.put(user, userBalance);
            addAuditLog("BurnTokens", amount, user);
            AuditLog.info(this.getContractId().toString(), "burnTokens", "Burned " + amount + " tokens from " + user);
        } else {
            AuditLog.warn(this.getContractId().toString(), "burnTokens", "Invalid token burn amount.");
        }
    }

    /** Função para obter saldo de tokens de um usuário */
    public Balance getTokenBalance(String user) {
        return tokenBalances.getOrDefault(user, new Balance(BigDecimal.ZERO));
    }

    /** Função de governança (votação) */
    public void vote(String publicKey, String proposal, boolean vote) {
        // Adiciona o voto do participante ao mapa de votos
        votes.put(publicKey, vote);
        totalVotes++;

        // Verifica se todos os participantes votaram
        if (totalVotes == authorizedUsers.size()) {
            int votesFor = 0;
            int votesAgainst = 0;

            // Conta os votos a favor e contra
            for (Map.Entry<String, Boolean> entry : votes.entrySet()) {
                if (entry.getValue()) {
                    votesFor++;
                } else {
                    votesAgainst++;
                }
            }

            // Determina o resultado da proposta
            String result = votesFor > votesAgainst ? "approved" : "rejected";
            addAuditLog("Vote", 0.0, publicKey);
            AuditLog.info(this.getContractId().toString(), "vote", "Proposal " + proposal + " was " + result);

            // Reseta os votos para a próxima proposta
            votes.clear();
            totalVotes = 0;
        } else {
            addAuditLog("Remaing Vote", 0.0, publicKey);
            AuditLog.warn(this.getContractId().toString(), "vote",
                    "Vote recorded. Waiting for remaining %d of %d participants to vote."
                            .formatted(authorizedUsers.size() - totalVotes, authorizedUsers.size()));
        }
    }

    /** Função de distribuição de recompensas */
    public void distributeRewards(String user, Double rewardAmount) {
        issueTokens(user, rewardAmount);
        addAuditLog("DistributeRewards", rewardAmount, user);
        AuditLog.info(this.getContractId().toString(), "distributeRewards",
                "Distributed " + rewardAmount + " tokens as rewards to " + user);
    }

    /** Função de auditoria/registro de transações */
    protected void addAuditLog(String action, Double amount, String participant) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String log = "Timestamp: " + timestamp + ", Action: " + action + ", Amount: "
                + amount + ", Participant: "
                + participant;
        String logHash = HashUtil.calculateHash(log);
        auditLog.add("[%s] %s".formatted(logHash, log));
    }

    /** Função para listar o log de auditoria */
    public List<String> getAuditLog() {
        return auditLog;
    }

    /** Função de punição */
    public void penalize(String user, Double penaltyAmount) {
        burnTokens(user, penaltyAmount);
        addAuditLog("Penalize", penaltyAmount, user);
        AuditLog.info(this.getContractId().toString(), "penalize",
                "Penalized " + user + " with " + penaltyAmount + " tokens.");
    }

    /** Função de garantia/deposito de segurança */
    public void depositCollateral(String user, Double collateralAmount) {
        issueTokens(user, collateralAmount);
        addAuditLog("DepositCollateral", collateralAmount, user);
        AuditLog.info(this.getContractId().toString(), "depositCollateral",
                "Deposited " + collateralAmount + " tokens as collateral for " + user);
    }

    /** Função de swap/conversão */
    public void swapTokens(String user, Double amountFrom, Double amountTo) {
        Balance userBalance = tokenBalances.getOrDefault(user, new Balance(BigDecimal.ZERO));
        if (amountFrom > 0 && amountFrom <= userBalance.getAmount().doubleValue()) {
            userBalance.subtract(BigDecimal.valueOf(amountFrom));
            userBalance.add(BigDecimal.valueOf(amountTo));
            tokenBalances.put(user, userBalance);
            addAuditLog("SwapTokens", amountFrom, user);
            AuditLog.info(this.getContractId().toString(), "swapTokens",
                    "Swapped " + amountFrom + " tokens to " + amountTo + " tokens for " + user);
        } else {
            AuditLog.warn(this.getContractId().toString(), "swapTokens", "Invalid swap amount.");
        }
    }

    /** Função de atualização/migração */
    public void migrate(String newContractAddress) {
        addAuditLog("Migrate", 0.0, newContractAddress);
        AuditLog.info(this.getContractId().toString(), "migrate", "Contract migrated to " + newContractAddress);
    }

    /** Método para calcular e retornar o hash dos dados do contrato */
    protected String calculateContractHash() {
        try {
            String contractData = getContractId().toString() + owner.getPublicKey();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contractData.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /** Método para assinar o contrato */
    public void signContract(Participant participant, String contractData) throws Exception {
        byte[] signature = participant.signContract(contractData);
        signatures.put(participant.getPublicKey(), signature);
        AuditLog.info(this.getContractId().toString(), "signContract",
                "Contract signed by " + participant.getPublicKey());
    }

    /** Método para verificar assinatura do contrato */
    public boolean verifySignature(Participant participant, String contractData) throws Exception {
        byte[] signature = signatures.get(participant.getPublicKey());
        return participant.verifySignature(contractData, signature);
    }
}
