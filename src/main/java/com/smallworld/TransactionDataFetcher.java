package com.smallworld;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.smallworld.data.Transaction;
import com.smallworld.datasource.TransactionDataSource;

public class TransactionDataFetcher {

    /**
     * Returns the sum of the amounts of all transactions
     */
    public BigDecimal getTotalTransactionAmount() {
    	
    	List<Transaction> txns = new TransactionDataSource().getUniqueTransactions();
    	
    	/*
		 * BigDecimal _totalAmount = new BigDecimal(0d).setScale(2); 
		 * List<Transaction> uniquetxns = new ArrayList<>(); txns.forEach(t -> { long existingCount =
		 * uniquetxns.stream().filter(unqTxn -> unqTxn.getMtn() == t.getMtn()).count();
		 * 
		 * if(existingCount <= 0) { uniquetxns.add(t); } }); for(int i = 0 ; i <
		 * uniquetxns.size() ; i++) { _totalAmount =
		 * _totalAmount.add(uniquetxns.get(i).getAmount()); }
		 */
    	
    	BigDecimal totalAmount = txns.stream()
    			.map(unTxn -> unTxn.getAmount())
    			.reduce(new BigDecimal(0d).setScale(2), (a, b) -> a.add(b));
    	
        return totalAmount;
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public BigDecimal getTotalTransactionAmountSentBy(String senderFullName) {
    	
    	List<Transaction> txns = new TransactionDataSource().getUniqueTransactions();
    	
    	BigDecimal totalAmountBySender = txns.stream().
    			filter(inTxn -> inTxn.getSenderFullName().equals(senderFullName)).
    			map(unTxn -> unTxn.getAmount()).
    			reduce(new BigDecimal(0d).setScale(2), (a, b) -> a.add(b));
    	
        return totalAmountBySender;
    }

    /**
     * Returns the highest transaction amount
     */
    public BigDecimal getMaxTransactionAmount() {
    	
    	List<Transaction> txns = new TransactionDataSource().getUniqueTransactions();
    	
    	BigDecimal maxTxnAmount = txns.stream().map(unTxn -> unTxn.getAmount()).
    			max(Comparator.comparing(BigDecimal::doubleValue)).
    			get();
    	
        return maxTxnAmount;
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
    	
    	List<Transaction> txns = new TransactionDataSource().getAllTransactions();
    	Stream<String> uniqueSenders = txns.stream().map(txn -> txn.getSenderFullName());
    	Stream<String> uniqueBenes = txns.stream().map(txn -> txn.getBeneficiaryFullName());
    	
    	long count = Stream.concat(uniqueSenders,uniqueBenes).distinct().count();
    	
        return count;
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
    	
    	List<Transaction> txns = new TransactionDataSource().getAllTransactions();
    	
    	long beneIssueCount = txns.stream().filter(txn -> txn.getBeneficiaryFullName().equals(clientFullName) && !txn.isIssueSolved()).count();
    	if(beneIssueCount > 0) {
    		return true;
    	}
    	
    	long senderIssueCount = txns.stream().filter(txn -> txn.getSenderFullName().equals(clientFullName) && !txn.isIssueSolved()).count();
    	if(senderIssueCount > 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Returns all transactions indexed by beneficiary name
     * </br>
     * </br>
     * Assuming that a beneficiary can have more than one transaction, 
     * changing the return type from Map<String, Transaction> to Map<String, List<Transaction>>.
     * @return {@link Map}
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
    	
		/*
		 * Map<Object, List<Transaction>> beneTxns = new
		 * TransactionDataSource().getAllTransactions().stream()
		 * .collect(Collectors.groupingBy(t -> t.getBeneficiaryFullName()));
		 */
    	
    	List<Transaction> txns = new TransactionDataSource().getAllTransactions();
    	Map<String, List<Transaction>> benesTxns = new HashMap<>();
    	
    	for(int i = 0 ; i < txns.size() ; i++) {
    		String beneName = txns.get(i).getBeneficiaryFullName();
    		
    		if(benesTxns.get(beneName) == null) {
    			benesTxns.put(beneName, new ArrayList<Transaction>());
    		}
    		
    		benesTxns.get(beneName).add(txns.get(i));
    	}
    	
        return benesTxns;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Long> getUnsolvedIssueIds() {
    	
    	return new TransactionDataSource().getAllTransactions().
    			stream().filter(tx -> !tx.isIssueSolved()).
    			map(mTx -> mTx.getIssueId()).collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
    	
    	return new TransactionDataSource().getAllTransactions().
    			stream().filter(tx -> tx.isIssueSolved() && tx.getIssueId() != null).
    			map(mTx -> mTx.getIssueMessage()).collect(Collectors.toList());
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
    	
    	List<Transaction> txns = new TransactionDataSource().getUniqueTransactions();
    	Collections.sort(txns);
    	
    	return txns.subList(0, txns.size() >= 3 ? 3 : txns.size());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
    	
    	List<Transaction> txns = new TransactionDataSource().getUniqueTransactions();
    	
    	String[] sendersName = txns.stream().map(tx -> tx.getSenderFullName()).collect(Collectors.toSet()).toArray(String[]::new);
    	BigDecimal[] sendersTotalAmount = new BigDecimal[sendersName.length];
    	for(int i = 0 ; i < sendersName.length ; i++) {
    		final int _i = i;
    		BigDecimal totalAmount = txns.stream().
    				filter(tx -> tx.getSenderFullName().equals(sendersName[_i])).
    				map(fTx -> fTx.getAmount()).reduce(new BigDecimal(0d), (a,b) -> a.add(b));
    		sendersTotalAmount[_i] = totalAmount;
    	}
    	
    	int maxAmountIndex = 0;
    	BigDecimal maxAmount = sendersTotalAmount[0];
    	
    	for(int j = 1 ; j < sendersTotalAmount.length ; j++) {
    		if(maxAmount.compareTo(sendersTotalAmount[j]) < 0) {
    			maxAmount = sendersTotalAmount[j];
    			maxAmountIndex = j;
    		}
    	}
    	 
    	return Optional.ofNullable(sendersName[maxAmountIndex]);
    }

}
