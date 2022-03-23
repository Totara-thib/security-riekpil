package de.riekpil.blog.user;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<User> getAllUsers() {
		return this.userService.getAllUsers();
	}

	@RolesAllowed("USER")
	@GetMapping("/{username}")
	public User getUserByUsername(@PathVariable String username) {
		return this.userService.getUserByUsername(username);
	}

	@RolesAllowed("USER")
	@PostMapping
	public ResponseEntity<Void> createNewUser(@RequestBody @Valid User user,
			UriComponentsBuilder uriComponentsBuilder) {
		this.userService.storeNewUser(user);
		return ResponseEntity.created(uriComponentsBuilder.path("/api/users/{username}").build(user.getUsername()))
				.build();
	}

	@DeleteMapping("/{username}")
	@RolesAllowed("ADMIN")
	public boolean deleteUser(@PathVariable String username) {
//Faire un Mock pour mes tests de Suppression
		return this.userService.deleteUser(this.userService.getUserByUsername(username));
	}
}