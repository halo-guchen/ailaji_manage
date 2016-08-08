<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>文件管理器</title>
   <jsp:include page="/common/hplus-common-js.jsp"></jsp:include>
   <script type="text/javascript" src="/js/artTemplate.js"></script>
   <script type="text/javascript" src="/js/fileinput.min.js"></script>
   <script type="text/javascript" src="/js/fileinput_locale_zh.js"></script>
   <link href="${pageContext.request.contextPath}/css/fileinput.min.css" rel="stylesheet">
</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-sm-3">
                <div class="ibox float-e-margins">
                    <div class="ibox-content">
                        <div class="file-manager">
                            <h5>显示：</h5>
                            <a href="file_manager.html#" class="file-control active">所有</a>
                            <a href="file_manager.html#" class="file-control">文档</a>
                            <a href="file_manager.html#" class="file-control">视频</a>
                            <a href="file_manager.html#" class="file-control">图片</a>
                            <div class="hr-line-dashed"></div>
                            <button class="btn btn-primary btn-block" onclick="demo()">上传文件</button>
                            <div class="hr-line-dashed"></div>
                            <h5>文件夹</h5>
                            <ul class="folder-list" style="padding: 0">
                                <li><a href="file_manager.html"><i class="fa fa-folder"></i> 文件</a>
                                </li>
                                <li><a href="file_manager.html"><i class="fa fa-folder"></i> 图片</a>
                                </li>
                                <li><a href="file_manager.html"><i class="fa fa-folder"></i> Web页面</a>
                                </li>
                                <li><a href="file_manager.html"><i class="fa fa-folder"></i> 插图</a>
                                </li>
                                <li><a href="file_manager.html"><i class="fa fa-folder"></i> 电影</a>
                                </li>
                                <li><a href="file_manager.html"><i class="fa fa-folder"></i> 书籍</a>
                                </li>
                            </ul>
                            <h5 class="tag-title">标签</h5>
                            <ul class="tag-list" style="padding: 0">
                                <li><a href="file_manager.html">爱人</a>
                                </li>
                                <li><a href="file_manager.html">工作</a>
                                </li>
                                <li><a href="file_manager.html">家庭</a>
                                </li>
                                <li><a href="file_manager.html">孩子</a>
                                </li>
                                <li><a href="file_manager.html">假期</a>
                                </li>
                                <li><a href="file_manager.html">音乐</a>
                                </li>
                                <li><a href="file_manager.html">照片</a>
                                </li>
                                <li><a href="file_manager.html">电影</a>
                                </li>
                            </ul>
                            <div class="clearfix"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-9 animated fadeInRight">
                <div class="row">
                    <div class="col-sm-12" id="fileContent">
                        

                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="file_upload" style="display:none">
      <div style="margin:5px 5px;width:95%">
		    <form class="form-horizontal" id="resource_form">
				<div class="form-group">
					<label for="file_name" class="col-sm-2 control-label">文件名称</label>
					<div class="col-sm-10">
					   <input  class="form-control" id="file_name" placeholder="文件名称">
					</div>
				</div>
				<div class="form-group">
					<label for="input_file" class="col-sm-2 control-label">资源文件</label>
					<div class="col-sm-10">
						<input  type="file" name="uploadFile"  id="input_file"  class="file-loading" />
					</div>
				</div>
				<div class="form-group">
				    <label for="typeSelect" class="col-sm-2 control-label">文件类型</label>
					<div class="col-sm-10">
				            <select class="form-control" id="typeSelect" name="type" >
					            <option value="1" selected="selected">图片</option>
					            <option value="2">视频</option>
					            <option value="3">音频</option>
				            </select>
					</div>
				</div>
				<div class="form-group">
				   <label class="col-sm-2 control-label">标签属性</label>
				   <div class="col-sm-10">
				            <select class="form-control" id="tagSelect" name="tag" multiple="multiple">
				               <option value="1">工作</option>
				               <option value="1">工作</option>
				            
				            </select>
				   </div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary" id="resource_upload">提交</button>
					</div>
				</div>
			</form>
		</div>
    </div>
    <script id="resourceContent" type="text/html">
   {{each list as resource}}
        <div class="file-box">
            <a href="{{resource.url}}">
                <div class="file">
                   <span class="corner"></span>
                      <div class="image">
                          <img alt="image" class="img-responsive" src="{{resource.url}}">
                      </div>
                <div class="file-name">
                     {{resource.name}}
                       <br/>
                      <small>添加时间：{{resource.creatTime}}</small>
                </div>
               </div>
            </a>
        </div>
   {{/each}}
    </script>

    <script type="text/javascript">
        var data;
        $(document).ready(function(){
        	$(".file-box").each(function(){animationHover(this,"pulse")});
        	loadResource();
        	initFileInput("input_file","${pageContext.request.contextPath}/upload/form");
        	});
        
        function loadResource(){
        	$.ajax({
        		type:'GET',
        		data:{type:1},
        		url:'${pageContext.request.contextPath}/private/user/resource/query',
        		success:function(result){
        			data = result;
        			var html=template('resourceContent', {list:data});
                	document.getElementById('fileContent').innerHTML=html;
        		},
        		error:function(){
        			alert("请求超时!");
        		}
        	})
        }
        $("#input_file").on('fileuploaded', function(event, data, id, index) {
            console.log(data)
        });
        function demo(){
        	layer.open({
        		  type: 1,
        		  title:'资源信息',
        		  skin: 'layui-layer-rim', //加上边框
        		  area: ['720px', '520px'], //宽高
        		  content: $("#file_upload")
        		}); 
        }
        
    </script>
   
</body>
</html>


