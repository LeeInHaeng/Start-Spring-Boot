package org.zerock.presistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.PDSBoard;

public interface PDSBoardRepository extends CrudRepository<PDSBoard, Long> {

	@Query("SELECT p, count(f) FROM PDSBoard p LEFT OUTER JOIN p.files f " + 
			"WHERE p.pid>0 GROUP BY p ORDER BY p.pid DESC")
	public List<Object[]> getSummary();
	
}
