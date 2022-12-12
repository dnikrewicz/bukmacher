package pl.inzynier.bukmacher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.inzynier.bukmacher.domain.Coupon;
import pl.inzynier.bukmacher.domain.Person;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT u FROM Coupon u WHERE u.person =?1")
    List<Coupon> findByPersonID(@Param("person")Person person);

}
