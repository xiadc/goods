$(function() {
	/**
	 * 得到所有错误信息，循环遍历。
	 */
	$(".labelErrorClass").each(function() {
		showError($(this));
	});
	
	/**
	 * 鼠标进入立即注册时换图片
	 */
	$("#submitBtn").hover(function() {
		$(this).attr("src","/goods/images/regist2.jpg");
	},
	function() {
		$(this).attr("src","/goods/images/regist1.jpg");
	});
	
	/**
	 * 输入框得到焦点隐藏错误信息
	 */
	$(".inputClass").focus(function() {
		var labelId = $(this).attr("id")+"Error";
		//alert(labelId);
		$("#"+labelId).text("");
		showError($("#"+labelId));
	});
	
	/**
	 * 输入框失去焦点进行校验
	 */
	$(".inputClass").blur(function() {
		var id=$(this).attr("id");
		var funName = "validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";
		//eval函数将字符串当做js执行
		eval(funName);
		
	});
	
	/**
	 * 点击提交注册表单时校验
	 */
	$("#registform").submit(function() {
		var bool = true;//表示检验通过
		if(!validateLoginname()){
			bool = false;
		}
		if(!validateLoginpass()){
			bool = false;
		}
		if(!validateReloginpass()){
			bool = false;
		}
		if(!validateEmail()){
			bool = false;
		}
		if(!validateVerifycode()){
			bool = false;
		}
		return bool;
	});
	
	
	
});

/**
 * 登录名校验方法
 */

function validateLoginname(){
	var id="loginname";
	var value=$("#"+id).val();
	/**
	 * 1.非空检验
	 * 2.长度检验
	 * 3.是否注册校验
	 */
	if(!value){
		$("#"+id+"Error").text("用户名不能为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	if(value.length<3||value.length>20){
		$("#"+id+"Error").text("用户名长度应介于3~20之间！");
		showError($("#"+id+"Error"));
		return false;
	}
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateLoginname",loginname:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("用户名已被注册！");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	
	return true;
}

/**
 * 登录密码校验方法
 */
function validateLoginpass () {
	var id="loginpass";
	var value=$("#"+id).val();
	/**
	 * 1.非空检验
	 * 2.长度检验
	 * 3.是否注册校验
	 */
	if(!value){
		$("#"+id+"Error").text("密码不能为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	if(value.length<3||value.length>20){
		$("#"+id+"Error").text("密码长度应介于3~20之间！");
		showError($("#"+id+"Error"));
		return false;
	}
	return true;
};

/**
 * 确认密码校验方法
 */
function validateReloginpass () {
	var id="reloginpass";
	var value=$("#"+id).val();
	/**
	 * 1.非空检验
	 * 2.长度检验
	 * 3.是否注册校验
	 */
	if(!value){
		$("#"+id+"Error").text("确认密码不能为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	if(value!= $("#loginpass").val()){
		$("#"+id+"Error").text("两次输入的密码不一致！");
		showError($("#"+id+"Error"));
		return false;
	}
	return true;
};

/**
 * 邮箱校验方法
 */
function validateEmail () {
	var id="email";
	var value=$("#"+id).val();
	/**
	 * 1.非空检验
	 * 2.长度检验
	 * 3.是否注册校验
	 */
	if(!value){
		$("#"+id+"Error").text("Email不能为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)){
		$("#"+id+"Error").text("邮箱格式不正确！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateEmail",email:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("该邮箱已被使用！");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	
	return true;
};

/**
 * 验证码校验方法
 */
function validateVerifycode () {
	var id="verifycode";
	var value=$("#"+id).val();
	/**
	 * 1.非空检验
	 * 2.长度检验
	 * 3.是否注册校验
	 */
	if(!value){
		$("#"+id+"Error").text("验证码不能为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	if(value.length!=4){
		$("#"+id+"Error").text("验证码长度必须为4！");
		showError($("#"+id+"Error"));
		return false;
	}
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateVerifyCode",verifycode:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("验证码错误！");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	
	return true;
};


/**
 * 判断当前内容是否存在内容，如果存在显示，不存在不显示
 */
function showError(ele){
    var text = ele.text();
    if(!text)
    	ele.css("display","none");
    else
    	ele.css("display","");
}
/**
 * 验证码换一张的实现
 */
function _hyz() {
	/**
	 * 1.获取<img>属性
	 * 2.重新设置它的src
	 * 3.使用毫秒来添加参数
	 * 
	 */
	$("#imgVerifyCode").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime());
}
