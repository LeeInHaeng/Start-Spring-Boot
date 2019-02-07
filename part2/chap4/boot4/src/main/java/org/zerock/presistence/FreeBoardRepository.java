package org.zerock.presistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.FreeBoard;

public interface FreeBoardRepository extends CrudRepository<FreeBoard, Long> {

	@Query("SELECT b.bno, b.title, count(r) FROM FreeBoard b "+
			"LEFT OUTER JOIN b.replies r "+
			"WHERE b.bno > 0 GROUP BY b")
	public List<Object[]> getPage(Pageable page);
	
	@Query("SELECT b,r FROM FreeBoard b LEFT OUTER JOIN b.replies r"
			+ " WHERE b.bno=?1 ORDER BY r.rno DESC")
	public List<Object[]> getIndex(Long board_bno);
}
