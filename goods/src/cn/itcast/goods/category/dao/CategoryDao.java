package cn.itcast.goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao {

	private QueryRunner qr = new TxQueryRunner();
	
	/**map转bean
	 * @param map
	 * @return
	 */
	private Category toCategory(Map<String,Object> map){
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String) map.get("pid");
		if(pid!=null){
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	
	/**
	 * MapList转化成CategoryList
	 * @param mapList
	 * @return
	 */
	private List<Category> toCategoryList(List<Map<String,Object>> mapList){
		List<Category> categoryList = new ArrayList<Category>();
		for(Map<String,Object> map : mapList){
			Category category = toCategory(map);
			categoryList.add(category);
		}
		return categoryList;
	}
	
	/**返回所有一级分类
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findAll() throws SQLException{
		String sql = "select * from t_category where pid is null order by orderBy";
		//得到所有的一级分类
		List<Map<String,Object>> categoryMapList = qr.query(sql, new MapListHandler());
		
		List<Category> parents = toCategoryList(categoryMapList);
		
		
		//遍历一级分类，为每个一级分类得到二级分类
		for(Category parent:parents){
			List<Category> childList = findByParent(parent.getCid());
			parent.setChildList(childList);
		}
		
		return parents;
		
	}
	
	/**按照父类id查找所有二级分类
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		String sql = "select * from t_category where pid =? order by orderBy";
		List<Map<String,Object>> categoryMaplist =qr.query(sql, new MapListHandler(), pid); 
		List<Category> childList = toCategoryList(categoryMaplist);
		return  childList;
	}
	
	/**添加一级分类或者二级分类
	 * @param category
	 * @throws SQLException 
	 */
	public void add(Category category) throws SQLException{
		String sql = "insert into t_category(cid,cname,pid,`desc`) values (?,?,?,?)";
		String pid = null;
		if(category.getParent()!=null){//二级分类
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCid(),category.getCname(),pid,category.getDesc()};
		qr.update(sql, params);
	}
	
	/**获取所有父分类不带子分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findParents() throws SQLException{
		String sql = "select * from t_category where pid is null order by orderBy";
		//得到所有的一级分类
		List<Map<String,Object>> categoryMapList = qr.query(sql, new MapListHandler());
		
		List<Category> parents = toCategoryList(categoryMapList);
		
		return parents;
		
	}
	
	/**修改一级分类
	 * @param category
	 * @throws SQLException
	 */
	public void update(Category category) throws SQLException{
		String sql = "update t_category set cname = ?,`desc` = ?,pid = ? where cid = ?";
		String pid = null;
		if(category.getParent()!=null){
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(),category.getDesc(),pid,category.getCid()};
		qr.update(sql, params);
	}
	
	/**按cid查找分类
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public Category findByCid(String cid) throws SQLException{
		String sql = "select * from t_category where cid = ?";
		 return toCategory(qr.query(sql, new MapHandler(), cid));
		
	}
	
	/**按cid删除分类
	 * @param cid
	 * @throws SQLException 
	 */
	public void deleteByCid(String cid) throws SQLException{
		String sql = "delete from t_category where cid = ?";
		qr.update(sql,cid);
	}
	
}
