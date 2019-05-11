package android.com.goodslike.model;

import java.util.List;


public class GoodsLikeService {
	private GoodsLikeDAO_interface dao;
	
	public GoodsLikeService() {
		dao = new GoodsLikeDAO();
	}
	
	public void addGoodsLike(GoodsLikeVO goodsLikeVO) {
		dao.insert(goodsLikeVO);
	}

	public void deleteGoodsLike(GoodsLikeVO goodsLikeVO) {
		dao.delete(goodsLikeVO);
	}

	public List<GoodsLikeVO> getAll() {
		return dao.getAll();
	}
	
	
	public List<GoodsLikeVO> findByAnyGoodsLikeVO(GoodsLikeVO goodsLikeVO) {
		return dao.findByAnyGoodsLikeVO(goodsLikeVO);
	}

}
