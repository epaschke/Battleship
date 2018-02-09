package emma.battleship.dao;

import emma.battleship.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GameDao extends CrudRepository<Game, Integer> {
}
