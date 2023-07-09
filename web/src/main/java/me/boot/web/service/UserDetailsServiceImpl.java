//package me.boot.web.service;
//
//import java.util.Optional;
//import javax.annotation.Resource;
//import me.boot.web.repository.UserDao;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * @description
// * @date 2023/06/17
// **/
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//  @Resource
//  private UserDao userDao;
//  // 自定义认证逻辑
//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//    Optional<me.boot.web.bean.User> optional = userDao.findByName(username);
//    if(optional.isEmpty()) {
//      throw new UsernameNotFoundException("Not Found User");
//    }
//    me.boot.web.bean.User user = optional.get();
//    UserDetails userDetails = User
//        .withUsername(user.getName())
//        .password(user.getPassword())
//        .authorities(user.getRoles().toArray(new String[0]))
//        .build();
//    return userDetails;
//  }
//}