package ca.sunlife.web.apps.cmsservice.model;

public class ServiceRequest {

	private String firstName;

	private String lastName;

	private String dateOfBirth;

	private int income;

	private int monthlyExpenses;

	private int monthlySavings;

	private int savings;

	private int assets;

	private int debts;

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

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getMonthlyExpenses() {
		return monthlyExpenses;
	}

	public void setMonthlyExpenses(int monthlyExpenses) {
		this.monthlyExpenses = monthlyExpenses;
	}

	public int getMonthlySavings() {
		return monthlySavings;
	}

	public void setMonthlySavings(int monthlySavings) {
		this.monthlySavings = monthlySavings;
	}

	public int getSavings() {
		return savings;
	}

	public void setSavings(int savings) {
		this.savings = savings;
	}

	public int getAssets() {
		return assets;
	}

	public void setAssets(int assets) {
		this.assets = assets;
	}

	public int getDebts() {
		return debts;
	}

	public void setDebts(int debts) {
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

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}



}
