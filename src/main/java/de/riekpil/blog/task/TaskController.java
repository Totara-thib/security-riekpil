package de.riekpil.blog.task;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("api/tasks/demo")
public class TaskController {
		
	@Autowired
	private TaskService taskService;
	
	@PostMapping
	public ResponseEntity<Void> update(@RequestBody JsonNode payload, 
			UriComponentsBuilder uriComponentsBuilder) {
		long taskId = this.taskService.createTask(payload.get("taskTitle").asText());
		
		return ResponseEntity
				.created(uriComponentsBuilder.path("/api/tasks/demo/{taskId}").build(taskId))
				.build();
	}
	
	@DeleteMapping("{taskId}")
	@RolesAllowed("ADMIN")
	public void deleteTask(@PathVariable Long taskId) {
		this.taskService.deleteTask(taskId);
	}
}
