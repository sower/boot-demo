//package me.boot.web.config;
//
//import java.io.IOException;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
///**
// * @description
// * @date 2023/06/25
// **/
//@Component
//@Order(-1000)
//public class WebFilter  extends OncePerRequestFilter {
//
//  @Override
//  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//      FilterChain filterChain) throws ServletException, IOException {
//    UsernamePasswordAuthenticationToken authenticationToken =
//        new UsernamePasswordAuthenticationToken(loginUser,null);
//    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//    //放行
//    filterChain.doFilter(request, response);
//  }
//}
