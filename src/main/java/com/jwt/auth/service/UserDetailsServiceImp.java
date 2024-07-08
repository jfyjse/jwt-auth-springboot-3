package com.jwt.auth.service;


import com.jwt.auth.model.Users;
import com.jwt.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + username));
        return UserDetailsImpl.build(user);

//        return repository.findByUsername(username)
//                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
