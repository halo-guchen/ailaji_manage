<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8"> 
<title>登录 — 爱垃圾</title>
<meta content="ailaji.cn" name="author">
<meta content="ailaji.cn" name="Copyright">
<meta name="viewport" id="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link href="../css/web.css" rel="stylesheet" type="text/css">
<link href="../css/ailaji.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/jquery-validate-method.js"></script>
</head>
<body>
	<div class="kPad">
		<div class="loginPage" id="loginPage">
			<div class="tc">
				<a href="http://tuweia.cn/c/home/" class="logoA"></a>
			</div>
			<div class="loginBox rmtS">
				<form name="myform" id="myform" method="post" action="${pageContext.request.contextPath}/face/logon">
					<div class="loginTitle">登录爱垃圾</div>
					<div class="loginList">
						<div class="loginListRow">
							<input placeholder="手机/邮箱号码" name="account" 
								class="inputText inputBlock" type="text">
						</div>
						<div class="loginListRow">
							<input placeholder="密码" name="password"
								class="inputText inputBlock" type="password">
						</div>
						<div class="loginListRow" role="vcodeBox">
							<input placeholder="验证码" name="vcode" 
								class="inputText inputBlock" type="text">
							<p class="loginFromNotes pt5">
								<img id="vcodeImage" src="/code">&nbsp;&nbsp; <a
									href="javascript:void(0);" onclick="changeVcode()">看不清，换一张</a>
							</p>
							<span id="errMsg" style="color:red;"></span>
						</div>
						<div class="clearfix pt10">
							<a href="http://www.ailaji.cn/forget" class="fr ct_3">忘记密码？</a> <label
								class="checkbox"><input name="autoSignin" value="1"
								type="checkbox">下次自动登录</label>
						</div>
						<div class="pt20">
							<a href="javascript:void(0);" type="submit" onclick="ajaxSubmitForm()"
								class="btn block blue">登 录</a>
						</div>
					</div>
					<div class="pt20 tc">
						<a href="${pageContext.request.contextPath}/page/regist" class="ct_6 underline">没有爱垃圾账号?立即注册</a>
					</div>

					<div class="pt20 tc"></div>
				</form>
			</div>
		</div>
		<div class="h6 ct_2 tc">© 2015 ailaji.cn 沪ICP备8888888号-8</div>
		<br>
	</div>
</body>
<script type="text/javascript">
	
	
	
	
	/* function ok(){
		
		$.post("${pageContext.request.contextPath}/face/logon",
				{ account: account, password: password,vcode:vcode },
				function(data){
					alert(data.);
		})
	} */
	
	$(function () {
		$("#myform").validate({
			rules : {
				account : {
					required : true,
					isMobileOrPhone:true
				},
				password : {
					required : true,
					maxlength:21,
					minlength:6
				},
				vcode : {
					required : true,
					maxlength:4,
					minlength:4
				}
			},
			messages : {
				account : {
					required : "请输入用户名",
					isMobileOrPhone: "账号应为正确的email地址或手机号码"
				},
				password : {
					required : "请输入密码",
					maxlength: "密码应为6-21位字符",
					minlength: "密码应为6-21位字符"
				},
				vcode : {
					required: "请输入验证码",
					maxlength: "验证码为4位",
					minlength: "验证码为4位"
				}
			}
		});
	});
	
	
	function ajaxSubmitForm(){
		var success=$("#myform").valid();
    	var account=$("input[name='account']").val().trim();
  		var password=$("input[name='password']").val().trim();
  		var vcode=$("input[name='vcode']").val().trim();
  		if(success&&account!=""&&password!=""&&vcode!=""){
  			$.post("${pageContext.request.contextPath}/face/logon",
  	 				{ account: account, password: password,vcode:vcode },
  	 				function(data){
  	 					changeVcode();
  	 					if(data.msg=="ok"){
  	 						window.location.href="${pageContext.request.contextPath}/page/main";
  	 					}else{
  	 						$("#errMsg").html(data.msg);
  	 						 $("#myform")[0].reset();
  	 					}
  	 					}) 
  		}else{
  			alert("请填写完整内容！");
  		} 
  		
     };
	
	function changeVcode() {
		 var timestamp = (new Date()).valueOf();
		var img=$("#vcodeImage");
		var src = img.attr("src");
		src=src+"?timestamp="+timestamp;
		img.attr("src", src);
	}; 
	
	$("input").on("focus",function(){
		$("#errMsg").html("");
	})
     
	
	
	
</script>
</html>