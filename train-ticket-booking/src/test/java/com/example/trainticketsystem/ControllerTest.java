package com.example.trainticketsystem;

import com.example.trainticketsystem.controller.TicketController;
import com.example.trainticketsystem.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
public class ControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void shouldPurchaseTicketSuccessfully() throws Exception {
		User user = new User();
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.doe@example.com");
		
		mockMvc.perform(post("/api/tickets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.user.email").value("john.doe@example.com"));
	}
	
	@Test
	void shouldReturnTicketForExistingUser() throws Exception {
		shouldPurchaseTicketSuccessfully();
		
		mockMvc.perform(get("/api/tickets/john.doe@example.com"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.user.email").value("john.doe@example.com"));
	}
}