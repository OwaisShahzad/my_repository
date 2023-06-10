package com.smallworld.data;

import java.math.BigDecimal;

public class Transaction implements Comparable<Transaction> {
    // Represent your transaction data here.
	
	private long mtn; //663458,
	private BigDecimal amount; //430.2,
	private String senderFullName; //"Tom Shelby",
	private int senderAge; //22,
	private String beneficiaryFullName; //"Alfie Solomons",
	private int beneficiaryAge; //33,
	private Long issueId; //1,
	private boolean issueSolved; //false,
	private String issueMessage; //"Looks like money laundering"
	
	public long getMtn() {
		return mtn;
	}
	public void setMtn(long mtn) {
		this.mtn = mtn;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getSenderFullName() {
		return senderFullName;
	}
	public void setSenderFullName(String senderFullName) {
		this.senderFullName = senderFullName;
	}
	public int getSenderAge() {
		return senderAge;
	}
	public void setSenderAge(int senderAge) {
		this.senderAge = senderAge;
	}
	public String getBeneficiaryFullName() {
		return beneficiaryFullName;
	}
	public void setBeneficiaryFullName(String beneficiaryFullName) {
		this.beneficiaryFullName = beneficiaryFullName;
	}
	public int getBeneficiaryAge() {
		return beneficiaryAge;
	}
	public void setBeneficiaryAge(int beneficiaryAge) {
		this.beneficiaryAge = beneficiaryAge;
	}
	public Long getIssueId() {
		return issueId;
	}
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}
	public boolean isIssueSolved() {
		return issueSolved;
	}
	public void setIssueSolved(boolean issueSolved) {
		this.issueSolved = issueSolved;
	}
	public String getIssueMessage() {
		return issueMessage;
	}
	public void setIssueMessage(String issueMessage) {
		this.issueMessage = issueMessage;
	}
	
	@Override
	public String toString() {
		return "Transaction [mtn=" + mtn + ", amount=" + amount + ", senderFullName=" + senderFullName + ", senderAge="
				+ senderAge + ", beneficiaryFullName=" + beneficiaryFullName + ", beneficiaryAge=" + beneficiaryAge
				+ ", issueId=" + issueId + ", issueSolved=" + issueSolved + ", issueMessage=" + issueMessage + "]";
	}
	
	@Override
	public int compareTo(Transaction t) {
		return t.getAmount().compareTo(this.getAmount());
	}
	
	
}
