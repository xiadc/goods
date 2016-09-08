<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>图书分类</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

<link rel="stylesheet" type="text/css" href="<c:url value='/adminjsps/admin/css/book/list.css'/>">
<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/pager/pager.css'/>" />

<script type="text/javascript" src="<c:url value='/adminjsps/admin/js/book/list.js'/>"></script>
  </head>
  
  <body>
<div class="divBook">
<ul>



 <c:forEach items="${ pb.beanList}" var="book">
  <li>
  <div class="inner">
    <a class="pic" href="<c:url value='/admin/AdminBookServlet?method=load&bid=${book.bid }'/>"><img src="<c:url value='/${book.image_b }'/>" border="0"/></a>
    <p class="price">
		<span class="price_n">&yen;${book.currPrice}</span>
		<span class="price_r">&yen;${book.price}</span>
		(<span class="price_s">${book.discount}折</span>)
	</p>
	<p><a id="bookname" title="${book.bname}" href="<c:url value='/admin/AdminBookServlet?method=load&bid=${book.bid }'/>">${book.bname}</a></p>
	<!-- author包含中文，可能编码出现问题，但是实际没有出现乱码，以下方法能自动进行url编码，保证不出乱码 -->
		<!-- url标签能自动对参数进行url编码 -->
<!-- 	<c:url value="/BookServlet" var="authorUrl">
			<c:param name="method" value="findByAuthor"/>
			<c:param name="author" value="${book.author}" />
		</c:url>
	<p><a href="${authorUrl}" name='P_zz' title='${book.author }'>${book.author }</a></p>
	 -->
	<p><a href="<c:url value='/admin/AdminBookServlet?method=findByAuthor&author=${book.author }'/>" name='P_zz' title='${book.author }'>${book.author }</a></p>
	<p class="publishing">
		<span>出 版 社：</span><a href="<c:url value='/admin/AdminBookServlet?method=findByPress&press=${book.press }'/>">${book.press }</a>
	</p>
	<p class="publishing_time"><span>出版时间：</span>${book.publishtime }</p>
  </div>
  </li>
</c:forEach>


</ul>
</div>
<div style="float:left; width: 100%; text-align: center;">
	<hr/>
	<br/>
	<%@include file="/jsps/pager/pager.jsp" %>
</div>
  </body>
 
</html>

