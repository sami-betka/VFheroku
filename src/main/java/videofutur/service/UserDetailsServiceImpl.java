package videofutur.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import videofutur.repository.AppRoleRepository;
import videofutur.repository.UserAccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
   
   @Autowired
   private UserAccountRepository userRepository;

   @Autowired
   private AppRoleRepository appRoleRepository;

   @Override
   public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       videofutur.model.UserAccount user = userRepository.findByUserName(userName);

       if (user == null) {
           System.out.println("User not found! " + userName);
           throw new UsernameNotFoundException("User " + userName + " was not found in the database");
       }

       System.out.println("Found User: " + user);

       // [ROLE_USER, ROLE_ADMIN,..]
       List<String> roleNames = appRoleRepository.findAllByUserId(user.getUserId());

       List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
       if (roleNames != null) {
           for (String role : roleNames) {
               // ROLE_USER, ROLE_ADMIN,..
               GrantedAuthority authority = new SimpleGrantedAuthority(role);
               grantList.add(authority);
           }
       }

       UserDetails userDetails = (UserDetails) new User(user.getUserName(), //
               user.getEncrytedPassword(), grantList);

       return userDetails;
   }

}