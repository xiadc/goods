package cn.itcast.goods.category.domain;

import java.util.List;

/**分类实体类
 * @author xiadc
 *
 */
public class Category {

	private String cid;
	private String cname;
	private Category parent;
	private String desc;
	private List<Category> childList;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<Category> getChildList() {
		return childList;
	}
	public void setChildList(List<Category> childList) {
		this.childList = childList;
	} 
	
	
}
