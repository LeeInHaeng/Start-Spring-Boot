package org.zerock;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Board;
import org.zerock.persistence.BoardRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Boot3ApplicationTests {

	@Autowired
	private BoardRepository repo;
	
	/*
	@Test
	public void testInsert200() {
		for(int i=1; i<=200; i++) {
			Board board = new Board();
			board.setTitle("제목..." + i);
			board.setContent("내용 ..." + i + " 채우기");
			board.setWriter("user0" + (i % 10));
			repo.save(board);
		}
	}
	
	@Test
	public void testByTitle() {
		repo.findBoardByTitle("제목...177")
		.forEach(board -> System.out.println(board));
	}
	
	@Test
	public void testByWriter() {
		repo.findByWriter("user00")
		.forEach(board -> System.out.println(board));
	}

	@Test
	public void testByWriterContaining() {
		repo.findByWriterContaining("05")
		.forEach(board -> System.out.println(board));
	}

	@Test
	public void testByTitleContainingOrContentContaining() {
		repo.findByTitleContainingOrContentContaining("1", "내용 ...5")
		.forEach(board -> System.out.println(board));
	}

	@Test
	public void testByTitleAndBno() {
		repo.findByTitleContainingAndBnoGreaterThan("5", 50L)
		.forEach(board -> System.out.println(board));
	}
	
	@Test
	public void testBnoOrderBy() {
		repo.findByBnoGreaterThanOrderByBnoDesc(90L)
		.forEach(board -> System.out.println(board));
	}

	@Test
	public void testBnoPaging() {
		repo.findByBnoGreaterThanOrderByBnoDesc(0L, PageRequest.of(0, 10))
		.forEach(board -> System.out.println(board));
	}
	*/
	
	@Test
	public void testContentPaging() {
		repo.findByContentPage("내용 ...1", PageRequest.of(0, 10))
		.forEach(board -> System.out.println(board));
	}
	
}

