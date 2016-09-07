package cn.itcast.goods.category.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.goods.category.dao.CategoryDao;
import cn.itcast.goods.category.domain.Category;

public class CategoryService {

	private CategoryDao categoryDao = new CategoryDao();
	
	/**查询所有分类
	 * @return
	 */
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**添加分类
	 * @param category
	 */
	public void add(Category category){
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**查找所有一级分类但不包括二级分类
	 * @return
	 */
	public List<Category> findParents(){
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**修改一级分类
	 * @param category
	 */
	public void update(Category category){
		try {
			categoryDao.update(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**按cid查找分类
	 * @param cid
	 * @return
	 */
	public Category findByCid(String cid){
		try {
			return categoryDao.findByCid(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**按cid删除分类
	 * @param cid
	 */
	public void deleteByCid(String cid){
		try {
			categoryDao.deleteByCid(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**按pid查找分类
	 * @param pid
	 * @return
	 */
	public List<Category> findByParent(String pid){
		try {
		 return	categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
