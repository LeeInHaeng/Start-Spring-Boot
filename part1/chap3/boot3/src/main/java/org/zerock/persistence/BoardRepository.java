package org.zerock.persistence;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.zerock.domain.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {

	public List<Board> findBoardByTitle(String title);
	
	public Collection<Board> findByWriter(String writer);
	
	public Collection<Board> findByWriterContaining(String writer);
	
	public Collection<Board> findByTitleContainingOrContentContaining(String title, String content);
	
	// title like % ? % and bno > ?
	public Collection<Board> findByTitleContainingAndBnoGreaterThan(String keyword, Long num);
	
	// bno > ? order by bno desc
	public Collection<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno);
	
	// bno > ? order by bno desc limit ?, ?
	public Page<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno, Pageable paging);
	
	@Query("SELECT b.bno, b.title, b.content " + 
	"FROM Board b WHERE b.content LIKE %:content% AND b.bno>0 ORDER BY b.bno DESC")
	public Page<Object[]> findByContentPage(@Param("content") String content, Pageable paging);
	
}
