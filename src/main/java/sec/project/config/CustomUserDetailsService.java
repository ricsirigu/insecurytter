package sec.project.config;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.repository.AccountRepository;
import sec.project.repository.MessageRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;
     
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Account account = new Account();
        account.setUsername("mallory");
        account.setPassword(passwordEncoder.encode("mallory"));
        accountRepository.save(account);
        
        account = new Account();
        account.setUsername("bob");
        account.setPassword(passwordEncoder.encode("bob"));
        accountRepository.save(account);
        
        Message m = new Message("Hi everybody, I'm Bob");
        account = accountRepository.findByUsername("bob");
        m.setAccount(account);
        messageRepository.save(m);
        
        m = new Message("ehheeh ;-)");
        account = accountRepository.findByUsername("mallory");
        m.setAccount(account);
        messageRepository.save(m);
        
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
