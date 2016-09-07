package cn.itcast.goods.book.web.servlet;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {

	private BookService bookService = new BookService();
	
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
	
	
	/**按分类查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.得到pc
		int pc =getPc(req);
		//2.得到url
		String url = getUrl(req);
		//3.获取查询条件，这里是cid
		String cid = req.getParameter("cid");
		//4.调用service
		PageBean<Book> pb = bookService.findByCategory(cid,pc);
		//5.给pageBean设置url,保存pageBean并转发
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**按作者查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.得到pc
		int pc =getPc(req);
		//2.得到url
		String url = getUrl(req);
		//3.获取查询条件，这里是cid
		String author = req.getParameter("author");
		//4.调用service
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		//5.给pageBean设置url,保存pageBean并转发
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**按出版社查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.得到pc
		int pc =getPc(req);
		//2.得到url
		String url = getUrl(req);
		//3.获取查询条件，这里是cid
		String press = req.getParameter("press");
		//4.调用service
		PageBean<Book> pb = bookService.findByPress(press, pc);
		//5.给pageBean设置url,保存pageBean并转发
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**按书名查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.得到pc
		int pc =getPc(req);
		//2.得到url
		String url = getUrl(req);
		//3.获取查询条件，这里是book对象
		String bname = req.getParameter("bname");
		//4.调用service
		PageBean<Book> pb = bookService.findByBname(bname, pc);
		//5.给pageBean设置url,保存pageBean并转发
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	
	/**组合查询，按照书名、作者和出版社高级查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCombination(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//1.得到pc
		int pc =getPc(req);
		//2.得到url
		String url = getUrl(req);
		//3.获取查询条件，这里是book对象
		Book criteria = CommonUtils.toBean(req.getParameterMap(), Book.class);
		//4.调用service
		PageBean<Book> pb = bookService.findByCombination(criteria, pc);
		//5.给pageBean设置url,保存pageBean并转发
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	
	/**加载图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	

}
