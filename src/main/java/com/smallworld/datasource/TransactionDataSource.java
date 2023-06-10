package com.smallworld.datasource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;

/**
 * This class is used to fetch the {@link com.smallworld.data.Transaction} JSON data from the transactions.json file.
 * @author Owais Shahzad
 *
 */
public class TransactionDataSource {

	private static ObjectMapper mapper = new ObjectMapper();
	private List<Transaction> trnsactions;
	private static String TRANSACTIONS_JSON = "transactions.json";
	
	/**
	 * This method is being used for fetch all the transactions.
	 * (Those with same mtn value will also be fetched in return value)
	 * 
	 * @return {@link List}
	 */
	public List<Transaction> getAllTransactions(){
		
		try {
			trnsactions = mapper.readValue(Paths.get(TRANSACTIONS_JSON).toFile(), new TypeReference<List<Transaction>>(){});
		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(trnsactions == null) {
			
			System.out.print("getAllTransactions() -> could not fetched the transactions.");
			
			trnsactions = new ArrayList<Transaction>();
		}
		
		System.out.println("getAllTransactions() -> transactions set success");
		
		return trnsactions;
	}
	
	/**
	 * This method is being used to fetch the transactions having unique mtn value.
	 * (No duplicate transactions will be fetched from the data)
	 * 
	 * @return {@link List}
	 */
	public List<Transaction> getUniqueTransactions(){
		
		List<Transaction> uniqueTransactions = getAllTransactions().stream()
		.collect(Collectors.groupingBy(t -> t.getMtn()))
		.values()
		.stream()
		.map(gTx -> gTx.stream().findFirst().get()).collect(Collectors.toList());
		
		System.out.println("getUniqueTransactions() -> transactions set success");
		
		return uniqueTransactions;
	}
}
