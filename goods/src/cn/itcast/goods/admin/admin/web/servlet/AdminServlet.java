package cn.itcast.goods.admin.admin.web.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.admin.admin.domain.Admin;
import cn.itcast.goods.admin.admin.service.AdminService;
import cn.itcast.servlet.BaseServlet;

public class AdminServlet extends BaseServlet {

private AdminService adminService = new AdminService();
	

	/**登录模块
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Admin formAdmin = CommonUtils.toBean(req.getParameterMap(), Admin.class);
		Admin admin = adminService.login(formAdmin);
		if(admin == null){
			req.setAttribute("msg", "用户名或密码错误！");
			return "f:/adminjsps/login.jsp";
		}else{
			req.getSession().setAttribute("sessionAdmin", admin);
			return "r:/adminjsps/admin/index.jsp";
		}
	}
}
