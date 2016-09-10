package cn.itcast.goods.admin.book.web.servlet;



import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	private BookService bookService =new  BookService();
	
	
	/**得到所有分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> parents  = categoryService.findAll();
		req.setAttribute("parents",parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		
		String cid = book.getCategory().getCid();
		Category category =  categoryService.findByCid(cid);
		String pid = category.getParent().getCid();//获取当前书籍所属的一级分类id
		List<Category> childList = categoryService.findByParent(pid);//获取当前一级分类下的所有的二级分类
		List<Category> parentList = categoryService.findParents();//获取所有的一级分类
		
		req.setAttribute("book", book);
		req.setAttribute("childList", childList);
		req.setAttribute("parentCid", pid);
		req.setAttribute("parentList", parentList);
		
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	

	/**根据一级分类加载所有二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loadChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid =req.getParameter("pid");
		List<Category> childList = categoryService.findByParent(pid);
		
		//将list转换成json数组
		String json = toJson(childList);
		resp.getWriter().print(json);
		return null;
		
	}
	
	//{"cid":"cid","cname","cname"}
	private String toJson(Category category){
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
//[{"":"","":""},{}]
	private String toJson(List<Category> list){
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < list.size(); i++) {
			sb.append(toJson(list.get(i)));
			if(i<list.size()-1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	
	/**添加图书准备工作
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			req.setAttribute("parents",categoryService.findParents());
			return "f:/adminjsps/admin/book/add.jsp";
		
	}
	
	/**编辑图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
			Category category = new Category();
			category.setCid(req.getParameter("cid"));
			book.setCategory(category);
			
			bookService.edit(book);
			req.setAttribute("msg", "修改图书成功！");
			return "f:/adminjsps/msg.jsp";		
	}
	
	/**删除图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
			bookService.delete(book);
			
			//删除图片
			String smallPath = this.getServletContext().getRealPath("/")+"\\"+book.getImage_b();
			String bigPath = this.getServletContext().getRealPath("/")+"\\"+book.getImage_w();
			File distSmallFile = new File(smallPath);
			File distBigFile = new File(bigPath);
			
			distSmallFile.delete();
			distBigFile.delete();
			
			
			req.setAttribute("msg", "删除图书成功！");
			return "f:/adminjsps/msg.jsp";		
	}
	
}
