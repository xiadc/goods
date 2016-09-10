package cn.itcast.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {

	private QueryRunner qr = new TxQueryRunner();
	
	/**按id查找
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public Book findByBid(String bid) throws SQLException{
		String sql = "select * from t_book where bid = ?";
		Map<String,Object> map = qr.query(sql, new MapHandler(), bid);
		Book book =  CommonUtils.toBean(map, Book.class);
		//Category category = CommonUtils.toBean(map, Category.class);
		Category category = new Category();
		category.setCid((String)map.get("cid"));
		book.setCategory(category);
		return book;
		
	}
	
	
	/**按分类查询
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCategory(String cid,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid", "=", cid));
		return findByCriteria(exprList, pc);
	}
	
	/**按书名模糊查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByBname(String bname,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%"+bname+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**按作者查
	 * @param author
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author", "like", "%"+author+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**按出版社查
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press", "like", "%"+press+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**多条件复合查询
	 * @param criteria
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCombination(Book criteria,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%"+criteria.getBname()+"%"));
		exprList.add(new Expression("author", "like", "%"+criteria.getAuthor()+"%"));
		exprList.add(new Expression("press", "like", "%"+criteria.getPress()+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 通用查询方法
	 * @param exprList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList, int pc) throws SQLException{
		//1.得到ps
		int ps = PageConstants.BOOK_PAGE_SIZE;
		
		StringBuilder whereSql = new StringBuilder("where 1=1");
		List<Object> params = new ArrayList<Object>();//对应问号的值
		for(Expression expr : exprList){
			whereSql.append(" and ").append(expr.getName()).append(" ")
			.append(expr.getOperation()).append(" ");
			if(!expr.getOperation().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		
		//2.总记录数
		String sql = "select count(*) from t_book "+ whereSql;
		Number number =  (Number)qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		
		//3.得到beanList，即当前页记录数
		 sql = "select * from t_book " + whereSql +" order by orderBy limit ?,?";
		 params.add((pc-1)*ps);//当前页首行记录下标
		 params.add(ps);//每页记录数
		 List<Book> bookList =  qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());
		 
		 //4.创建PageBean，设置参数
		 PageBean<Book> pb = new PageBean<Book>();
		 pb.setBeanList(bookList);
		 pb.setPc(pc);
		 pb.setPs(ps);
		 pb.setTr(tr);
		 
		 return pb;
	}
	
	
	/**添加图书
	 * @param book
	 * @throws SQLException
	 */
	public void add(Book book) throws SQLException{
		String sql = "insert into t_book(bid,bname,author,price,currPrice," +
				"discount,press,publishtime,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Object[] params ={book.getBid(),book.getBname(),book.getAuthor(),book.getPrice(),book.getCurrPrice(),
					book.getDiscount(),book.getPress(),book.getPublishtime(),book.getEdition(),book.getPageNum(),book.getWordNum(),book.getPrinttime(),
					book.getBooksize(),book.getPaper(),book.getCategory().getCid(),book.getImage_w(),book.getImage_b()};
		qr.update(sql, params);
	}
	
	/**修改图书
	 * @param book
	 * @throws SQLException 
	 */
	public void updateBook(Book book) throws SQLException{
		String sql ="update t_book set bname=?,author=?,price=?,currPrice=?,discount=?,press=?,publishtime=?,edition=?,pageNum=?," +
				"wordNum=?,printtime=?,booksize=?,paper=?,cid=? where bid = ?";
		Object[] params = {book.getBname(),book.getAuthor(),book.getPrice(),book.getCurrPrice(),
				book.getDiscount(),book.getPress(),book.getPublishtime(),book.getEdition(),book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(),book.getCategory().getCid(),book.getBid()};
		qr.update(sql, params);
	}
	
	/**删除图书
	 * @param bid
	 * @throws SQLException 
	 */
	public void delete(String bid) throws SQLException{
		String sql = "delete from t_book where bid = ?";
		qr.update(sql, bid);
	}
	
}
