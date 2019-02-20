package org.zerock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerock.persistence.MemberRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Commit
public class MemberTests {

	@Autowired
	private MemberRepository repo;
	
	@Autowired
	PasswordEncoder pwEncoder;
	
	/*
	@Test
	public void testInsert() {
		for(int i=0; i<=100; i++) {
			Member member = new Member();
			member.setUid("user" + i);
			member.setUpw("pw" + i);
			member.setUname("사용지" + i);
			
			MemberRole role = new MemberRole();
			if(i <= 80) {
				role.setRoleName("BASIC");
			}
			else if(i <= 90) {
				role.setRoleName("MANAGER");
			}
			else {
				role.setRoleName("ADMIN");
			}
			ArrayList<MemberRole> arr = new ArrayList<>();
			arr.add(role);
			member.setRoles(arr);
			
			repo.save(member);
		}
	}

	@Test
	public void testRead() {
		Optional<Member> result = repo.findById("user85");
		
		result.ifPresent(member -> System.out.println(member));
	}
	*/
	
	@Test
	public void testUpdateOldMember() {
		List<String> ids = new ArrayList<>();
		
		for(int i=0; i<=100; i++) {
			ids.add("user"+i);
		}
		
		repo.findAllById(ids).forEach(member -> {
			member.setUpw(pwEncoder.encode(member.getUpw()));
			repo.save(member);
		});
	}
}
