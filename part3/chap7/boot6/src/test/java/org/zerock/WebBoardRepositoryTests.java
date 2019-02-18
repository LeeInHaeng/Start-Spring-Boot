package org.zerock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.WebBoard;
import org.zerock.persistence.WebBoardRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebBoardRepositoryTests {

	@Autowired
	WebBoardRepository repo;
	
	/*
	@Test
	public void insertBoardDummies() {
		IntStream.range(0,300).forEach(i -> {
			WebBoard board = new WebBoard();
			
			board.setTitle("Sample Boarrd Title "+i);
			board.setContent("Content Sample ..." + i + " of Board");
			board.setWriter("user0" + (i%10));
			
			repo.save(board);
		});
	}
	*/
	
	@Test
	public void testList2() {
		Pageable pageable = PageRequest.of(0, 20, Direction.DESC, "bno");
		
		Page<WebBoard> result = repo.findAll(repo.makePredicate("t", "10"),pageable);
		
		System.out.println("PAGE : " + result.getPageable());
		
		result.getContent().forEach(board -> System.out.println(board));
	}
}
