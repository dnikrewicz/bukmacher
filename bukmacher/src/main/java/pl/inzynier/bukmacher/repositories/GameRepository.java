package pl.inzynier.bukmacher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.inzynier.bukmacher.domain.Game;
import pl.inzynier.bukmacher.domain.Person;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT u FROM Game u WHERE u.id =?1")
    Game findGameById(long id);
}
