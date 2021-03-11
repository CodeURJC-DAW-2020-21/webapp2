package es.dawgroup2.juding.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /*
     * FETCHING USERS BY THEIR ROLE
     */
    public List<User> getCompetitors(){
        return userRepository.findByRole(1);
    }

    public List<User> getActiveReferees(){
        return userRepository.findByRoleAndRefereeRangeGreaterThanEqual(2, 1);
    }

    public List<User> getRefereeApplications(){
        return userRepository.findByRoleAndRefereeRange(2, 0);
    }

    public int refereePendingApplications(){
        return userRepository.countUsersByRoleAndRefereeRange(2, 0);
    }

    public User getUserOrNull(String licenseId){
        Optional<User> opt = userRepository.findById(licenseId);
        return opt.orElse(null);
    }

    public User save(User user){
        return userRepository.save(user);
    }
}
