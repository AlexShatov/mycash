package ru.mycash.util;

import java.util.regex.Pattern;

public class InputMatcher {

	private static final String loginRegex = "[A-Za-z0-9_]{6,15}";
	private static final String passwordRegex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,15}";
	private static final String mailRegex = "([a-zA-Z0-9!#$%&amp;'*+\\/=?^_`{|}~.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*){6,25}";
	private static final String amountRegex = "\\d+(\\.\\d{1,2})?";
	private static final String currencyRegex = "[A-Z]{3}";
	private static final String dateRegex = "\\d{4}-\\d{2}-\\d{2}";
	private static final String nameRegex = "[\\d+\\w+\\s+]{1,20}";
	
	public InputMatcher() {
		
	}
	
	public boolean matchLogin(String login) {
		return (login != null && Pattern.matches(loginRegex, login)); 
		
	}
	
	public boolean matchPassword(String pass) {
		return (pass != null && Pattern.matches(passwordRegex, pass));
	}
	
	
	public boolean matchMail(String mail) {
		return (mail != null && Pattern.matches(mailRegex, mail));
	}
	
	public boolean matchAmount(String amount) {
		return (amount != null && Pattern.matches(amountRegex, amount));
	}
	
	public boolean matchCurrency(String curr) {
		return (curr != null && Pattern.matches(currencyRegex, curr));
	}
	
	public boolean matchDate(String date) {
		return (date != null && Pattern.matches(dateRegex, date));
	}
	
	public boolean matchName(String input) {
		return (input != null && Pattern.matches(nameRegex, input));
	}
}
