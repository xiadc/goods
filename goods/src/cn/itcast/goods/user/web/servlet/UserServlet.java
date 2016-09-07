package cn.itcast.goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {

	private UserService userService = new UserService();
	
	/**
	 * 注册功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateLoginname(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String loginname = request.getParameter("loginname");
		boolean b=  userService.ajaxValidateLoginname(loginname);
		response.getWriter().print(b);
		return null;
	}
	
	/**验证email
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateEmail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		boolean b=  userService.ajaxValidateLoginname(email);
		response.getWriter().print(b);
		return null;
	}
	
	/**验证码是否正确
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String verifycode = request.getParameter("verifycode");
		String vcode= (String) request.getSession().getAttribute("vCode");
		response.getWriter().print(vcode.equalsIgnoreCase(verifycode));
		return null;
	}
	
	/**提交注册表单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.表单信息转成javabean
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
		//2.验证表单信息,如果检验失败，
		Map<String,String> errorMap = this.validateRegist(formUser, request.getSession());
		if(!errorMap.isEmpty()){
			request.setAttribute("form", formUser);
			request.setAttribute("errors", errorMap);
			return "f:/jsps/user/regist1.jsp";
		}
		//3.存储user信息，转给service
		userService.regist(formUser);
		//4.获取注册结果，转跳成功或者失败页面
		request.setAttribute("code", "success");
		request.setAttribute("msg", "注册功能，请到邮箱激活");
		return "f:/jsps/msg.jsp";
	}
	
	/**激活账号
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String activation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String activationCode = request.getParameter("activationCode");
		try {
			userService.activation(activationCode);
			request.setAttribute("msg", "恭喜您激活成功！");
			request.setAttribute("code", "success");
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("code", "error");
		}
		return "f:/jsps/msg.jsp";
	}
	
	
	/**注册校验
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String,String> validateRegist(User formUser,HttpSession session){
		Map<String,String> errorMap = new  HashMap<String, String>();
		//1.校验登录名
		String loginname = formUser.getLoginname();
		if(loginname==null||loginname.trim().isEmpty()){
			errorMap.put("loginname","用户名不能为空！");
		}else if(loginname.length()<3||loginname.length()>20){
			errorMap.put("loginname", "用户名长度必须介于3到20之间");
		}else if(!userService.ajaxValidateLoginname(loginname)){
			errorMap.put("loginname", "用户名已被注册！");
		}
		
		//2.校验登陆密码
		String loginpass = formUser.getLoginpass();
		if(loginpass==null||loginpass.trim().isEmpty()){
			errorMap.put("loginpass","密码不能为空！");
		}else if(loginpass.length()<3||loginpass.length()>20){
			errorMap.put("loginpass", "密码长度必须介于3到20之间");
		}
		
		//3.确认密码校验
		String reloginpass = formUser.getReloginpass();
		if(reloginpass==null||reloginpass.trim().isEmpty()){
			errorMap.put("reloginpass","密码不能为空！");
		}else if(!reloginpass.equals(loginpass)){
			errorMap.put("reloginpass", "两次输入密码不一致！");
		}
		
		//4.校验email
		String email = formUser.getEmail();
		if(email==null||email.trim().isEmpty()){
			errorMap.put("email","email不能为空！");
		}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
			errorMap.put("email", "用户名长度必须介于3到20之间");
		}else if(!userService.ajaxValidateEmail(email)){
			errorMap.put("email", "email已被注册！");
		}
		
		//5.校验验证码
		String verifyCode = formUser.getVerifycode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode ==null||verifyCode.trim().isEmpty()){
			errorMap.put("verifycode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errorMap.put("verifycode", "验证码错误！");
		}
		
		return errorMap;	
	}
	
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		User formUser =CommonUtils.toBean(request.getParameterMap(), User.class);
		
		Map<String,String> errorMap = validatelogin(formUser, request.getSession());
		if(!errorMap.isEmpty()){
			request.setAttribute("form", formUser);
			request.setAttribute("errors", errorMap);
			
			return "f:/jsps/user/login.jsp";
		}
		
		User user =  userService.login(formUser);
		
		if(user ==null){
			request.setAttribute("user", formUser);
			request.setAttribute("msg", "用户名或密码错误！");
			return "f:/jsps/user/login.jsp";
		}else if(!user.isStatus()){//激活码未激活
			request.setAttribute("user", formUser);
			request.setAttribute("msg", "账户未激活！");			
			return "f:/jsps/user/login.jsp";
		}else{
			request.getSession().setAttribute("sessionUser", user);
			String loginname = user.getLoginname();
			loginname = URLEncoder.encode(loginname,"utf-8");
			Cookie cookie = new Cookie("loginname", loginname);
			cookie.setMaxAge(3600*30);
			response.addCookie(cookie);
			return "r:/index.jsp";
		}
	}
	
	/**登陆校验
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String,String> validatelogin(User formUser,HttpSession session){
		Map<String,String> errorMap = new  HashMap<String, String>();
		//1.校验登录名
		String loginname = formUser.getLoginname();
		if(loginname==null||loginname.trim().isEmpty()){
			errorMap.put("loginname","用户名不能为空！");
		}else if(loginname.length()<3||loginname.length()>20){
			errorMap.put("loginname", "用户名长度必须介于3到20之间");
		}
		//2.校验登陆密码
		String loginpass = formUser.getLoginpass();
		if(loginpass==null||loginpass.trim().isEmpty()){
			errorMap.put("loginpass","密码不能为空！");
		}else if(loginpass.length()<3||loginpass.length()>20){
			errorMap.put("loginpass", "密码长度必须介于3到20之间");
		}
		//5.校验验证码
		String verifyCode = formUser.getVerifycode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode ==null||verifyCode.trim().isEmpty()){
			errorMap.put("verifycode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vcode)){
			errorMap.put("verifycode", "验证码错误！");
		}
		
		return errorMap;	
	}
	
	public String updatePassword(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
		/*
		 * 1.获取表单数据
		 */
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
		//获取session中的user
		User sessionUser = (User) request.getSession().getAttribute("sessionUser");
		if(sessionUser==null){
			request.setAttribute("msg", "您还没有登录");
			return "f:/jsps/user/login.jsp";
		}
		
		try {
			userService.updatePassword(sessionUser.getUid(), formUser.getLoginpass(), formUser.getNewloginpass());
			request.setAttribute("msg", "修改密码成功！");
			request.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", formUser);//为了回显
			return "f:/jsps/user/pwd.jsp";
		}
		
	}
	
	/**退出登录
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest request,HttpServletResponse response)
			throws ServletException, IOException{
			request.getSession().invalidate();
			return "r:/jsps/user/login.jsp";
	}
	
}
