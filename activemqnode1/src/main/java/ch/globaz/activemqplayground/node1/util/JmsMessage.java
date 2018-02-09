package ch.globaz.activemqplayground.node1.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

public class JmsMessage {

	private String message;
	private String threadName;
	private String sender;


	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate date;

	public JmsMessage(){}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public JmsMessage(String message, String sender){
		this.message = message;
		this.date = LocalDate.now();
		this.threadName = Thread.currentThread().getName();
		this.sender = sender;


	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String asJsonString ()  {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;

		try {
			jsonString =  mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return jsonString;
	}
}
