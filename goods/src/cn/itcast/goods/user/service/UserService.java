package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.dao.UserDao;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

/**
 * @author xiadc
 * @date:2016-7-20 上午9:56:02
 */
public class UserService {
	
	private UserDao userDao = new UserDao();

	/**注册校验
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**email 校验
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void regist(User user){
		
		//1.补全user
		user.setStatus(false);
		user.setActivationcode(CommonUtils.uuid()+CommonUtils.uuid());
		user.setUid(CommonUtils.uuid());
		
		//2.user加入数据库
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		//3.发邮件
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String host = prop.getProperty("host");
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
		
		//创建email session
		Session session  = MailUtils.createSession(host, username, password);
		//创建mail对象
		String from = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		String content = MessageFormat.format(prop.getProperty("content"), user.getActivationcode());
		
		Mail mail = new Mail(from,to,subject,content);
		//发邮件
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**激活用户
	 * @param code
	 * @throws UserException
	 */
	public void activation(String code) throws UserException{
		try {
			User user = userDao.findByCode(code);
			if(user ==null) throw new UserException("激活码不存在！");
			if(user.isStatus()) throw new UserException("您已经激活过了，请勿重复激活！");
			userDao.updateState(user.getUid(), true);//激活
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**登陆功能
	 * @param user
	 * @return
	 */
	public User login(User user){
		try {
			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**修改密码
	 * @param user
	 * @throws UserException 
	 */
	public void updatePassword(String uid,String oldPass,String newPass) throws UserException{
		
		try {
			//校验老密码
			boolean bool = userDao.findByUidAndPassword(uid, oldPass);
			if(!bool) throw new UserException("老密码错误！");
			
			userDao.updatePassword(uid, newPass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
