package cn.itcast.goods.admin.category;



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

public class AdminCategoryServlet extends BaseServlet {

	private CategoryService categoryService = new CategoryService();
	private BookService bookService = new BookService();
	
	/**查询所有分类gitTest111
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		 req.setAttribute("parents",categoryService.findAll());
		 return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**添加一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		return findAll(req,resp);
	}
	
	/**添加二级分类第一步：checkbox中显示正确的一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid = req.getParameter("pid");
		List<Category> parents = categoryService.findParents();
		req.setAttribute("pid", pid);
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	
	/**添加二级分类第二步，向数据库中插入二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category category =  CommonUtils.toBean(req.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		Category parent = new Category();
		parent.setCid(req.getParameter("pid"));
		category.setParent(parent);
		categoryService.add(category);
		return findAll(req,resp);
	}
	
	/**修改一级分类准备工作，获取需要修改的一级分类对象
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateParentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		Category category = categoryService.findByCid(cid);
		req.setAttribute("parent",category );
		return "f:/adminjsps/admin/category/edit.jsp";
	} 
	/**修改一级分类第二步：修改一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category category =  CommonUtils.toBean(req.getParameterMap(), Category.class);
		categoryService.update(category);
		return findAll(req,resp);
	} 
	
	/**修改二级分类第一步：获取当前二级分类的对象，传递到edit2.jsp页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		Category category = categoryService.findByCid(cid);
		List<Category> parents =categoryService.findParents(); 
		req.setAttribute("child", category);
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/edit2.jsp";
	} 
	
	/**修改二级分类第二步：修改二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		Category parent = new Category();
		parent.setCid(req.getParameter("pid"));
		category.setParent(parent);
		categoryService.update(category);
		return findAll(req,resp);
	}
	
	/**删除父分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		List<Category> list = categoryService.findByParent(cid);
		if(list.size()!=0){//子分类不为空，则不能删除
			req.setAttribute("msg", "子分类存在，不能删除该父分类");
			req.setAttribute("code", "error");
			return "f:/adminjsps/msg.jsp";
		}
		//子分类为空，可以删除
		categoryService.deleteByCid(cid);
		req.setAttribute("msg", "分类删除成功！");
		req.setAttribute("code", "success");
		return findAll(req,resp);
		
	}
	
	public String deleteChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		PageBean<Book> list =bookService.findByCategory(cid, 1);
		List<Book> bookList =  list.getBeanList();
		
		if(bookList.size()!=0){//子分类不为空，则不能删除
			req.setAttribute("msg", "子分类中存在图书，不能删除该子分类！");
			req.setAttribute("code", "error");
			return "f:/adminjsps/msg.jsp";
		}
		//子分类为空，可以删除
		categoryService.deleteByCid(cid);
		req.setAttribute("msg", "子分类删除成功！");
		req.setAttribute("code", "success");
		return findAll(req,resp);
		
	}
}
