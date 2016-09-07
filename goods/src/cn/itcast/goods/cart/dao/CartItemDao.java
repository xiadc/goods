package cn.itcast.goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class CartItemDao {

	private QueryRunner qr = new TxQueryRunner();

	/**根据id加载多条记录
	 * @param cartItemIds
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> loadCartItems(String cartItemIds) throws SQLException{
		Object[] params = cartItemIds.split(",");
		String whereSql = getWhereSql(params.length);
		String sql = "select * from t_cartitem c,t_book b where c.bid = b.bid and"+whereSql;
		return  toCartItemList(qr.query(sql, new MapListHandler(), params));
	}
	
	
	/**按cartItemId查找条目
	 * @param cartItemId
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql = "select * from t_cartitem c,t_book b where c.bid = b.bid and cartItemId = ?";
		 Map<String,Object> map = qr.query(sql, new MapHandler(), cartItemId);
		 return toCartItem(map);
	}
	
	/**生成where子句功能
	 * @param len
	 * @return
	 */
	private String getWhereSql(int len){
		StringBuilder sb = new StringBuilder(" cartItemId in (");
		for(int i=0;i<len;i++){
			sb.append("?");
			if(i<len-1){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**按id批量删除
	 * @param cartItemIds
	 * @throws SQLException
	 */
	public void batchDelete(String cartItemIds) throws SQLException{
		Object[] params = cartItemIds.split(",");
		String whereSql = getWhereSql(params.length);
		String sql = "delete from t_cartitem where"+whereSql;
		qr.update(sql, params);
		
	}
	
	/**按bid和uid查找购物车条目
	 * @param bid
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByBidAndUid(String bid,String uid) throws SQLException{
		String sql = "select * from t_cartitem where bid = ? and uid =?";
		CartItem cartItem =  qr.query(sql, new BeanHandler<CartItem>(CartItem.class), bid,uid);
		return cartItem;
	}
	
	/**修改指定条目的数量
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId, int quantity) throws SQLException{
		String sql = "update t_cartitem set quantity = ? where cartItemId = ?";
		qr.update(sql, quantity,cartItemId);
	}
	
	/**添加条目
	 * @param cartItem
	 * @throws SQLException
	 */
	public void addCartItem(CartItem cartItem) throws SQLException{
		String sql = "insert into t_cartitem(cartItemId,quantity,bid,uid) values (?,?,?,?)";
		Object[] params = {cartItem.getCartItemId(),cartItem.getQuantity(),cartItem.getBook().getBid(),
				cartItem.getUser().getUid()};
		qr.update(sql, params);
	}
	
	/**Map转CartItem对象
	 * @param map
	 * @return
	 */
	private CartItem toCartItem(Map<String,Object> map){
		if(map==null||map.size()==0) return null;
		CartItem cartItem =  CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}
	
	/**MapList转List
	 * @param mapList
	 * @return
	 */
	private List<CartItem> toCartItemList(List<Map<String,Object>> mapList){
		List<CartItem> list = new ArrayList<CartItem>();
		for(Map<String,Object> map : mapList){
			CartItem cartItem = toCartItem(map);
			list.add(cartItem);
		}
		return list;
	}
	
	/**按用户uid查找购物车
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid) throws SQLException{
		String sql = "select * from t_cartitem c,t_book b where c.bid = b.bid and uid = ? order by c.orderBy";
		List<Map<String,Object>> mapList =  qr.query(sql, new MapListHandler(), uid);
		List<CartItem> list = toCartItemList(mapList);
		return list;
	}
}
