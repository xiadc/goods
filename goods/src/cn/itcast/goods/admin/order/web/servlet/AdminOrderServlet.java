package cn.itcast.goods.admin.order.web.servlet;




import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {

	private OrderService orderService = new OrderService();
	
	/**获取当前页码pagecode
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		int pc =1;
		String param = req.getParameter("pc");
		if(param!=null&&!param.trim().isEmpty()){
			try{
				pc = Integer.parseInt(param);
			}catch(RuntimeException e){}
		}
			return pc;
	}
	
	/**截取url，页面中的分页导航中需要使用它作为超链接目标
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		// http://localhost:8080/goods/BookServlet?method=findByCategory&cid=xxx
		// /goods/BookServlet + ? +method=findByCategory&cid=xxx
		String url = req.getRequestURI()+ "?" + req.getQueryString();
		/*
		 * 如果url中存在pc参数，截取掉，不存在就不用截取
		 */
		int index =url.lastIndexOf("&pc=");
		if(index!=-1)
			url=url.substring(0,index);
		return url;
	}
	
	/**查询所有订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			int pc =getPc(req);
			String url = getUrl(req);
			PageBean<Order> pb = orderService.findAll(pc);
			pb.setUrl(url);
			req.setAttribute("pb", pb);
			return "f:/adminjsps/admin/order/list.jsp";
			
	}
	
	/**按状态查询订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			int pc =getPc(req);
			String url = getUrl(req);
			int status = Integer.parseInt(req.getParameter("status"));
			PageBean<Order> pb = orderService.findByStatus(status,pc);
			pb.setUrl(url);
			req.setAttribute("pb",pb);
			return "f:/adminjsps/admin/order/list.jsp";
			
	}
	
	/**按订单id查询订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String queryOrderByOid(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		String btn = req.getParameter("btn");//分清来自哪个超链接
		PageBean<Order> pb = orderService.findByOid(oid);
		List<Order> list = pb.getBeanList();
		Order order = list.get(0);
		req.setAttribute("order", order);
		req.setAttribute("btn", btn);//反写这些状态，方便前台页面判断是该支付，取消或者确认收货
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	/**取消订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancelOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status !=1){
			req.setAttribute("msg", "订单当前状态不能取消！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);//5代表取消订单
		req.setAttribute("msg", "订单已取消！");
		return "f:/adminjsps/msg.jsp";
	}
	
	/**发货
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliverOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status !=2){
			req.setAttribute("msg", "订单当前状态不能发货！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);//3代表取消订单
		req.setAttribute("msg", "订单已发货！");
		return "f:/adminjsps/msg.jsp";
	}
	
}
