<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>Bootstrap Table</title>
<jsp:include page="/common/hplus-common-js.jsp"></jsp:include>    
    

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
    
            <div class="ibox-content">
                <div class="row row-lg">
                <form method="get" class="form-horizontal">
                    <div class="col-md-6">
					    <div class="form-group">
                                <label class="col-sm-2 control-label">用户名:</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control">
                                </div>
                        </div>
					    <div class="form-group">
					    <label class="col-sm-2 control-label">性别:</label>
						    <div class="col-sm-10 ">
							   <label class="radio-inline"><input type="radio" name="sex" value="1">男</label>
							   <label class="radio-inline"><input type="radio" name="sex" value="0">女</label>
	                        </div>
                        </div>
					</div>
					<div class="col-md-6">
					    <div class="form-group">
					        <label class="col-sm-2 control-label">账户：</label>
					        <div class="col-sm-10">
					            <input type="text" name="" class="form-control" placeholder="请输入账户">
					        </div>
					    </div>
					</div>
					<div class="col-md-6">
					    <div class="form-group">
					       <label class="col-sm-2 control-label">Email:</label>
					       <div class="col-md-6 ">
					          <p class="form-control-static ">这是一段静态的文字</p>
					       </div>
					    </div>
					</div>
					
				</form>
				</div>
			</div>

            <div class="ibox-content">
                <div class="row row-lg">
                    <div class="col-sm-12">
                        <h4 class="example-title">用户信息</h4>
                        <div class="example">
                            <div id="userTable"></div>
                        </div>
                    </div>
                </div>
            </div>
        <!-- End Panel Basic -->
    </div>

</body>
<script type="text/javascript">
function getTab(){
	var url = '${pageContext.request.contextPath}/private/user/query';
	$('#userTable').bootstrapTable({
	method: 'get', //这里要设置为get，不知道为什么 设置post获取不了
	url: url,
	cache: false,
	width:'100%',
	striped: true,
	pagination: true,
	pageList: [10,20],
	pageSize:20,
	pageNumber:1,
	search: true,
	sidePagination:'server',//设置为服务器端分页
	showColumns: true,
	columns: [
	{
		field: 'id',
		title: 'ID',
		width: '16%',
		align: 'center',
	}, {
		field: 'account',
		title: '账户',
		width: '16%',
		align: 'center',
	}, {
		field: 'password',
		title: '密码',
		width: '16%',
		align: 'center',
	}, {
		field: 'username',
		title: '用户名',
		width: '16%',
		align: 'center',
	}, {
		title: '年龄',
		field: 'age',
		width: '16%',
		align: 'center',
	}, {
		title: '性别',
		field: 'sex',
		width: '16%',
		align: 'center',
	}]
	});
}
	
	$(function(){
		getTab();
		});
	

</script>

</html>
