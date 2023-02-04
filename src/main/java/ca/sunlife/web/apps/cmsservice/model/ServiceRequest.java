package ca.sunlife.web.apps.cmsservice.model;

public class ServiceRequest {

	private String firstName;

	private String lastName;

	private String dateOfBirth;

	private String income;

	private String monthlyExpenses;

	private String monthlySavings;

	private String savings;

	private String assets;

	private String debts;

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

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getMonthlyExpenses() {
		return monthlyExpenses;
	}

	public void setMonthlyExpenses(String monthlyExpenses) {
		this.monthlyExpenses = monthlyExpenses;
	}

	public String getMonthlySavings() {
		return monthlySavings;
	}

	public void setMonthlySavings(String monthlySavings) {
		this.monthlySavings = monthlySavings;
	}

	public String getSavings() {
		return savings;
	}

	public void setSavings(String savings) {
		this.savings = savings;
	}

	public String getAssets() {
		return assets;
	}

	public void setAssets(String assets) {
		this.assets = assets;
	}

	public String getDebts() {
		return debts;
	}

	public void setDebts(String debts) {
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
