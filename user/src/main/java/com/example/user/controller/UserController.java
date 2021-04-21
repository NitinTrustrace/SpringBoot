package com.example.user.controller;


import com.example.user.dto.UserPatch;
import com.example.user.dto.UserRequest;
import com.example.user.model.UserModel;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    @PostMapping("/userData")
    public ResponseEntity<String> signup(@RequestHeader(value = "Accept") String acceptHeader, @RequestHeader(value = "Authorization") String authorizationHeader, @RequestBody UserRequest userRequest) {

        if (authorizationHeader.equals("Nitin@123")) {
            try {
                userService.saveUser(userRequest);
                return new ResponseEntity<>("User Registration Succesfull", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            try {

                return new ResponseEntity<>("User Registration Failed", HttpStatus.NOT_ACCEPTABLE);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    @GetMapping("/userData")
    public ResponseEntity<Page<UserModel>> findAll(@RequestParam Optional<String> username, @RequestParam Optional<Integer> page) {
        // Sort by added
        try {
            return status(HttpStatus.OK).body(userService.findByName(username, page));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/userData/{id}")
    public ResponseEntity<Optional<UserModel>> getById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(userService.getUserById(id));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/userData/{id}")
    public ResponseEntity<String> updateById(@RequestBody UserRequest userRequest, @PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(userService.updateStudent(userRequest, id));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/userData/{id}")
    public ResponseEntity<String> deleteTutorial(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(userService.deleteByID(id));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/userData")
    public ResponseEntity<String> deleteAllTutorials() {
        try {
            return status(HttpStatus.OK).body(userService.deleteAll());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/userData/{id}")
    public ResponseEntity<String> partialUpdate(@RequestBody ArrayList<UserPatch> userPatch, @PathVariable Long id) {
        try {
            for (int i = 0; i < userPatch.size(); i++) {

                userService.partialReplaceUpdate(userPatch.get(i), id);

            }
            return status(HttpStatus.OK).body("Patch updated");
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
