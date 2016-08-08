<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>球体转动</title>
<link href="${pageContext.request.contextPath}/css/tagCloud.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/tagCloud.js"></script>
</head>
<body>

            <div class="tagcloud">
				<a href="javascript:" style="color:red;">豆瓣</a>
				<a href="javascript:" style="color:pink;">新浪</a>
				<a href="javascript:" style="color:yellow;">百度</a>
				<a href="javascript:" style="color:blue;">360</a>
				<a href="javascript:" style="color:orange;">腾讯</a>
				<a href="javascript:" style="color:black;">搜狐</a>
				<a href="javascript:" style="color:green;">拉钩</a>
				<a href="javascript:" style="color:grey;">微信</a>
				<a href="javascript:" style="color:red;">饿了吗</a>
			</div>
</body>
<script type="text/javascript">
$(function() {
	$('.tagcloud').tagCloud();
	});
</script>
</html>