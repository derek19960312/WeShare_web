package com.goodsorder.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goods.model.GoodsVO;
import com.goodsdetails.model.GoodsDetailsService;
import com.goodsorder.model.GoodsOrderService;
import com.goodsorder.model.GoodsOrderVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.withdrawalrecord.model.WithdrawalRecordService;


@WebServlet("/android/GoodsOrderServlet")
public class GoodsOrderServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Gson gson = new GsonBuilder()
				
                 .setDateFormat("yyyy-MM-dd HH:mm:ss")
                 .create(); 
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = res.getWriter();
		String action = req.getParameter("action");
		
		
		if("find_my_good_by_memId".equals(action)) {
			
			
			String memId = req.getParameter("memId");
			GoodsOrderService goSvc = new GoodsOrderService();
			List<GoodsOrderVO> gvos = goSvc.findGoodByMemId(memId);
			
			out.print(gson.toJson(gvos));
			
			
		}
		
		if("add_new_good_order".equals(action)) {
			
			GoodsOrderVO goodsOrderVO = gson.fromJson(req.getParameter("goodsOrderVO"),GoodsOrderVO.class);
			//取出memId 比對餘額是否充足
			String memId = goodsOrderVO.getMemId();
			MemberService memSvc = new MemberService();
			MemberVO memberVO = memSvc.getOneMember(memId);
			if(memberVO.getMemBalance() < goodsOrderVO.getGoodTotalPrice()) {
				//餘額不足
				out.print("Insufficient_account_balance");
				return;
			}
			
			//取出我的車
			Type mapType = new TypeToken<Map<GoodsVO,Integer>> (){}.getType();
			Map<GoodsVO,Integer> myCart = gson.fromJson(req.getParameter("myCart"), mapType);
			
			
			
			//建立訂單VO
			goodsOrderVO.setBuyerName(memberVO.getMemName());
			goodsOrderVO.setBuyerAddress(memberVO.getMemAdd());
			goodsOrderVO.setBuyerPhone(memberVO.getMemPhone());
			goodsOrderVO.setGoodOrdStatus(1);
		
			//建立訂單Service
			GoodsOrderService goodsOrderSvc = new GoodsOrderService();
			
			goodsOrderSvc.insert(goodsOrderVO,myCart);
			
			//扣除款項
			memberVO.setMemBalance(memberVO.getMemBalance() - goodsOrderVO.getGoodTotalPrice());
			memSvc.update_balance(memberVO);
			
			//新增交易紀錄
			WithdrawalRecordService wrSvc = new WithdrawalRecordService();
			wrSvc.addWithdrawalRecord(memberVO.getMemId(), -goodsOrderVO.getGoodTotalPrice(), new Date(new GregorianCalendar().getTimeInMillis()));
			
			
			
			out.print("Success");
			
		}
		
		
		
	}

}
