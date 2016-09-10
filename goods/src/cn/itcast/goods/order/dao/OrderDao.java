package cn.itcast.goods.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * @author xiadc
 *
 */
public class OrderDao {
	
	private QueryRunner qr = new TxQueryRunner();
	
	/**按id查询订单状态
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public int findStatus(String oid) throws SQLException{
		String sql = "select status from t_order where oid = ?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), oid);
		return number.intValue();
	}
	
	/**更新状态
	 * @param oid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String oid ,int status) throws SQLException{
		String sql = "update t_order set status = ? where oid = ?";
		qr.update(sql, status,oid);
	}
	
	/**通用查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Order> findByCriteria(List<Expression> exprList, int pc) throws SQLException{
		//1.得到ps
		int ps = PageConstants.ORDER_PAGE_SIZE;
		
		StringBuilder whereSql = new StringBuilder("where 1=1");
		List<Object> params = new ArrayList<Object>();//对应问号的值
		for(Expression expr : exprList){
			whereSql.append(" and ").append(expr.getName()).append(" ")
			.append(expr.getOperation()).append(" ");
			if(!expr.getOperation().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		
		//2.总记录数
		String sql = "select count(*) from t_order "+ whereSql;
		Number number =  (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		
		//3.得到beanList，即当前页记录数
		 sql = "select * from t_order " + whereSql +" order by ordertime desc limit ?,?";
		 params.add((pc-1)*ps);//当前页首行记录下标
		 params.add(ps);//每页记录数
		 List<Order> orderList =  qr.query(sql, new BeanListHandler<Order>(Order.class), params.toArray());
		 
		 for(Order order : orderList){
			 List<OrderItem> orderItemList  = loadOrderItems(order);
			 order.setOrderItemList(orderItemList);
		 }
		 
		 //4.创建PageBean，设置参数
		 PageBean<Order> pb = new PageBean<Order>();
		 pb.setBeanList(orderList);
		 pb.setPc(pc);
		 pb.setPs(ps);
		 pb.setTr(tr);
		 
		 return pb;
	}
	
	/**根据订单获取订单条目列表
	 * @param order
	 * @return
	 * @throws SQLException
	 */
	private List<OrderItem> loadOrderItems(Order order) throws SQLException {
		String sql = "select * from t_orderitem where oid = ?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
		return toOrderItemList(mapList);
	}

	/**MapList转OrderItemList
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> list = new ArrayList<OrderItem>();
		for(Map<String,Object> map: mapList){
			OrderItem orderItem =  toOrder(map);
			list.add(orderItem);
		}
		return list;
	}

	/**map转Order对象
	 * @param map
	 * @return
	 */
	private OrderItem toOrder(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	/**按用户查找订单
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid", "=", uid));
		return findByCriteria(exprList, pc);
	}
	
	/**新增订单
	 * @param order
	 * @throws SQLException 
	 */
	public void addOrder(Order order) throws SQLException{
		//1.添加订单
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = {order.getOid(),order.getOrdertime(),order.getTotal(),
				order.getStatus(),order.getAddress(),order.getUser().getUid()};
		qr.update(sql, params);
		//2.添加订单条目
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		Object[][] objs= new Object[order.getOrderItemList().size()][];
		for(int i = 0 ;i<objs.length;i++){
			OrderItem orderItem = order.getOrderItemList().get(i);
			objs[i] = new Object[]{orderItem.getOrderItemId(),orderItem.getQuantity(),orderItem.getSubtotal(),
					orderItem.getBook().getBid(),orderItem.getBook().getBname(),orderItem.getBook().getCurrPrice(),
					orderItem.getBook().getImage_b(),orderItem.getOrder().getOid()};	
			}
		qr.batch(sql, objs);
		}
	
	/**按oid查询订单
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByOid(String oid) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("oid", "=", oid));
		return findByCriteria(exprList, 1);//按id查询结果只有一个，所以分页没有意义，这里直接给1，返回的结果实际只有一个Order对象
	}
	
	/**查询所有订单
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findAll(int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		return findByCriteria(exprList, pc);
	}
	
	/**按状态查询订单
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByStatus(int status,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status", "=", status+""));
		return findByCriteria(exprList, pc);
	}
}
