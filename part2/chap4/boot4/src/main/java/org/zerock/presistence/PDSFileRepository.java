package org.zerock.presistence;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.zerock.domain.PDSFile;

public interface PDSFileRepository extends CrudRepository<PDSFile, Long> {
	
	@Modifying
	@Query("UPDATE FROM PDSFile f set f.pdsfile = ?2 WHERE f.fno = ?1 ")
	public int updatePDSFile(Long fno, String newFileName);
	
	@Query("SELECT f FROM PDSFile f")
	public List<Object[]> searchPDSFile();
	
	@Modifying
	@Query("DELETE FROM PDSFile f where f.fno=?1")
	public int deletePDSFile(Long fno);
}
