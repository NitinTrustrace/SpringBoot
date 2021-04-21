package com.example.user.service;

import com.example.user.dto.UserPatch;
import com.example.user.dto.UserRequest;
import com.example.user.model.UserModel;
import com.example.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    @Transactional
    public void saveUser(UserRequest userRequest){

        UserModel user = new UserModel();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setCreated(Instant.now());
        userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public Page<UserModel> findByName(Optional<String> username, Optional<Integer> noOfPage) {

        return userRepository.findByName(username.orElse("_"), PageRequest.of(noOfPage.orElse(0), 5));


    }

    @Transactional(readOnly = true)
    public Optional<UserModel> getUserById(Long id) {

        return userRepository.findById(id);

    }

    public String updateStudent(UserRequest userRequest, long id) {

        Optional<UserModel> userRequestOptional = userRepository.findById(id);

        if (!userRequestOptional.isPresent()) {
            return "user not found";
        }
        else {
            UserModel user = userRequestOptional.get();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            userRepository.save(user);
            return "User updated";

        }
    }

    public String deleteByID(Long id){
        userRepository.deleteById(id);
        return "Deleted By Id";

    }

    public String deleteAll(){
        userRepository.deleteAll();
        return "All Users Deleted";
    }


    @Transactional
    public void partialReplaceUpdate(UserPatch userPatch, Long id) {

        if(userPatch.getAction().equals("replace")){

        if(userPatch.getFieldName().equals("username")) {
            userRepository.findUserReplace(userPatch.getValue(), id);
        }

        else if(userPatch.getFieldName().equals("email")) {
            userRepository.findEmailReplace(userPatch.getValue(), id);
        }

        }

        else if(userPatch.getAction().equals("add")){

            if(userPatch.getFieldName().equals("username")) {
                userRepository.findUserReplace(userPatch.getValue(), id);
            }

            else if(userPatch.getFieldName().equals("email")) {
                userRepository.findEmailReplace(userPatch.getValue(), id);
            }

        }
        else if(userPatch.getAction().equals("delete")){
            if(userPatch.getFieldName().equals("username")) {
                userRepository.deleteUsername(id);
            }

            else if(userPatch.getFieldName().equals("email")) {
                userRepository.deleteEmail(id);
            }

        }


    }

}
