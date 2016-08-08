<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>注册— 爱垃圾</title>
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
			<form id="registForm">
				<div class="tc">
					<a href="http://www.ailaji.cn" class="logoA"></a>
				</div>
				<div class="loginBox rmtS">
					<div class="loginTitle">注册爱垃圾账户</div>
					<div class="loginTopLine">
						<span>手机快速注册</span>
					</div>
					<div class="loginList">
						<div class="loginListRow">
							<input placeholder="手机号码" name="account"
								class="inputText inputBlock" type="text">
							<div class="pt10">
								<b class="ct_5">*</b>填写您常用的手机号码，用于接收验证码
							</div>
						</div>
						<div class="loginListRow">
							<div class="loginFromBtn">
								<a href="javascript:void(0);" role="msgcode" id="reSendCode">获取验证码</a>
							</div>
							<div class="loginFromBox">
								<input placeholder="请输入短信验证码" name="code"
									class="inputText inputBlock" type="text">
							</div>
						</div>
						<div class="loginListRow">
							<input placeholder="密码" name="password"
								class="inputText inputBlock" type="password">
							<div class="pt10">
								<b class="ct_5">*</b> 密码应为6-21位字符
							</div>
						</div>
						<div class="pt20">
							<a href="javascript:void(0);" id="submitBtn"
								class="btn block blue">立即注册</a>
						</div>
					</div>
					<div class="rptS tc" style="padding-bottom: 20px;">
						<a href="${pageContext.request.contextPath}/page/login" class="ct_6 underline">已有爱垃圾账号?立即登陆</a>
					</div>
				</div>
			</form>
		</div>
		<div class="h6 ct_2 tc">© 2015 ailaji.cn 沪ICP备8888888号-8</div>
		<br>
	</div>
</body>

<script>

$(function () {
	$("#registForm").validate({
		rules : {
			account : {
				required : true,
				isMobile:true
			},
			password : {
				required : true,
				maxlength:21,
				minlength:6
			},
			code : {
				required : true,
				maxlength:6,
				minlength:6
			}
		},
		messages : {
			account : {
				required : "请输入用户名",
				isMobile: "账号应为正确的手机号码"
			},
			password : {
				required : "请输入密码",
				maxlength: "密码应为6-21位字符",
				minlength: "密码应为6-21位字符"
			},
			code : {
				required: "请输入验证码",
				maxlength: "验证码为6位",
				minlength: "验证码为6位"
			}
		}
	});
});

$("#reSendCode").bind("click",function(){
	var account = $('input[name="account"]').val();
	var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
	if (!reg.test(account)) {
	        alert('账号应为正确的手机号码');
	        $('input[name="account"]').errorInput();
	}else{
		getMessageCode(account);
	}
});

		 function getMessageCode(tel) {
			 $.ajax({
				   type: "POST",
				   url: "${pageContext.request.contextPath}/face/messageCode",
				   dataType:"json",
				   data: {account:tel},
				   success: function(msg){
					   setTimeout(reClassBtn, 1000);
					   $('#reSendCode').addClass('disabled').html('<span id="retime">60</span>秒后重新获取');
				   }
				});
		 };
		 function reClassBtn() {
             if ($('#reSendCode').hasClass('disabled')) {
                     var time = $('#retime').text();
                     time--;
                     $('#retime').text(time);
                     if (time == 0) {
                             $('#reSendCode').removeClass('disabled').html('获取验证码');
                     };
                     setTimeout(reClassBtn, 1000);
             };
     }
		 
		 $("#submitBtn").bind("click",function(){
			 var success=$("#registForm").valid();
			 var account=$("input[name='account']").val().trim();
		  	 var password=$("input[name='password']").val().trim();
		  	 var code=$("input[name='code']").val().trim();
			 if(success&&account!=""&&password!=""&&code!=""){
				 $.ajax({
					   type: "POST",
					   url: "${pageContext.request.contextPath}/face/regist",
					   dataType:"json",
					   data: {account:account,password:password,vcode:code},
					   success: function(data){
						   if(data.msg=="ok"){
						   window.location.href='http://www.ailaji.cn/page/jump';
						   }else{
							   $("#registForm")[0].reset();
							   $('#reSendCode').removeClass('disabled').html('获取验证码');
							   alert(data.msg);
						   }
					   }
					});
			 }else{
				 alert("请填写完整内容！");
			 }
		 })
		
	
</script>
</html>