package com.example.demo;

import java.util.Date;
import java.util.Map;

public class Sale {
	private Date saleDate;
	private String email;
	private String paymentMethod;
	private int saleItemCount;
	private Map<String, Integer> qtyPurchased;

	public Sale(Date saleDate, String email, String paymentMethod, int saleItemcount,
			Map<String, Integer> qtyPurchased) {
		this.saleDate = saleDate;
		this.email = email;
		this.paymentMethod = paymentMethod;
		this.saleItemCount = saleItemcount;
		this.qtyPurchased = qtyPurchased;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public int getSaleItemCount() {
		return saleItemCount;
	}

	public void setSaleItemCount(int saleItemCount) {
		this.saleItemCount = saleItemCount;
	}

	public Map<String, Integer> getQtyPurchased() {
		return qtyPurchased;
	}

	public void setQtyPurchased(Map<String, Integer> qtyPurchased) {
		this.qtyPurchased = qtyPurchased;
	}

}
