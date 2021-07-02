<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
 <base href="<%=basePath%>" />
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function () {

			if(window.top!=window){//如果当前窗口不是顶级窗口，把当前窗口设置为顶级窗口
				window.top.location=window.location
			}
			$("#loginAct").val("");//页面加载完毕将用户名框清空

			$("#loginAct").focus();//页面加载完毕用户名框获得焦点

			$("#submitBtn").click(function(){

				login();
			})

			$(window).keydown(function (event){//event可以取得敲的是哪个键
				if(event.keyCode==13){//敲的是回车键执行登录操作

					login();
				}
			})

		})

		function login(){//自定义方法，建议写在外面
			var loginAct = $.trim($("#loginAct").val());//拿到账号密码使用$.trim()去掉前后空格
			var loginPwd =  $.trim($("#loginPwd").val());

			if(loginPwd==""||loginAct==""){
				$("#msg").html("账号密码不能为空")
				return false;//如果账号密码为空，方法终止
			}

			//将请求发送到后台
			$.ajax({
				url:"user/login.do",
				data:{
					loginAct:loginAct,
					loginPwd:loginPwd,
				},
				type:"post",
				dataType:"json",
				success:function (data){
					if(data.success){//如果登录成功
						//跳转到欢迎页面
						window.location.href="workbench/index.jsp";
					}else{//如果登录失败

						$("#msg").html(data.msg);
					}

				}
			})

		}

	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<button type="button" id =submitBtn class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>