package ru.arman.taskflow.service;

import org.springframework.stereotype.Service;
import ru.arman.taskflow.dto.UserCreateRequest;
import ru.arman.taskflow.dto.UserResponse;
import ru.arman.taskflow.exception.UserNotFoundException;
import ru.arman.taskflow.mapper.UserMapper;
import ru.arman.taskflow.model.User;
import ru.arman.taskflow.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponse createUser(UserCreateRequest request){
        if (userRepository.existByEmail(request.email())){
            throw new IllegalArgumentException("Пользователь с email " + request.email() + " уже существует");
        }
        User user = UserMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    public UserResponse getuserById(Long id){
        User user = findUserOrThrow(id);
        return UserMapper.toResponse(user);
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)){
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

    private User findUserOrThrow(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
    }
}
