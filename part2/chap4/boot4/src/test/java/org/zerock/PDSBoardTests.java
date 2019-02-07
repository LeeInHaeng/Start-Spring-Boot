package org.zerock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.domain.PDSBoard;
import org.zerock.domain.PDSFile;
import org.zerock.presistence.PDSBoardRepository;
import org.zerock.presistence.PDSFileRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Commit
public class PDSBoardTests {

	@Autowired
	PDSBoardRepository repo;
	
	@Autowired
	PDSFileRepository repo2;
	
	/*
	@Test
	public void testInsertPDS() {
		PDSBoard pds = new PDSBoard();
		pds.setPname("Document");
		
		PDSFile file1 = new PDSFile();
		file1.setPdsfile("file1.doc");
		
		PDSFile file2 = new PDSFile();
		file2.setPdsfile("file2.doc");
		
		List<PDSFile> myList = new ArrayList<PDSFile>();
		myList.add(file1);
		myList.add(file2);
		pds.setFiles(myList);
		
		repo.save(pds);
	}

	@Test
	public void insertDummies() {
		List<PDSBoard> list = new ArrayList<>();
		
		IntStream.range(1,100).forEach(i -> {
			PDSBoard pds = new PDSBoard();
			pds.setPname("자료 "+i);
			
			PDSFile file1 = new PDSFile();
			file1.setPdsfile("file1.doc");
			
			PDSFile file2 = new PDSFile();
			file2.setPdsfile("file2.doc");
			
			List<PDSFile> myList = new ArrayList<PDSFile>();
			myList.add(file1);
			myList.add(file2);
			pds.setFiles(myList);
			
			list.add(pds);
		});
		
		repo.saveAll(list);
	}
	
	@Test
	public void testUpdateFileName1() {
		Long fno = 1L;
		String newName = "updateFile1.doc";
		
		int count = repo2.updatePDSFile(fno, newName);
		System.out.println("update count : "+count);
	}
	
	@Test
	public void deletePDSFile() {
		Long fno = 2L;
		int cnt = repo2.deletePDSFile(fno);
		System.out.println(cnt);
	}
	
	@Test
	public void  searchFile() {
		List<Object[]> result = repo2.searchPDSFile();
		System.out.println(result);
	}
	*/
	
	@Test
	public void viewSummary() {
		repo.getSummary().forEach(arr->
				System.out.println(Arrays.toString(arr)));
	}
}
