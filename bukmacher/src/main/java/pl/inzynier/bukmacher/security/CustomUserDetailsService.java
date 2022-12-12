package pl.inzynier.bukmacher.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.inzynier.bukmacher.domain.Person;
import pl.inzynier.bukmacher.repositories.PersonRepository;


public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException{
        Person person = personRepository.findByLogin(login);
        if(person == null){
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(person);
    }
}
