package org.zerock.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.zerock.domain.Member;
import org.zerock.domain.MemberRole;

public class ZerockSecurityUser extends User {

	private static final String ROLE_PREFIX = "ROLE_";
	
	private Member member;
	
	public ZerockSecurityUser(Member member) {
		
		super(member.getUid(), "{noop}"+member.getUpw(), makeGrantedAuthority(member.getRoles()));
		
		this.member = member;
	}
	
	private static List<GrantedAuthority> makeGrantedAuthority(List<MemberRole> roles){
		
		List<GrantedAuthority> list = new ArrayList<>();
		
		roles.forEach(
				role -> list.add(
						new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName())
						));
		
		return list;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public static String getRolePrefix() {
		return ROLE_PREFIX;
	}
	
	
}
