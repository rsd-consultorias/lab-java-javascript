package br.com.rsdconsultoria.test;

import br.com.rsdconsultoria.model.SmartContract;
import br.com.rsdconsultoria.model.Participant;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

public class SmartContractTest {

    private SmartContract contract;
    private SmartContract recipientContract;
    private Participant owner;
    private Participant recipient;

    @Before
    public void setUp() throws Exception {
        owner = new Participant("OwnerAddress");
        recipient = new Participant("RecipientAddress");
        contract = new SmartContract(owner);
        recipientContract = new SmartContract(recipient);
        contract.addAuthorizedUser("OwnerAddress");
        contract.addAuthorizedUser("RecipientAddress");
        contract.addAuthorizedUser("ThirdPartyAddress");
        contract.addAuthorizedUser("ThirdPartyAddress2");
    }

    @Test
    public void testDeposit() {
        contract.deposit(100.0);
        assertEquals(BigDecimal.valueOf(100.0), contract.getBalance().getAmount());
    }

    @Test
    public void testWithdraw() {
        contract.deposit(100.0);
        contract.withdraw(30.0);
        assertEquals(BigDecimal.valueOf(70.0), contract.getBalance().getAmount());
    }

    @Test
    public void testTransfer() {
        contract.deposit(100.0);
        contract.transfer(recipientContract, 50.0);
        assertEquals(BigDecimal.valueOf(50.0), contract.getBalance().getAmount());
        assertEquals(BigDecimal.valueOf(50.0), recipientContract.getBalance().getAmount());
    }

    @Test
    public void testAddAuthorizedUser() {
        contract.addAuthorizedUser("User1");
        assertTrue(contract.isUserAuthorized("User1"));
    }

    @Test
    public void testRemoveAuthorizedUser() {
        contract.addAuthorizedUser("User1");
        contract.removeAuthorizedUser("User1");
        assertFalse(contract.isUserAuthorized("User1"));
    }

    @Test
    public void testIssueTokens() {
        contract.issueTokens("User1", 100.0);
        assertEquals(BigDecimal.valueOf(100.0), contract.getTokenBalance("User1").getAmount());
    }

    @Test
    public void testBurnTokens() {
        contract.issueTokens("User1", 100.0);
        contract.burnTokens("User1", 50.0);
        assertEquals(BigDecimal.valueOf(50.0), contract.getTokenBalance("User1").getAmount());
    }

    @Test
    public void testDistributeRewards() {
        contract.issueTokens("User1", 100.0);
        contract.distributeRewards("User1", 20.0);
        assertEquals(BigDecimal.valueOf(120.0), contract.getTokenBalance("User1").getAmount());
    }

    @Test
    public void testPenalize() {
        contract.issueTokens("User1", 100.0);
        contract.penalize("User1", 10.0);
        assertEquals(BigDecimal.valueOf(90.0), contract.getTokenBalance("User1").getAmount());
    }

    @Test
    public void testDepositCollateral() {
        contract.depositCollateral("User1", 15.0);
        assertEquals(BigDecimal.valueOf(15.0), contract.getTokenBalance("User1").getAmount());
    }

    @Test
    public void testSwapTokens() {
        contract.issueTokens("User1", 10.0);
        contract.swapTokens("User1", 5.0, 10.0);
        assertEquals(BigDecimal.valueOf(15.0), contract.getTokenBalance("User1").getAmount());
    }

    @Test
    public void testMigrate() {
        contract.migrate("NewContractAddress");
        // Since migration logic is not defined, we can only assert that no errors occur
        assertTrue(true);
    }

    @Test
    public void testVote() {
        contract.vote("NewContractAddress", "Proposal1", true);
        // Since voting logic is simplified, we can only assert that no errors occur
        assertTrue(true);
    }
}
