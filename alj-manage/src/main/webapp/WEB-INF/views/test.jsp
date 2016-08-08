<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>bootstrap练习</title>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">


<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container-fluid">
	<div class="h1 text-center"><abbr title="是个大标题" >标题h1</abbr> </div>
	<div>
	 <p>Nullam quis risus eget urna mollis ornare vel eu leo. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nullam id dolor id nibh ultricies vehicula.
Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. <span class="lead">Donec ullamcorper nulla non metus auctor fringilla.</span> Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Donec ullamcorper nulla non metus auctor fringilla.
Maecenas sed diamTo switch directories, type <kbd>cd</kbd> followed by the name of the directory.<br>
To edit settings, press <kbd><kbd>ctrl</kbd> + <kbd>,</kbd></kbd>sit amet non magna. Donec id elit non mi porta gravida at <mark>eget metus</mark>. <del>Duis mollis</del>, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit.</p>
	</div>
	
	<dl class="dl-horizontal">
	  <dt >Nullam quis risus eget urna mollis ornare vel eu leo. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nullam id dolor id nibh ultricies vehicula.
Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. <span class="lead">Donec ullamcorper nulla non metus auctor fringilla.</span> Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Donec ullamcorper nulla non metus auctor fringilla.
Maecenas sed</dt>
	  <dd>...</dd>
	</dl>
	
	<div class="row">
		<table class="table table-striped">
		 <thead>表格</thead>
		 <tr>
		   <td>1</td>
		   <td>2</td>
		   <td>3</td>
		 </tr>
	    </table>
	
	</div>
	
	<form class="form-inline">
	  <div class="form-group">
	    <label for="exampleInputName2">Name</label>
	    <input type="text" class="form-control" id="exampleInputName2" placeholder="Jane Doe">
	  </div>
	  <div class="form-group">
	    <label for="exampleInputEmail2">Email</label>
	    <input type="email" class="form-control" id="exampleInputEmail2" placeholder="jane.doe@example.com">
	  </div>
	  <button type="submit" class="btn btn-default">Send invitation</button>
	</form>
	
	<form>
	  <div class="form-group">
	    <label for="exampleInputEmail1">Email address</label>
	    <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Email">
	  </div>
	  <div class="form-group">
	    <label for="exampleInputPassword1">Password</label>
	    <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
	  </div>
	  <div class="form-group">
	    <label for="exampleInputFile">File input</label>
	    <input type="file" id="exampleInputFile">
	    <p class="help-block">Example block-level help text here.</p>
	  </div>
	  <div class="checkbox">
	    <label>
	      <input type="checkbox"> Check me out
	    </label>
	  </div>
	  <button type="submit" class="btn btn-default">Submit</button>
	</form>
	
	
	<div class="checkbox">
	  <label>
	    <input type="checkbox" value="">
	    Option one is this and that&mdash;be sure to include why it's great
	  </label>
	</div>
	<div class="checkbox disabled">
	  <label>
	    <input type="checkbox" value="" disabled>
	    Option two is disabled
	  </label>
	</div>
	
	
	<select class= "form-control" multiple>
		    <option> 请选择</option >
		    <option> 游记</option >
		    <option> 景点</option >
			<option> 东京</option >
			<option> 日本</option >
			<option> 香港</option >
			<option> 加拿大</option >
	</select>                              
</div>
</body>
</html>