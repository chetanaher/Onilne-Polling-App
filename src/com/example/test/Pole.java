package com.example.test;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aher on 10/2/2014.
 */
public class Pole {
	public String questionId;
	public String question;
	public String userId;
	public String createdAt;
	public String updatedAt;
	public List<NameValuePair> options = new ArrayList<NameValuePair>();

	public Pole(String questionId, String question, String userId,
			List<NameValuePair> options, String createdAt) {
		this.questionId = questionId;
		this.question = question;
		this.userId = userId;
		this.options = options;
		this.createdAt = createdAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}
	
	public String getQuestionId() {
		return questionId;
	}

	public String getQuestion() {
		return question;
	}

	public String getUserId() {
		return userId;
	}

	public List<NameValuePair> getOptions() {
		return options;
	}
}
