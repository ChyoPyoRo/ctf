package kimdaehan.ctf.service;

import jakarta.annotation.Nullable;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    @NonNull
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        return userRepository.findByUserId(userId).orElseThrow(() -> {
            String message = String.format("Username \"%s\" does not exist!", userId);
            return new UsernameNotFoundException(message);
        });
    }

    public void upsertUser(User user){
        userRepository.save(user);
    }

    public User getUserId(String userId){
        return userRepository.findByUserId(userId).orElse(null);
    }
   public List<User> getAllUser(){
        return userRepository.findAll();
   }

}
