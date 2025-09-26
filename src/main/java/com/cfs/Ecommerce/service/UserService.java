package com.cfs.Ecommerce.service;

import com.cfs.Ecommerce.dto.UserDTO;
import com.cfs.Ecommerce.model.User;
import com.cfs.Ecommerce.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserDTO registerUser(User user){
        User newUser = userRepo.save(user);
        return new UserDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getPhoneNumber());
    }

    public UserDTO loginUser(String email, String password){
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isPresent() && user.get().getPassword().equals(password)){
            return new UserDTO(user.get().getId(), user.get().getName(), user.get().getEmail(), user.get().getPhoneNumber());
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public List<UserDTO> getAllUsers(){
        return userRepo.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber()))
                .collect(Collectors.toList());
    }
}
