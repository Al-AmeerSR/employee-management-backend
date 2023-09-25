package com.crudexample.fullstackbackend.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crudexample.fullstackbackend.exception.UserNotFoundException;
import com.crudexample.fullstackbackend.model.Cuser;
import com.crudexample.fullstackbackend.repository.UserRepository;

@RestController
@CrossOrigin("http://localhost:3000/") //connecting to frontend
public class UserController {
	@Autowired
	private UserRepository urepo;

	@PostMapping("/user")
	Cuser newUser (@RequestBody Cuser newUser) {
		return urepo.save(newUser);
	}
	@GetMapping("/users")
	List<Cuser>getAllUsers(){
		return urepo.findAll();
	}
	@GetMapping("/user/{id}")
	Cuser getUser(@PathVariable Long id) {
		return urepo.findById(id)
				.orElseThrow(()->new UserNotFoundException(id));
	}
	@PutMapping("/user/{id}")
	Cuser UpdateUser(@RequestBody Cuser newUser,@PathVariable Long id) {
		return urepo.findById(id)
				.map(user -> {
					user.setUsername(newUser.getUsername());
					user.setEmail(newUser.getEmail());
					user.setName(newUser.getName());
					return urepo.save(user);
				})
				.orElseThrow(()->new UserNotFoundException(id));
	}
	@DeleteMapping("/user/{id}")
	String deleteUser(@PathVariable Long id) {
		if(!urepo.existsById(id)) {
			throw new UserNotFoundException(id);
		}
		urepo.deleteById(id);
		return "user with id:" +id +"has been deleted successfully";
	}

}
