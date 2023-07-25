package vn.iodev.iosecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import vn.iodev.iosecurity.jwt.JwtAuthenticationFilter;
import vn.iodev.iosecurity.model.TaiKhoanService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
public class WebSecurityConfig {
    @Autowired
    TaiKhoanService taiKhoanService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
        authProvider.setUserDetailsService(taiKhoanService);
        authProvider.setPasswordEncoder(passwordEncoder());
   
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
      return authConfig.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                .antMatchers("/api/authenticate/**", "/api/auth/**").permitAll()
                .antMatchers("/api/mail/**").hasAnyAuthority("VAITRO_QUANTRIHETHONG")
                .antMatchers(HttpMethod.GET, "/api/taikhoans/**").hasAnyAuthority("VAITRO_QUANTRIHETHONG", "VAITRO_NGUOIDUNG", "VAITRO_QUANTRITOCHUC")
                .antMatchers(HttpMethod.PUT, "/api/taikhoans/**").hasAnyAuthority("VAITRO_QUANTRIHETHONG", "VAITRO_NGUOIDUNG", "VAITRO_QUANTRITOCHUC")
                .antMatchers(HttpMethod.POST, "/api/taikhoans/**").hasAnyAuthority("VAITRO_QUANTRIHETHONG")
                .antMatchers(HttpMethod.DELETE, "/api/taikhoans/**").hasAnyAuthority("VAITRO_QUANTRIHETHONG")
                .antMatchers("/api/vaitros/**").hasAnyAuthority("VAITRO_QUANTRIHETHONG")
            .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
