<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>cartlist.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script src="<c:url value='/js/round.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>">
<script type="text/javascript">
 


//我的代码

$(function(){
	showTotal();	
	//全选按钮的点击事件
	$("#selectAll").click(function(){
		//获取全选框的勾选状态
		var bool = $("#selectAll").attr("checked");
		//根据全选框状态改变其他框的勾选状态
		setCheckBoxItem(bool);
		//设置结算按钮样式
		setJieSuan(bool);
		//重新计算总计
		showTotal();
	});
	
	//给所有条目的复选框添加click事件
	$(":checkbox[name=checkboxBtn]").click(function(){
		var all = $(":checkbox[name=checkboxBtn]").length;
		var select = $(":checkbox[name=checkboxBtn][checked=true]").length;
		
		if(all == select){//全选中
			$("#selectAll").attr("checked",true);
			setJieSuan(true);
		}else if(select == 0){
			$("#selectAll").attr("checked",false);
			setJieSuan(false);
		}else{
			$("#selectAll").attr("checked",false);
			setJieSuan(true);
		}
		
		//重新计算总计
		showTotal();
		
		/* //设置全选框是否勾选
		var bool = true;
		$(":checkbox[name=checkboxBtn][checked=false]").each(function(){
			bool=false;
		});
		$("#selectAll").attr("checked",bool);
		
		//重新计算总计
		showTotal(); 
		
		//设置结算框的可用样式
		bool = false;
		$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
			bool=true;
		});
		setJieSuan(bool); */
	});
	
	//为加减添加点击事件
	$(".jian").click(function(){
		//得到减当前id
		var id = $(this).attr("id").substring(0,32);
		//得到当前条目数量
		var quantity = $("#"+id+"Quantity").val();
		if(quantity == 1){
			if(confirm("你确定要删除该条目吗？")){
				location = "/goods/CartItemServlet?method=batchDelete&cartItemIds="+id;
			}
		}else{
			sendAjaxQuantity(id,Number(quantity)-1);
		}
	});
	
		$(".jia").click(function(){
		//得到减当前id
		var id = $(this).attr("id").substring(0,32);
		//得到当前条目数量
		var quantity = $("#"+id+"Quantity").val();
		
		sendAjaxQuantity(id,Number(quantity)+1);
	
	});
	
	
	
});
//发送ajax请求，更改数据库中该条目的数量
function sendAjaxQuantity(cartItemId,quantity){
	$.ajax({
		url:"/goods/CartItemServlet",
		data:{method:"updateQuantity",cartItemId:cartItemId,quantity:quantity},
		dataType:"json",
		type:"POST",
		async:false,
		cache:false,
		success:function(result){
			//修改数量
			$("#"+cartItemId+"Quantity").val(result.quantity);
			//修改小计
			$("#"+cartItemId+"Subtotal").text(result.sumTotal);
			//重新计算总计
			showTotal();
		}
	
	});
}

//显示总计
function showTotal(){
	var total = 0;
	//获取所有选定的checkbox
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
		//对每一个选定的checkbox循环获取id
		var id = $(this).val();		
		//获取每个选中的商品小计
		var subTotal = $("#"+id+"Subtotal").text(); 			
		//累加小计
		total += Number(subTotal);	
	});
	//刷新显示总计
	$("#total").text(round(total,2));//把total保留2位小数
}
/*
	统一设置所有条目
*/
function setCheckBoxItem(bool){
	$(":checkbox[name=checkboxBtn]").attr("checked",bool);
}

//设置结算按钮是否可用
function setJieSuan(bool){
	if(bool){
		$("#jiesuan").removeClass("kill").addClass("jiesuan");
		$("#jiesuan").unbind("click");//解除所有click事件，使按钮恢复可用
	}else{
		$("#jiesuan").removeClass("jiesuan").addClass("kill");//移除与增加样式
		//使结算按钮不可用
		$("#jiesuan").click(function(){return false;});
	}
}
//批量删除
function batchDelete(){
	var cartItemIdArray = new Array();
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
		cartItemIdArray.push($(this).val());
	});	
	location = "/goods/CartItemServlet?method=batchDelete&cartItemIds="+cartItemIdArray;
}

//结算按钮点击事件
function jieSuan(){
	var cartItemIdArray = new Array();
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
		cartItemIdArray.push($(this).val());
	});
	$("#cartItemIds").val(cartItemIdArray.toString());
	$("#jieSuanForm").submit();
	
}

</script>
  </head>
  <body>

<c:choose>
	<c:when test="${empty list}">
		<table width="95%" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td align="right">
					<img align="top" src="<c:url value='/images/icon_empty.png'/>"/>
				</td>
				<td>
					<span class="spanEmpty">您的购物车中暂时没有商品</span>
				</td>
			</tr>
		</table>  
	
    <br/>
    <br/>
</c:when>
<c:otherwise>
<table width="95%" align="center" cellpadding="0" cellspacing="0">
	<tr align="center" bgcolor="#efeae5">
		<td align="left" width="50px">
			<input type="checkbox" id="selectAll" checked="checked"/><label for="selectAll">全选</label>
		</td>
		<td colspan="2">商品名称</td>
		<td>单价</td>
		<td>数量</td>
		<td>小计</td>
		<td>操作</td>
	</tr>




<c:forEach items="${list}" var="cartItem">
	<tr align="center">
		<td align="left">
			<input value="${cartItem.cartItemId}" type="checkbox" name="checkboxBtn" checked="checked"/>
		</td>
		<td align="left" width="70px">
			<a class="linkImage" href="<c:url value='/BookServlet?method=load&bid=${cartItem.book.bid }'/>"><img border="0" width="54" align="top" src="<c:url value='/${cartItem.book.image_b }'/>"/></a>
		</td>
		<td align="left" width="400px">
		    <a href="<c:url value='/BookServlet?method=load&bid=${cartItem.book.bid }'/>"><span>${cartItem.book.bname }</span></a>
		</td>
		<td><span>&yen;<span class="currPrice" >${cartItem.book.currPrice }</span></span></td>
		<td>
			<a class="jian" id="${cartItem.cartItemId}Jian"></a><input class="quantity" readonly="readonly" id="${cartItem.cartItemId}Quantity" type="text" value="${cartItem.quantity }"/><a class="jia" id="${cartItem.cartItemId}Jia"></a>
		</td>
		<td width="100px">
			<span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId}Subtotal">${cartItem.sumTotal}</span></span>
		</td>
		<td>
			<a href="<c:url value='/CartItemServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId}'/>">删除</a>
		</td>
	</tr>
</c:forEach>

	
	<tr>
		<td colspan="4" class="tdBatchDelete">
			<a href="javascript:batchDelete();">批量删除</a>
		</td>
		<td colspan="3" align="right" class="tdTotal">
			<span>总计：</span><span class="price_t">&yen;<span id="total"></span></span>
		</td>
	</tr>
	<tr>
		<td colspan="7" align="right">
			<a href="javascript:jieSuan();" id="jiesuan" class="jiesuan"></a>
		</td>
	</tr>
</table>
	<form id="jieSuanForm" action="<c:url value='/CartItemServlet'/>" method="post">
		<input type="hidden" name="cartItemIds" id="cartItemIds"/>
		<input type="hidden" name="method" value="loadCartItems"/>
	</form>
	</c:otherwise>
</c:choose>

  </body>
</html>
