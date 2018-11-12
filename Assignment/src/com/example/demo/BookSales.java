package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.demo.Book;
import com.example.demo.Sale;



public class BookSales {

	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

		//parses the command line arguments
		Map<String, String> map = commandLineParser(args);
		
	
		BufferedReader booksPath = new BufferedReader(new FileReader(map.get("--books")));
		BufferedReader salesPath = new BufferedReader(new FileReader(map.get("--sales")));
		
		String line = "";
		String[] booksData = null;
		List<Book> books = new ArrayList<>();

		//read books data from file and adding it to array
		while ((line = booksPath.readLine()) != null) {
			booksData = line.split(",");
			books.add(new Book(booksData[0], booksData[1], booksData[2], Double.parseDouble(booksData[3])));
		}
		booksPath.close();

		List<Sale> sales = new ArrayList<>();
		String sale = "";
		String[] salesData = null;

		//read sales data from file and adding it to array
		while ((sale = salesPath.readLine()) != null) {
			
			//split data by , as it is a csv file
			salesData = sale.split(",");
			Map<String, Integer> qtyPurchased = new HashMap<>();
			int itemsCount = Integer.parseInt(salesData[3]);

			int j = 4;
			for (int i = 0; i < itemsCount; i++) {

				String saleQty[] = salesData[j++].split(";");
				qtyPurchased.put(saleQty[0], Integer.parseInt(saleQty[1]));
			}

			//modify the date given according to our need
			String[] date = salesData[0].split("-");

			sales.add(
					new Sale(new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])),
							salesData[1], salesData[2], Integer.parseInt(salesData[3]), qtyPurchased));

		}
		salesPath.close();
        
		double amount=0.0d;
		
		
		//writing the data into the file
		BufferedWriter outputFile = new BufferedWriter(new FileWriter("output.txt"));
		
		if(map.get("--top_selling_books") != null) {
			int  top_selling_books=map.get("--top_selling_books")!=null?Integer.parseInt(map.get("--top_selling_books")):0;
			List<String> topSellingBooks = topSellingBooks(top_selling_books, books, sales);
			if(!topSellingBooks.isEmpty()) {
				outputFile.write("top_selling_books");
				for(String bookName:topSellingBooks) {
					outputFile.write("\t"+bookName);
				}
				outputFile.newLine();
			}
		}
		
		if(map.get("--top_customers") != null) {
			
			List<String> topCustomers = topCustomers(Integer.parseInt(map.get("--top_customers")), books, sales);
			
			
			if(!topCustomers.isEmpty()) {
				
				outputFile.write("top_customers");
				for(String customerName:topCustomers) {
					outputFile.write("\t"+customerName);
				}
				outputFile.newLine();
			}
			
			
		}
		
		String salesOnDate = map.get("--sales_on_date");
		if(salesOnDate!=null) {
			Date d = null;
			if(salesOnDate!=null) {
				String[] givenDate = salesOnDate.split("-");
				d=  new Date(Integer.parseInt(givenDate[0]), Integer.parseInt(givenDate[1]),
						Integer.parseInt(givenDate[2]));
			}
			if(d!=null) {
				amount = getSaleAmountOnDate(d, books, sales);
				outputFile.write("sales_on_date\t");
				outputFile.write(salesOnDate+"\t"+amount);
			}
			
		}
		
		
		outputFile.close();
		
	}

	//get the list of top selling books
	private static List<String> topSellingBooks(int topCount, List<Book> books, List<Sale> sales) {
		Map<String, Double> bookSaleAmount = new HashMap<>();
		List<String> bookNames = new ArrayList<>();
		for (Sale sale : sales) {
			Map<String, Integer> qtyPurchased = sale.getQtyPurchased();
			Book book = null;
			for (Map.Entry<String, Integer> entry : qtyPurchased.entrySet()) {
				if (book == null || !book.getBookId().equals(entry.getKey())) {
					book = books.stream().filter(b -> b.getBookId().equals(entry.getKey())).findFirst().orElse(null);
				}

				if (book == null) {
					// book not found
					continue;
				}

				double bookPrice = book.getBookPrice();
				double totalPrice = bookPrice * entry.getValue();
				if (bookSaleAmount.containsKey(book.getBookId())) {
					bookSaleAmount.put(book.getBookId(), bookSaleAmount.get(book.getBookId()) + totalPrice);
				} else {
					bookSaleAmount.put(book.getBookId(), totalPrice);
				}
			}
		}

		// Create a list from elements of HashMap
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(bookSaleAmount.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		for (Map.Entry<String, Double> aa : list) {
			if (topCount == 0) {
				break;
			}
			bookNames.add(aa.getKey());
			topCount--;
		}
		return bookNames;
	}

	//get the top customers
	private static List<String> topCustomers(int topCount, List<Book> books, List<Sale> sales) {
		Map<String, Double> bookSaleAmount = new HashMap<>();
		List<String> customers = new ArrayList<>();

		for (Sale sale : sales) {
			Map<String, Integer> qtyPurchased = sale.getQtyPurchased();
			String customer = sale.getEmail();
			Book book = null;
			for (Map.Entry<String, Integer> entry : qtyPurchased.entrySet()) {
				if (book == null || !book.getBookId().equals(entry.getKey())) {
					book = books.stream().filter(b -> b.getBookId().equals(entry.getKey())).findFirst().orElse(null);
				}
				if (book == null) {
					// book not found
					continue;
				}

				double bookPrice = book.getBookPrice();
				double totalPrice = bookPrice * entry.getValue();

				if (bookSaleAmount.containsKey(customer)) {
					bookSaleAmount.put(customer, bookSaleAmount.get(customer) + totalPrice);
				} else {
					bookSaleAmount.put(customer, totalPrice);
				}
			}

		}
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(bookSaleAmount.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		for (Map.Entry<String, Double> aa : list) {
			if (topCount == 0) {
				break;
			}
			customers.add(aa.getKey());
			topCount--;
		}

		return customers;

	}

	//get the amount of sale on the given date
	static double getSaleAmountOnDate(Date date, List<Book> books, List<Sale> sales) {

		List<Sale> saleList = sales.stream().filter(s -> s.getSaleDate().equals(date)).collect(Collectors.toList());
		double sum = 0.0d;
		for (Sale sale : saleList) {
			Book book = null;
			Map<String, Integer> qtyPurchased = sale.getQtyPurchased();
			for (Map.Entry<String, Integer> entry : qtyPurchased.entrySet()) {
				if (book == null || !book.getBookId().equals(entry.getKey())) {
					book = books.stream().filter(b -> b.getBookId().equals(entry.getKey())).findFirst().orElse(null);
				}
				double bookPrice = book.getBookPrice();
				if (book == null)
					continue;
				int quantity = entry.getValue();
				sum += bookPrice * quantity;

			}

		}
		return sum;

	}

	//parses the command line args
	static Map<String, String> commandLineParser(String[] args) {

		Map<String, String> map = new HashMap<>();

		if (args.length < 2) {
			System.out.println("Not enough arguments received");
			System.exit(0);
		}

		for (int i = 0; i < args.length; i++) {

			if (args[i].startsWith("--")) {
				String[] values = args[i].split("=");
				if(values.length<2) {
					continue;
				}
				map.put(values[0], values[1]);
			}
		}

		if (!map.containsKey("--books") || !map.containsKey("--sales")) {
			System.out.println("Books and sales files are required");
			System.exit(0);
		}
		return map;
	}

}
