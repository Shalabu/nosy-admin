package com.nosy.admin.nosyadmin.controller;

import com.nosy.admin.nosyadmin.model.User;
import com.nosy.admin.nosyadmin.service.UserService_Tmp;
import com.nosy.admin.nosyadmin.utils.Conversion;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = UserController_Tmp.class)
// secure = false disables the security layer
@AutoConfigureMockMvc(secure = false)
public class UserControllerTest_Tmp {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("default_user_service")
    private UserService_Tmp userService;

    @MockBean
    private Conversion conversion;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllUsersEmptyTest() throws Exception {
        List<User> emptyList = Lists.emptyList();

        // mock
        when(userService.getAllUsers()).thenReturn(emptyList);

        mockMvc.perform(get("/api/v1/nosy/user"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService, times(1)).getAllUsers();
        verifyZeroInteractions(conversion);
    }

    @Test
    public void getAllUserTest() throws Exception {
        List<User> list = Lists.list(
                new User() {{
                    setEmail("user1@email.com");
                    setFirstName("first1");
                    setLastName("last1");
                    setPassword("pass1");
                    setInfo("info1");
                }},
                new User() {{
                    setEmail("user2@email.com");
                    setFirstName("first2");
                    setLastName("last2");
                    setPassword("pass2");
                    setInfo("info2");
                }}
        );

        // mock
        when(userService.getAllUsers()).thenReturn(list);
        
        // test
        mockMvc.perform(get("/api/v1/nosy/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.length()", is(2)));

        verify(userService, times(1)).getAllUsers();
        verify(conversion, times(list.size())).convertToUserDto(any());
    }
}
