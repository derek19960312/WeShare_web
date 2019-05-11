package android.com.goodslike.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import android.com.goods.model.GoodsVO;
import android.com.member.model.MemberVO;
@Entity
@Table(name = "GOODSLIKE")
public class GoodsLikeVO implements Serializable{


	private static final long serialVersionUID = 1L;
	private GoodsVO goodsVO;
	private MemberVO memberVO;
	
	@Id
	@ManyToOne()
	@JoinColumn(name = "GOODID") 
	public GoodsVO getGoodId() {
		return goodsVO;
	}
	
	public void setGoodId(GoodsVO goodsVO) {
		this.goodsVO = goodsVO;
	}
	
	
	@Id
	@ManyToOne()
	@JoinColumn(name = "MEMBERID") 
	public MemberVO getMemId() {
		return memberVO;
	}
	public void setMemId(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	
}
