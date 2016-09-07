package cn.itcast.goods.cart.web.servlet;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.cart.service.CartItemService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class CartItemServlet extends BaseServlet {

	private CartItemService cartItemService = new CartItemService();
	
	public String loadCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemIds = req.getParameter("cartItemIds");
		List<CartItem> list = cartItemService.loadCartItems(cartItemIds);
		req.setAttribute("cartItemList", list);
		req.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
		
	}
	/**更新条目数量
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateQuantity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemId = req.getParameter("cartItemId");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		CartItem cartItem =  cartItemService.updateQuantity(cartItemId, quantity);
		//给客户端返回一个json对象
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"sumTotal\"").append(":").append(cartItem.getSumTotal());
		sb.append("}");
		System.out.println(sb);
		resp.getWriter().print(sb);
		return null;
	}
	
	/**批量删除功能
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemIds = req.getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		return myCart(req, resp);
	}
	
	/**显示我的购物车
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("sessionUser");
		List<CartItem> list =  cartItemService.findByUid(user.getUid());
		req.setAttribute("list", list);
		return "f:/jsps/cart/list.jsp";
	}
	
	/**添加购物车条目
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addCartItem(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("sessionUser");
		String bid = req.getParameter("bid");
		Book book = new Book();
		book.setBid(bid);
		int quantity =Integer.parseInt(req.getParameter("quantity")) ;
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(quantity);
		cartItem.setUser(user);
		cartItem.setBook(book);
		
		cartItemService.addCartItem(cartItem);
		
		//return "f:/CartItemServlet?method=myCart";
		return myCart(req, resp);
	}

}
