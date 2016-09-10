package cn.itcast.goods.order.service;

import java.sql.SQLException;

import cn.itcast.goods.order.dao.OrderDao;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {

	private OrderDao orderDao = new OrderDao();
	
	/**按id查询状态
	 * @param oid
	 * @return
	 */
	public int findStatus(String oid){
		try {
			return orderDao.findStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**更新状态
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid,int status){
		try {
			 orderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**我的订单
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid,int pc){
		try {
			JdbcUtils.beginTransaction();//开始事务
			PageBean<Order> pb = orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**添加订单
	 * @param order
	 */
	public void addOrder(Order order){
		try {
			JdbcUtils.beginTransaction();//开始事务
			 orderDao.addOrder(order);
			JdbcUtils.commitTransaction();		
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**按id查询订单
	 * @param order
	 */
	public PageBean<Order> findByOid(String oid){
		try {
			JdbcUtils.beginTransaction();//开始事务
			PageBean<Order> pb = orderDao.findByOid(oid);
			JdbcUtils.commitTransaction();	
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	/**查询所有订单
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findAll(int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb= orderDao.findAll(pc);
			JdbcUtils.commitTransaction();	
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	
	/**按状态查询订单
	 * @param status
	 * @param pc
	 * @return
	 */
	public PageBean<Order> findByStatus(int status,int pc){
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb= orderDao.findByStatus(status, pc);
			JdbcUtils.commitTransaction();	
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
}
