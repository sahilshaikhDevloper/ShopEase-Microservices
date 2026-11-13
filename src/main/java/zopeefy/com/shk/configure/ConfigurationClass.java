package zopeefy.com.shk.configure;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zopeefy.com.shk.JwTserviceAuth.JwtAuthenticationFilter;

@Configuration
public class ConfigurationClass {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public ConfigurationClass(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf->csrf.disable()).sessionManagement
                        (session->session.
                                sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/users/Login",
                                "/api/users/CreateUser"
                        ).permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )

                .httpBasic(basic->basic.disable())
                .formLogin(form->form.disable())
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean

    public FilterRegistrationBean <JwtAuthenticationFilter>
    jwtAuthenticationFilterFilterRegistrationBean
            (JwtAuthenticationFilter filter){
        FilterRegistrationBean <JwtAuthenticationFilter>
                registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }
}
