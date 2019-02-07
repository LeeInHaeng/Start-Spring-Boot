package org.zerock.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="tbl_free_replies", indexes = {@Index(unique=false, columnList="board_bno")} )
public class FreeBoardReply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rno;
	private String reply;
	private String replyer;
	
	@CreationTimestamp
	private Timestamp replydate;
	@CreationTimestamp
	private Timestamp updatedate;
	
	@ManyToOne
	private FreeBoard board;
	
	public Long getRno() {
		return rno;
	}
	public void setRno(Long rno) {
		this.rno = rno;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getReplyer() {
		return replyer;
	}
	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}
	public Timestamp getReplydate() {
		return replydate;
	}
	public void setReplydate(Timestamp replydate) {
		this.replydate = replydate;
	}
	public Timestamp getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Timestamp updatedate) {
		this.updatedate = updatedate;
	}
	public FreeBoard getBoard() {
		return board;
	}
	public void setBoard(FreeBoard board) {
		this.board = board;
	}
	@Override
	public String toString() {
		return "FreeBoardReply [rno=" + rno + ", reply=" + reply + ", replyer=" + replyer + ", replydate=" + replydate
				+ ", updatedate=" + updatedate + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rno == null) ? 0 : rno.hashCode());
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
		FreeBoardReply other = (FreeBoardReply) obj;
		if (rno == null) {
			if (other.rno != null)
				return false;
		} else if (!rno.equals(other.rno))
			return false;
		return true;
	}
	
	
}
