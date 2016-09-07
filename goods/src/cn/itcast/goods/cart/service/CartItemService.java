package cn.itcast.goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.cart.dao.CartItemDao;
import cn.itcast.goods.cart.domain.CartItem;

public class CartItemService {

	private CartItemDao cartItemDao = new CartItemDao();

	/**加载多条记录
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**更新条目数量
	 * @param cartItemId
	 * @param quantity
	 * @return
	 */
	public CartItem updateQuantity(String cartItemId,int quantity){
		try {
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**批量删除功能
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds){
		try {
			cartItemDao.batchDelete(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按uid查找
	 * 
	 * @param uid
	 * @return
	 */
	public List<CartItem> findByUid(String uid) {
		try {
			return cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**添加购物车条目
	 * @param cartItem
	 */
	public void addCartItem(CartItem cartItem) {
		try {
			CartItem _cartItem = cartItemDao.findByBidAndUid(cartItem.getBook()
					.getBid(), cartItem.getUser().getUid());

			if (_cartItem == null) {// 如果数据库中不存在该条目，则添加该条目到数据库
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			} else {//如果数据库中存在该条目
				int quantity = cartItem.getQuantity()+_cartItem.getQuantity();
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
