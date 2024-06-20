//package ru.skillbox.authentication.service;
//
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import ru.skillbox.authentication.entity.User;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class UserService implements UserDetailsService {
//
////    private UserRepository userRepository;
////    private PasswordEncoder passwordEncoder;
////
////    @Autowired
////    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder ){
////        this.userRepository = userRepository;
////        this.passwordEncoder = passwordEncoder;
////    }
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = null;
////        Users user = userRepository.findByEmail(email).get();
//        if (user == null)
//            throw new UsernameNotFoundException("user not found");
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
////        authorities.add(new SimpleGrantedAuthority(user.getRoles().));
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
//    }
//
////    public void createUser(UserDTO user){
////        User user2 = new User();
////        user2.setPassword(user.getPassword1());
////        user2.setEmail(user.getEmail());
////        user2.setFirstName(user.getFirstName());
////        user2.setSecondName(user.getLastName());
////        user2.setPassword(passwordEncoder.encode(user.getPassword1()));
////
////        userRepository.save(user2);
////    }
//}
