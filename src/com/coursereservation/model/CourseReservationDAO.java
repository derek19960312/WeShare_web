package com.coursereservation.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CourseReservationDAO implements CourseReservationDAO_interface {
	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	//新增
		private static final String INSERT_STMT = "Insert into CourseReservation values ('CR'||LPAD(to_char(CourseReservation_seq.NEXTVAL),5,'0'),sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		//修改
		private static final String UPDATE = "UPDATE CourseReservation set teamId=?, crvStatus=?, classStatus=?, TranStatus=? , crvScore=?, crvRate=? where crvId = ?";
		
		//查詢全部
		private static final String GET_ALL_STMT = "SELECT * FROM CourseReservation";
		
		//複合查詢1
		private static final String GET_XXXID_STMT = "SELECT * FROM CourseReservation where (case when crvId=? then 1 else 0 end+ case when teacherId=? then 1 else 0 end+ case when memId=? then 1 else 0 end)>=1";
		
		//複合查詢2
		private static final String GET_STATUS_STMT ="SELECT * FROM CourseReservation where (case when crvStatus=? then 1 else 0 end+ case when classStatus=? then 1 else 0 end+ case when tranStatus=? then 1 else 0 end)>=1";
		

	@Override
	public void insert(CourseReservationVO courseReservationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			pstmt.setString(1,courseReservationVO.getTeacherId());
			pstmt.setString(2,courseReservationVO.getMemId());
			pstmt.setString(3,courseReservationVO.getInscId());
			pstmt.setString(4,courseReservationVO.getTeamId());
			pstmt.setInt(5,courseReservationVO.getCrvStatus());
			pstmt.setInt(6,courseReservationVO.getClassStatus());
			pstmt.setInt(7,courseReservationVO.getTranStatus());
			pstmt.setTimestamp(8,courseReservationVO.getCrvMFD());
			pstmt.setTimestamp(9,courseReservationVO.getCrvEXP());
			pstmt.setString(10,courseReservationVO.getCrvLoc());
			pstmt.setInt(11,courseReservationVO.getCrvTotalTime());
			pstmt.setInt(12,courseReservationVO.getCrvTotalPrice());
			pstmt.setDouble(13,courseReservationVO.getCrvScore());
			pstmt.setString(14, courseReservationVO.getCrvRate());
			pstmt.executeUpdate();
			System.out.println("已新增一筆資料");
			
		}catch(SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public void update(CourseReservationVO courseReservationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1,courseReservationVO.getTeamId());
			pstmt.setInt(2,courseReservationVO.getCrvStatus());
			pstmt.setInt(3,courseReservationVO.getClassStatus());
			pstmt.setInt(4,courseReservationVO.getTranStatus());
			pstmt.setDouble(5,courseReservationVO.getCrvScore());
			pstmt.setString(6, courseReservationVO.getCrvRate());
			pstmt.setString(7, courseReservationVO.getCrvId());
			pstmt.executeUpdate();
			System.out.println("已修改一筆資料");

		}catch(SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public List<CourseReservationVO> findByStatus(Integer xxxStatus) {
		List<CourseReservationVO> list = new ArrayList<CourseReservationVO>();
		CourseReservationVO courseReservationVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_STATUS_STMT);
			pstmt.setInt(1, xxxStatus);
			pstmt.setInt(2, xxxStatus);
			pstmt.setInt(3, xxxStatus);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				courseReservationVO = new CourseReservationVO();
				courseReservationVO.setCrvId(rs.getString("crvId"));
				courseReservationVO.setTeacherId(rs.getString("teacherId"));
				courseReservationVO.setMemId(rs.getString("memId"));
				courseReservationVO.setInscId(rs.getString("inscId"));
				courseReservationVO.setTeamId(rs.getString("teamId"));
				courseReservationVO.setCrvStatus(rs.getInt("crvStatus"));
				courseReservationVO.setClassStatus(rs.getInt("classStatus"));
				courseReservationVO.setTranStatus(rs.getInt("tranStatus"));
				courseReservationVO.setCrvMFD(rs.getTimestamp("crvMFD"));
				courseReservationVO.setCrvEXP(rs.getTimestamp("crvEXP"));
				courseReservationVO.setCrvLoc(rs.getString("crvLoc"));
				courseReservationVO.setCrvTotalTime(rs.getInt("crvTotalTime"));
				courseReservationVO.setCrvTotalPrice(rs.getInt("crvTotalPrice"));
				courseReservationVO.setCrvScore(rs.getDouble("crvScore"));
				courseReservationVO.setCrvRate(rs.getString("crvRate"));
				list.add(courseReservationVO); // Store the row in the list
			}
		}catch(SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	@Override
	public List<CourseReservationVO> findByPrimaryKey(String xxxId) {
		List<CourseReservationVO> list = new ArrayList<CourseReservationVO>();
		CourseReservationVO courseReservationVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_XXXID_STMT);
			pstmt.setString(1, xxxId);
			pstmt.setString(2, xxxId);
			pstmt.setString(3, xxxId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				courseReservationVO = new CourseReservationVO();
				courseReservationVO.setCrvId(rs.getString("crvId"));
				courseReservationVO.setTeacherId(rs.getString("teacherId"));
				courseReservationVO.setMemId(rs.getString("memId"));
				courseReservationVO.setInscId(rs.getString("inscId"));
				courseReservationVO.setTeamId(rs.getString("teamId"));
				courseReservationVO.setCrvStatus(rs.getInt("crvStatus"));
				courseReservationVO.setClassStatus(rs.getInt("classStatus"));
				courseReservationVO.setTranStatus(rs.getInt("tranStatus"));
				courseReservationVO.setCrvMFD(rs.getTimestamp("crvMFD"));
				courseReservationVO.setCrvEXP(rs.getTimestamp("crvEXP"));
				courseReservationVO.setCrvLoc(rs.getString("crvLoc"));
				courseReservationVO.setCrvTotalTime(rs.getInt("crvTotalTime"));
				courseReservationVO.setCrvTotalPrice(rs.getInt("crvTotalPrice"));
				courseReservationVO.setCrvScore(rs.getDouble("crvScore"));
				courseReservationVO.setCrvRate(rs.getString("crvRate"));
				list.add(courseReservationVO); // Store the row in the list
			}
		}catch(SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	@Override
	public List<CourseReservationVO> getAll() {
		List<CourseReservationVO> list = new ArrayList<CourseReservationVO>();
		CourseReservationVO courseReservationVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				courseReservationVO = new CourseReservationVO();
				courseReservationVO.setCrvId(rs.getString("crvId"));
				courseReservationVO.setTeacherId(rs.getString("teacherId"));
				courseReservationVO.setMemId(rs.getString("memId"));
				courseReservationVO.setInscId(rs.getString("inscId"));
				courseReservationVO.setTeamId(rs.getString("teamId"));
				courseReservationVO.setCrvStatus(rs.getInt("crvStatus"));
				courseReservationVO.setClassStatus(rs.getInt("classStatus"));
				courseReservationVO.setTranStatus(rs.getInt("tranStatus"));
				courseReservationVO.setCrvMFD(rs.getTimestamp("crvMFD"));
				courseReservationVO.setCrvEXP(rs.getTimestamp("crvEXP"));
				courseReservationVO.setCrvLoc(rs.getString("crvLoc"));
				courseReservationVO.setCrvTotalTime(rs.getInt("crvTotalTime"));
				courseReservationVO.setCrvTotalPrice(rs.getInt("crvTotalPrice"));
				courseReservationVO.setCrvScore(rs.getDouble("crvScore"));
				courseReservationVO.setCrvRate(rs.getString("crvRate"));
				list.add(courseReservationVO); // Store the row in the list
			}
		}catch(SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

}
