package ca.sunlife.web.apps.cmsservice.model;

import java.util.Date;

public class QuickAssessment {
	
	private Date dob;
    private double income;
    private double monthlyExpenses;
    private double savings;
    private double monthlySavings;
    private double assets;
    private double totalDebt;
  
    
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public double getMonthlyExpenses() {
		return monthlyExpenses;
	}
	public void setMonthlyExpenses(double monthlyExpenses) {
		this.monthlyExpenses = monthlyExpenses;
	}
	public double getSavings() {
		return savings;
	}
	public void setSavings(double savings) {
		this.savings = savings;
	}
	public double getMonthlySavings() {
		return monthlySavings;
	}
	public void setMonthlySavings(double monthlySavings) {
		this.monthlySavings = monthlySavings;
	}
	public double getAssets() {
		return assets;
	}
	public void setAssets(double assets) {
		this.assets = assets;
	}
	public double getTotalDebt() {
		return totalDebt;
	}
	public void setTotalDebt(double totalDebt) {
		this.totalDebt = totalDebt;
	}

    
}
