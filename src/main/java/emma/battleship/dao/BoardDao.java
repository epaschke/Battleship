package emma.battleship.dao;

import emma.battleship.model.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BoardDao extends CrudRepository<Board, Integer> {
    public List<Board> findAll();
    public Board findById(Integer boardId);
    public <S extends Board> S save(S Board);
    public void delete(Integer boardId);
}
