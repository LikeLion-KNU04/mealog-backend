package knulions.mealog.domain.Image.repository;

import knulions.mealog.domain.Image.entity.Image;
import knulions.mealog.domain.board.entity.Board;
import knulions.mealog.domain.market.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {


    @Query(value = "SELECT * FROM image WHERE program_id = :boardId ORDER BY image_id", nativeQuery = true)
    Image findFirstImageByBoard(@Param("boardId") Long boardId);

    List<Image> findAllByBoardId(Long boardId);
    List<Image> findByBoard(Optional<Board> board);

    List<Image> findAllByMarketId(Long marketId);
    List<Image> findByMarket(Optional<Market> market);

    void deleteAllByBoardId(Long id);
}
