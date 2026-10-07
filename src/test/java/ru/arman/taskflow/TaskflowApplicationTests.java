package ru.arman.taskflow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.arman.taskflow.security.CustomUserDetailsService;
import ru.arman.taskflow.security.JwtUtil;

@SpringBootTest
class TaskflowApplicationTests {

    @Test
    void contextLoads() {
    }

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

}
