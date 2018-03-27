/******************** 
	作用:登录页面
	版本:V1.0
	时间:2017-03-10
********************/

$(function() {
	initDropDownMenu(); //初始化下拉列表
	initData(); //初始化数据
	$("#sure").on("click", function(e) {
		var user=$("#userName").val();
		var pass=$("#password").val();
		var language = $("#currentLanguage").html();
		if(user==""){
			alert("请输入用户名");
			$("#userName").focus();
			return false;
		}else if(pass==""){
			alert("请输入密码");
			$("#password").focus();
			return false;
		}
		$("#language").val(language);
		//判断是否需要记住密码
		var duration = 30; //有效期限(天)
		var options = {
			expires: duration
		};
		var isRemember = $("#remember").prop("checked");
		if (isRemember) {
			//记住密码
			$.cookie("userName", user, options);
			$.cookie("password", pass, options);
			$.cookie("language", language, options);
		} else {
			//清除之前记住的信息
			$.removeCookie('userName');
			$.removeCookie('password');
			$.removeCookie('language');
		}
		return true;
	});
	
	/**
	 * 初始化数据
	 */
	function initData() {
		var user = $.cookie("userName");
		var pass= $.cookie("password");
		var language = $.cookie("language");
		if(user!=undefined&&user!=null){
			$("#userName").val(user);
			$("#password").val(pass);
			$("#currentLanguage").html(language);
		}
	}
});