package org.zerock.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_pdsfiles")
public class PDSFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fno;
	private String pdsfile;
	public Long getFno() {
		return fno;
	}
	public void setFno(Long fno) {
		this.fno = fno;
	}
	public String getPdsfile() {
		return pdsfile;
	}
	public void setPdsfile(String pdsfile) {
		this.pdsfile = pdsfile;
	}
	@Override
	public String toString() {
		return "PDSFile [fno=" + fno + ", pdsfile=" + pdsfile + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fno == null) ? 0 : fno.hashCode());
		result = prime * result + ((pdsfile == null) ? 0 : pdsfile.hashCode());
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
		PDSFile other = (PDSFile) obj;
		if (fno == null) {
			if (other.fno != null)
				return false;
		} else if (!fno.equals(other.fno))
			return false;
		if (pdsfile == null) {
			if (other.pdsfile != null)
				return false;
		} else if (!pdsfile.equals(other.pdsfile))
			return false;
		return true;
	}
	
}
