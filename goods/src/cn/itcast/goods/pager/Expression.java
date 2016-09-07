package cn.itcast.goods.pager;

public class Expression {

	private String name;
	private String operation;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Expression [name=" + name + ", operation=" + operation
				+ ", value=" + value + "]";
	}
	public Expression(String name, String operation, String value) {
		super();
		this.name = name;
		this.operation = operation;
		this.value = value;
	}
	public Expression() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
