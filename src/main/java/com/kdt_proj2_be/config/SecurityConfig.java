package com.kdt_proj2_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // password BCrypt 암호화
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/**").permitAll()
                .requestMatchers("/swagger-ui.html","v3/api-docs/","/swagger-ui/").permitAll()
        );

        //CORS 해결 코드
        http.cors(cors -> cors.configurationSource(corsSource()));

        // 폼으로 로그인하겠다. 로그인 페이지, 로그인 성공 페이지, 로그인 성공페이지
        http.formLogin((formLogin)
                -> formLogin.loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/fail")
        );

        http.logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }


//    // CORS 허용?
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        System.out.println("SecurityConfig filterChain"); //확인용
//        http.csrf(cf->cf.disable());
//        http.formLogin(frmLogin->frmLogin.disable());
//        http.httpBasic(basic->basic.disable());
//        http.cors(cors->cors.configurationSource(corsSource()));
//        http.authorizeHttpRequests(security->security
//                //.requestMatchers("/child/**").authenticated()
//                //.requestMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().permitAll());
//
//        http.addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()));
//        http.addFilterBefore(new JWTAuthorizationFilter(memRepo), AuthorizationFilter.class);
//        http.oauth2Login(oauth2->oauth2.loginPage("/login").successHandler(successHandler));
//
//        http.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();
//    }
//
//     // CORS 수정전
//    private CorsConfigurationSource corsSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOriginPattern(CorsConfiguration.ALL);
//        config.addAllowedMethod(CorsConfiguration.ALL);
//        config.addAllowedHeader(CorsConfiguration.ALL);
//        config.addExposedHeader("Authorization");
//        config.setAllowCredentials(true);   // 쿠키 전송 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

    // CORS 설정 적용 (프론트엔드에서 접근 가능하도록 수정)
    @Bean
    public CorsConfigurationSource corsSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5000")); // 클라이언트 주소 허용 프론트 3000 플라스트 5000
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 요청 헤더 허용
        config.addExposedHeader("Authorization"); // 응답 헤더 노출
        config.setAllowCredentials(true); // 쿠키 전송 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }



}
