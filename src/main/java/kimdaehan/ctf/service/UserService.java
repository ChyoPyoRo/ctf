package kimdaehan.ctf.service;

import jakarta.annotation.Nullable;
import kimdaehan.ctf.entity.User;
import kimdaehan.ctf.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

   public void changeUser(User existUser, User changeUser){
       existUser.setUserId(changeUser.getUserId());
       //유저 PW 변경
       existUser.setPassword(changeUser.getPassword());
       //유저 이름 변경
       existUser.setName(changeUser.getName());
       //유저 닉네임 변경
       existUser.setNickName(changeUser.getNickName());
       //유저 소속 변경
       existUser.setAffiliation(changeUser.getAffiliation());
       //유저 권한 변경
       existUser.setType(changeUser.getType());
       userRepository.save(existUser);
   }

}
