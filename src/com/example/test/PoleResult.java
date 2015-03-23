package com.example.test;

public class PoleResult {
	private String optionId;
	private String optionCount;
	private String optionResultPercentage;
	private String optionText;

	public PoleResult(String optionId, String optionCount, String optionResultPercentage, String optionText) {
		this.optionId = optionId;
		this.optionCount = optionCount;
		this.optionResultPercentage = optionResultPercentage;
		this.optionText = optionText;
	}
	
	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(String optionCount) {
		this.optionCount = optionCount;
	}

	public String getOptionResultPercentage() {
		return optionResultPercentage;
	}

	public void setOptionResultPercentage(String optionResultPercentage) {
		this.optionResultPercentage = optionResultPercentage;
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

}
