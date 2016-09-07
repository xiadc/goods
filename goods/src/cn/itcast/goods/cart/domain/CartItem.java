package cn.itcast.goods.cart.domain;

import java.math.BigDecimal;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.user.domain.User;

/**购物车条目类
 * @author xiadc
 *
 */
public class CartItem {

	private String cartItemId;//id
	private int quantity;//数量
	private Book book;//书
	private User user;//用户
	
	
	/**小计
	 * @return
	 */
	public double getSumTotal(){
		BigDecimal b1 = new BigDecimal(quantity+"");
		BigDecimal b2 = new BigDecimal(book.getCurrPrice()+"");
		BigDecimal b3 =b1.multiply(b2);
		return b3.doubleValue();
	}
	
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
