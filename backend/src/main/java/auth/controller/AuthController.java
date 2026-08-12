package auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.model.User;
import com.example.auth.service.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> request
    ) {

        try {

            String name = request.get("name");
            String email = request.get("email");
            String password = request.get("password");

            User user
                    = authService.register(
                            name,
                            email,
                            password
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Registration successful"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request,
            HttpSession session
    ) {

        try {

            String email = request.get("email");
            String password = request.get("password");

            User user
                    = authService.login(
                            email,
                            password
                    );

            session.setAttribute(
                    "userId",
                    user.getId()
            );

            session.setAttribute(
                    "userName",
                    user.getName()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Login successful"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            HttpSession session
    ) {

        Object userId
                = session.getAttribute("userId");

        Object userName
                = session.getAttribute("userName");

        if (userId == null) {

            return ResponseEntity
                    .status(401)
                    .body(
                            Map.of(
                                    "message",
                                    "Not logged in"
                            )
                    );
        }

        return ResponseEntity.ok(
                Map.of(
                        "id", userId,
                        "name", userName
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Logout successful"
                )
        );
    }
}
