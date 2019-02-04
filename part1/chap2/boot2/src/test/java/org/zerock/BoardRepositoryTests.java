package org.zerock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.Board;
import org.zerock.persistence.BoardRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardRepositoryTests {

	@Autowired
	private BoardRepository boardRepo;
	
	/*
	@Test
	public void testInsert() {
		Board board = new Board();
		board.setTitle("게시물의 제목");
		board.setContent("게시물 내용 넣기....");
		board.setWriter("user00");
		
		boardRepo.save(board);
	}
	
	@Test
	public void testRead() {
		boardRepo.findById(1L).ifPresent((board)->{
			System.out.println(board);
		});
	}

	@Test
	public void testUpdate() {
		System.out.println("Read First..................................");
		boardRepo.findById(1L).ifPresent((board)->{
			
			System.out.println("Update Title.................................");
			board.setTitle("수정된 제목입니다.");
			
			System.out.println("Call Save().....................................");
			boardRepo.save(board);
			
		});
		
	}
	*/
	
	@Test
	public void testDelete() {
		System.out.println("delete entity");
		
		boardRepo.deleteById(1L);
	}
}
