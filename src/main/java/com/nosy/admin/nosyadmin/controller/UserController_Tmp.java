package com.nosy.admin.nosyadmin.controller;

import com.nosy.admin.nosyadmin.dto.UserDto;
import com.nosy.admin.nosyadmin.service.UserService_Tmp;
import com.nosy.admin.nosyadmin.utils.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin")
@RequestMapping("/api/v1/nosy/user")
public class UserController_Tmp {
    // This is an interface, thus if we need to change the
    // implementation now is very easy. And we can mock.
    private final UserService_Tmp userService;
    private final Conversion conversion;

    @Autowired
    public UserController_Tmp(@Qualifier("default_user_service") UserService_Tmp userService,
                              Conversion conversion) {
        this.userService = userService;
        this.conversion = conversion;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(conversion::convertToUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "{email}")
    public UserDto getUserByEmail(@Email String email) throws Exception {
        return userService.getUserByEmail(email)
                .map(conversion::convertToUserDto)
                .orElseThrow(Exception::new);
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) throws Exception {
        return userService.createUser( conversion.convertToUser(userDto) )
                .map(conversion::convertToUserDto)
                .orElseThrow(Exception::new);
    }
}
