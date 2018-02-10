package emma.battleship.dao;

import emma.battleship.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface GameDao extends CrudRepository<Game, Integer> {
    public List<Game> findAll();
    public Game findById(Integer gameId);
    public <S extends Game> S save(S Game);
    public void delete(Integer gameId);
}
