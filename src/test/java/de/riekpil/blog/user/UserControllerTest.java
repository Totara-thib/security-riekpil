package de.riekpil.blog.user;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	@WithMockUser(username = "test", roles = "GENERIC")
	public void shouldNotBeAbleToAccessAnUser() throws Exception {
		this.mockMvc.perform(get("/api/users/test")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "test")
	public void shouldNotFindUserIfDoesNotExist() throws Exception {

		when(userService.getUserByUsername("test"))
				.thenThrow(new UserNotFoundException("Can't find this user sad smiley"));

		mockMvc.perform(get("/api/users/test")).andExpect(status().isNotFound());

	}

	@Test
	public void shouldBeAbleToCreateAUser() throws Exception {

		User user = new User("test", "test2@gmail.com");
		String body = (new ObjectMapper()).valueToTree(user).toString();

		this.mockMvc
				.perform(post("/api/users").with(SecurityMockMvcRequestPostProcessors.user("gigi").roles("USER"))
						.contentType(MediaType.APPLICATION_JSON).content(body).with(csrf()))
				.andExpect(status().isCreated());
	}

	@Test
	public void shouldNotBeAbleToCreateAUserWithTheWrongRole() throws Exception {

		User user = new User("test", "test2@gmail.com");
		String body = (new ObjectMapper()).valueToTree(user).toString();

		this.mockMvc
				.perform(post("/api/users").with(SecurityMockMvcRequestPostProcessors.user("gigi").roles("GENERIC"))
						.contentType(MediaType.APPLICATION_JSON).content(body).with(csrf()))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "test", roles = "ADMIN")
	public void shouldNotBeAbleToDeleteAUserWithTheWrongRole() throws Exception {

		this.mockMvc.perform(delete("/api/users/test").with(csrf())).andExpect(status().isOk());
	}
}
