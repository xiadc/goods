package cn.itcast.goods.admin.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.admin.admin.domain.Admin;

/**
 * Servlet Filter implementation class AdminFilter
 */
@WebFilter({ "/adminjsps/admin/*", "/admin/*" })
public class AdminFilter implements Filter {

    
    public AdminFilter() {
        // TODO Auto-generated constructor stub
    }

	
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		Object admin =  req.getSession().getAttribute("sessionAdmin");
		
		if(admin==null){
			req.setAttribute("msg", "请您先登录！");
			req.getRequestDispatcher("/adminjsps/login.jsp").forward(request, response);	
			return;
		}
		chain.doFilter(request, response); 
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
