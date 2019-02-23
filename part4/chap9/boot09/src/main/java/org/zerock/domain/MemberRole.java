package org.zerock.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_member_roles")
public class MemberRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fno;
	
	private String roleName;

	public Long getFno() {
		return fno;
	}

	public void setFno(Long fno) {
		this.fno = fno;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fno == null) ? 0 : fno.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberRole other = (MemberRole) obj;
		if (fno == null) {
			if (other.fno != null)
				return false;
		} else if (!fno.equals(other.fno))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MemberRole [fno=" + fno + ", roleName=" + roleName + "]";
	}
	
	
}
