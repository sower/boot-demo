//package me.boot.web.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
///**
// * @description
// * @date 2023/06/17
// **/
//@Configuration
//@EnableWebSecurity
//// 开启注解设置权限
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig{
//
//  // 配置密码加密器
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
//
//  @Bean
//  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
////    final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();
////    configurers.add(new GlobalAuthenticationConfigurerAdapter() {
////                      @Override
////                      public void configure(AuthenticationManagerBuilder auth){
////                        // auth.doSomething()
////                      }
////                    }
////    );
//    return authConfig.getAuthenticationManager();
//  }
//
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    return http
//        .csrf().disable()
//        .cors().and()
//        .authorizeRequests()
//        .antMatchers("/h2").permitAll()
//        .antMatchers("/index").permitAll()
//        .antMatchers(HttpMethod.OPTIONS).permitAll()
////        .anyRequest().authenticated()
//        .anyRequest().permitAll()
//        .and()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and().build();
//  }
//
//}
