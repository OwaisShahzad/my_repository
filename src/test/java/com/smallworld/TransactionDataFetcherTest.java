package com.smallworld;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.smallworld.data.Transaction;

@TestInstance(value = Lifecycle.PER_CLASS)
public class TransactionDataFetcherTest {

	TransactionDataFetcher txnDataFetcher;
	
	@BeforeAll                                         
    void instanciate() {
		txnDataFetcher = new TransactionDataFetcher();
    }
	
	@Test
	@DisplayName("getTotalTransactionAmount()")
    public void getTotalTransactionAmount() {
		BigDecimal actualUniqueTxnTotal = new BigDecimal(3588.17d).setScale(2, BigDecimal.ROUND_DOWN);
		BigDecimal actualDuplicateTxnTotal = new BigDecimal(5070.37d).setScale(2, BigDecimal.ROUND_DOWN);
		
		Assertions.assertEquals(actualUniqueTxnTotal,txnDataFetcher.getTotalTransactionAmount());
		Assertions.assertNotEquals(actualDuplicateTxnTotal,txnDataFetcher.getTotalTransactionAmount());
    }
	
	@ParameterizedTest
	@DisplayName("getTotalTransactionAmountSentBy()")
	@CsvSource( {
		"Billy Kimber, 459.09",
		"Tom Shelby, 678.06",
		"Owais Shahzad, 0.00"
		
	})
    public void getTotalTransactionAmountSentBy(String senderFullName, BigDecimal totalSentTxnAmt) {
		
		Assertions.assertEquals(totalSentTxnAmt,txnDataFetcher.getTotalTransactionAmountSentBy(senderFullName));
    }

	@Test
	@DisplayName("getMaxTransactionAmount()")
    public void getMaxTransactionAmount() {
		
    	Assertions.assertEquals(new BigDecimal(985.0d).setScale(1), txnDataFetcher.getMaxTransactionAmount());
    	Assertions.assertNotEquals(new BigDecimal(0.00d), txnDataFetcher.getMaxTransactionAmount());
    	
    }
	
	@Test
	@DisplayName("getMaxTransactionAmount()")
    public void countUniqueClients() {
		
    	Assertions.assertEquals(14, txnDataFetcher.countUniqueClients());
    }
	
	@ParameterizedTest
	@DisplayName("hasOpenComplianceIssues()")
	@CsvSource({
		"Tom Shelby, true",
		"Owais Shahzad, false"
	})
    public void hasOpenComplianceIssues(String clientFullName, boolean isOpen) {
		Assertions.assertEquals(isOpen, txnDataFetcher.hasOpenComplianceIssues(clientFullName));
    }
	
	@ParameterizedTest
	@DisplayName("getTransactionsByBeneficiaryName()")
	@CsvSource({
		"Alfie Solomons,Tom Shelby",
		"Arthur Shelby,Owais Shahzad"
		})
    public void getTransactionsByBeneficiaryName(String extbeneName , String nonextbeneName) {
		
		Map<String, List<Transaction>> txnsMap = txnDataFetcher.getTransactionsByBeneficiaryName();
		
		Assertions.assertNotNull(txnsMap.get(extbeneName));
		Assertions.assertNull(txnsMap.get(nonextbeneName));
    }
	
	@Test
	@DisplayName("getUnsolvedIssueIds()")
    public void getUnsolvedIssueIds() {
    	
		//open issues IDs in transaction,josn data are 1, 3, 99, 54, 15.
		
		Assertions.assertEquals(true, txnDataFetcher.getUnsolvedIssueIds().contains(1l));
		Assertions.assertNotEquals(true, txnDataFetcher.getUnsolvedIssueIds().contains(65l));
		Assertions.assertNotEquals(true, txnDataFetcher.getUnsolvedIssueIds().contains(0l));
    }
	
	@ParameterizedTest
	@DisplayName("getAllSolvedIssueMessages()")
	@ValueSource(strings = {
		"Never gonna give you up",
		"Never gonna let you down",
		"Never gonna run around and desert you"
		//,"Null",//not existing message
		})
    public void getAllSolvedIssueMessages(String solvedMsgs) {
    	
		Assertions.assertEquals(true,txnDataFetcher.getAllSolvedIssueMessages().contains(solvedMsgs));
    }
	
	@Test
	@DisplayName("getTop3TransactionsByAmount")
    public void getTop3TransactionsByAmount() {
		
		List<Long> txnMtns = txnDataFetcher.getTop3TransactionsByAmount().stream().map(tx -> tx.getMtn()).collect(Collectors.toList());
		
		Assertions.assertEquals(true,txnMtns.contains(5465465l));
		Assertions.assertNotEquals(true,txnMtns.contains(96132456l));
    }

    @Test
    @DisplayName("getTopSender()")
    public void getTopSender() {
    	Assertions.assertEquals(Optional.of("Grace Burgess"), txnDataFetcher.getTopSender());
    	
    }


}
