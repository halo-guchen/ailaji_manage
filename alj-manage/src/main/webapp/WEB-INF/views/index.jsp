<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ailaji-ERP</title>
<jsp:include page="/common/common-js.jsp"></jsp:include>
<style type="text/css">
	.content {
		padding: 10px 10px 10px 10px;
	}
	.wu-header { height:75px; position:relative; z-index:0; overflow:hidden; border-bottom:1px #DDDDDD solid; background:url(../images/page-background.png) bottom repeat-x;}
.wu-header-left { position:absolute; z-index:1; left:18px; top:10px;}
.wu-header-left h1 { font:20px/20px Verdana; color:#3F6CAF;}
.wu-header-right { position:absolute; z-index:1; right:5px; top:22px; color:#3F6CAF; text-align:right;}
.wu-header-right p { line-height:0.7em;}
.wu-header-right a { color:#3F6CAF; margin:0 5px;}
</style>
</head>
<body class="easyui-layout">
    <div class="wu-header" data-options="region:'north',border:false,split:true">
    	<div class="wu-header-left">
        	<h1>爱垃圾  | ERP</h1>
        </div>
        <div class="wu-header-right">
        	<p><strong class="easyui-tooltip" title="2条未读消息">admin</strong>，欢迎您！</p>
            <p><a href="#">网站首页</a>|<a href="#">支持论坛</a>|<a href="#">帮助中心</a>|<a href="#">安全退出</a></p>
        </div>
    </div>
    <div data-options="region:'west',title:'菜单',split:true" style="width:200px;">
    	<ul id="menu" class="easyui-tree" style="margin-top: 10px;margin-left: 5px;">
         	<li>
         		<span>商品管理</span>
         		<ul>
	         		<li data-options="attributes:{'url':'/goods/info/detail/main'}">商品信息</li>
	         		<li data-options="attributes:{'url':'/goods/info/add/main'}">新建商品</li>
	         		<li data-options="attributes:{'url':'/goods/brand/main'}">品牌管理</li>
	         		<li data-options="attributes:{'url':'/goods/classify/main'}">分类列表</li>
	         		<li data-options="attributes:{'url':''}">商品属性</li>
	         		<li data-options="attributes:{'url':''}">标签专区</li>
	         		<li data-options="attributes:{'url':''}">限时特价及审核</li>
	         		<li data-options="attributes:{'url':''}">组合活动</li>
	         		<li data-options="attributes:{'url':''}">校区单价</li>
	         	</ul>
         	</li>
         	<li>
         		<span>采购管理</span>
         		<ul>
	         		<li data-options="attributes:{'url':''}">库存告警</li>
	         		<li data-options="attributes:{'url':''}">库存剩余</li>
	         		<li data-options="attributes:{'url':''}">PO单列表</li>
	         		<li data-options="attributes:{'url':''}">审核列表</li>
	         		<li data-options="attributes:{'url':''}">门店申请列表</li>
	         	</ul>
         	</li>
         	<li>
         		<span>仓库管理</span>
         		<ul>
	         		<li data-options="attributes:{'url':''}">库存查询</li>
	         		<li data-options="attributes:{'url':''}">收货处理</li>
	         		<li data-options="attributes:{'url':''}">调拨申请列表</li>
	         		<li data-options="attributes:{'url':''}">转仓详情</li>
	         		<li data-options="attributes:{'url':''}">月光宝盒商品池</li>
	         		<li data-options="attributes:{'url':''}">盘点单详情</li>
	         	</ul>
         	</li>
         	<li>
         		<span>门店</span>
         		<ul>
	         		<li data-options="attributes:{'url':''}">订单列表</li>
	         		<li data-options="attributes:{'url':''}">库存管理</li>
	         		<li data-options="attributes:{'url':''}">筋斗云资料</li>
	         		<li data-options="attributes:{'url':''}">申请单列表</li>
	         		<li data-options="attributes:{'url':''}">转铺详情</li>
	         		<li data-options="attributes:{'url':''}">收货处理</li>
	         	</ul>
         	</li>
         	<li>
         		<span>财务管理</span>
         		<ul>
	         		<li data-options="attributes:{'url':''}">打印</li>
	         	</ul>
         	</li>
         	<li>
         		<span>系统</span>
         		<ul>
	         		<li data-options="attributes:{'url':''}">订单查询</li>
	         	</ul>
         	</li>
         </ul>
    </div>
    <div data-options="region:'center',title:''">
    	<div id="tabs" class="easyui-tabs">
		    <div title="首页" style="padding:20px;">
		        	
		    </div>
		</div>
    </div>
    
<script type="text/javascript">
$(function(){
	$('#menu').tree({
		onClick: function(node){
			if($('#menu').tree("isLeaf",node.target)){
				var tabs = $("#tabs");
				var tab = tabs.tabs("getTab",node.text);
				if(tab){
					tabs.tabs("select",node.text);
				}else{
					tabs.tabs('add',{
					    title:node.text,
					    href: node.attributes.url,
					    closable:true,
					    bodyCls:"content"
					});
				}
			}
		}
	});
});
</script>
</body>
</html>