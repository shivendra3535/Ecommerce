package com.cfs.Ecommerce.controller;

import com.cfs.Ecommerce.dto.UserDTO;
import com.cfs.Ecommerce.dto.UserLoginDTO;
import com.cfs.Ecommerce.model.User;
import com.cfs.Ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user){
        UserDTO userDTO = userService.registerUser(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO){
        try {
            UserDTO userDTO = userService.loginUser(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
