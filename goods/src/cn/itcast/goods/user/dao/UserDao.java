package cn.itcast.goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * @author xiadc
 * @date:2016-7-20 上午9:55:42
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
	
	/**
	 * 校验用户名是否注册
	 * @param loginname
	 * @return
	 * @throws SQLException 
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql ="select count(*) from t_user where loginname=?";
		Number number =(Number)qr.query(sql, new ScalarHandler(),loginname);
		return number.intValue() ==0;
	}
	
	/**校验email是否注册
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql ="select count(*) from t_user where email=?";
		Number number =(Number)qr.query(sql, new ScalarHandler(),email);
		return number.intValue() ==0;
	}
	
	/**添加用户
	 * @param user
	 * @throws SQLException 
	 */
	public void add(User user) throws SQLException{
		String sql = "insert into t_user values(?,?,?,?,?,?)";
		Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),
				user.isStatus(),user.getActivationcode()};
		qr.update(sql,params);
	}
	
	/**根据激活码查询用户信息
	 * @param code
	 * @throws SQLException 
	 */
	public User findByCode(String code) throws SQLException{
		String sql = "select * from t_user where activationcode = ?";
		return qr.query(sql, new BeanHandler<User>(User.class),code);
	}
	
	/**更新激活状态
	 * @param uid
	 * @param state
	 * @throws SQLException
	 */
	public void updateState(String uid,boolean state) throws SQLException{
		String sql = "update t_user set status = ? where uid =?";
		qr.update(sql,state,uid);
	}
	
	/**按用户名与密码查询用户
	 * @param loginname
	 * @param loginpass
	 * @return
	 * @throws SQLException
	 */
	public User findByLoginnameAndLoginpass(String loginname,String loginpass) throws SQLException{
		String sql = "select * from t_user where loginname = ? and loginpass = ?";
		return qr.query(sql, new BeanHandler<User>(User.class),loginname,loginpass);
	}
	
	/**按uid和password查询
	 * @param uid
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public boolean findByUidAndPassword(String uid,String password) throws SQLException{
		String sql = "select count(*) from t_user where uid = ? and loginpass = ?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),uid,password);
		return number.intValue() > 0;
	}
	
	/**修改密码
	 * @param loginpass
	 * @param uid
	 * @throws SQLException
	 */
	public void updatePassword(String uid,String password) throws SQLException{
		String sql = "update t_user set loginpass = ? where uid =? ";
		qr.update(sql, password,uid);
	}
	
}
