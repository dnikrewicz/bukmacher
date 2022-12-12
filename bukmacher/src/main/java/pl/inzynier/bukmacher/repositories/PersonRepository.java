package pl.inzynier.bukmacher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.inzynier.bukmacher.domain.Coupon;
import pl.inzynier.bukmacher.domain.Person;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT u FROM Person u WHERE u.login =?1")
    Person findByLogin(String login);
}
