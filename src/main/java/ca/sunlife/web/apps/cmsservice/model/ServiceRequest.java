package ca.sunlife.web.apps.cmsservice.model;

public class ServiceRequest {

	private String firstName;

	private String lastName;

	private String dateOfBirth;

	private double income;

	private double monthlyExpenses;

	private double monthlySavings;

	private double savings;

	private double assets;

	private double debts;

	private String email;

	private String leadSource;

	private boolean quickStart;

	private String language;

	private String id;

	private String postalCode;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public double getMonthlySavings() {
		return monthlySavings;
	}

	public void setMonthlySavings(double monthlySavings) {
		this.monthlySavings = monthlySavings;
	}

	public double getSavings() {
		return savings;
	}

	public void setSavings(double savings) {
		this.savings = savings;
	}

	public double getAssets() {
		return assets;
	}

	public void setAssets(double assets) {
		this.assets = assets;
	}

	public double getDebts() {
		return debts;
	}

	public void setDebts(double debts) {
		this.debts = debts;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

	public boolean isQuickStart() {
		return quickStart;
	}

	public void setQuickStart(boolean quickStart) {
		this.quickStart = quickStart;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

}
