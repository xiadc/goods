package cn.itcast.goods.order.domain;

import java.util.List;

import cn.itcast.goods.user.domain.User;

/**订单类
 * @author xiadc
 *
 */
public class Order {
	
	private String oid;//主键
	private String ordertime;//下单时间
	private double total;//订单钱总计
	private int status;//订单状态，1未支付，2已支付未发货 ，3已发货未确认收货，4确认收货 ，5已取消（未支付才能取消）
	private String address;//收货地址
	private User user;//订单所属用户
	private List<OrderItem> orderItemList;//订单条目列表
	
	public String getOid() {
		return oid;
	}
	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}
	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
}