package es.dawgroup2.juding.users;

import es.dawgroup2.juding.auxTypes.refereeRange.RefereeRange;
import es.dawgroup2.juding.auxTypes.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByRolesContaining(Role role);

    List<User> findByRolesContainingAndRefereeRangeNot(Role role, RefereeRange refereeRange);

    List<User> findByRolesContainingAndRefereeRange(Role role, RefereeRange refereeRange);

    Optional<User> findByNickname(String nickname);

    List<User> findByWeightBetween(int min, int max);

    int countUsersByRolesAndRefereeRange(Role role, RefereeRange refereeRange);
}
