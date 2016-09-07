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
}
