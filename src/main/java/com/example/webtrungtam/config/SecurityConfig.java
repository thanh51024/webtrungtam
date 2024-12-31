package com.example.webtrungtam.config;

import com.example.webtrungtam.util.JwtUtil;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String secretKey = "super_secret_key_1234567890123456";
    private static final String TEST_TOKEN = "test-token-12345";
    // Bean để mã hóa mật khẩu bằng Argon2
    @Bean
    public Argon2PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 65536, 10);
    }
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
    @Lazy
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Lazy
    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Mở CORS để hỗ trợ test API qua ngrok
    // Cho phép tất cả domain, header và method để tránh lỗi CORS khi gọi API từ trình duyệt hoặc Swagger UI
    // Chỉ nên bật cấu hình này trong môi trường phát triển, không áp dụng cho môi trường production    @Bean
    // Cấu hình CORS để cho phép các yêu cầu từ bên ngoài
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Cho phép cookie
        config.addAllowedOriginPattern("*"); // Cho phép tất cả origin
        config.addAllowedHeader("*"); // Cho phép tất cả header
        config.addAllowedMethod("*"); // Cho phép tất cả method
        config.addExposedHeader("Authorization"); // Cho phép trả header Authorization
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Cấu hình Swagger với URL động từ ngrok
    @Bean
    public OpenAPI customOpenAPI() {
        String ngrokUrl = getNgrokUrl().orElse("http://localhost:8080"); // Nếu không có, fallback về localhost

        return new OpenAPI()
                .info(new Info().title("API Documentation").version("1.0"))
                .addServersItem(new Server().url(ngrokUrl).description("Dynamic Ngrok URL"));
    }

    private Optional<String> getNgrokUrl() {
        try {
            URL url = new URL("http://localhost:4040/api/tunnels");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Lấy URL từ phản hồi JSON của ngrok
            String jsonResponse = response.toString();
            int startIndex = jsonResponse.indexOf("https://");
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            return Optional.of(jsonResponse.substring(startIndex, endIndex));

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty(); // Trả về mặc định nếu lỗi
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Cho phép tất cả các domain
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Cho phép tất cả header
        configuration.setAllowCredentials(true); // Cho phép gửi cookie
        configuration.addExposedHeader("Authorization"); // Header đặc biệt

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cho tất cả đường dẫn
        return source;
    }

    // Cấu hình bảo mật
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Tắt CSRF để test API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll() // Cho phép API đăng nhập không cần xác thực
                .requestMatchers("/api/auth/login", "/user/search", "/user/messages").permitAll()
                //.requestMatchers("/api/users/me").authenticated() // Cần xác thực
                .requestMatchers("/api/user/**").permitAll()
                .requestMatchers("/ws/chat/**").permitAll() // Cho phép truy cập WebSocket
                .requestMatchers("/admin/**").hasAnyAuthority("Admin") // ADMIN có quyền truy cập trang admin
                .requestMatchers("/teacher/**").hasAnyAuthority("Teacher") // TEACHER truy cập trang giáo viên
                .requestMatchers("/student/**").hasAnyAuthority("Student") // STUDENT truy cập trang học sinh
//                        .anyRequest().authenticated() // Các API khác yêu cầu xác thực
               .anyRequest().permitAll()

            )
            //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Vô hiệu hóa session
            //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Thêm filter JWT

            //.httpBasic(httpBasic -> httpBasic.disable()) // Không dùng HTTP Basic
            //.formLogin(form -> form.disable()); // Không dùng form đăng nhập

            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Sử dụng session
            //.formLogin(form -> form.defaultSuccessUrl("/dashboard").permitAll()) // Bật form đăng nhập
            .logout(logout -> logout.permitAll()); // Bật chức năng đăng xuất

        return http.build();
    }
}

