<%@page import="cc.rinoux.server.weixin.util.WeixinJsSdk"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cc.rinoux.server.weixin.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
	String basePathURLString;
	if(request.getQueryString()==null)
	{
		basePathURLString = request.getScheme() + "://" + request.getServerName() + request.getRequestURI();
	}
	else
	{
		basePathURLString = request.getScheme() + "://" + request.getServerName() + request.getRequestURI() + "?" + request.getQueryString();
	}
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--针对移动设备,为了确保适当的绘制和触屏缩放，网站显示宽度等于设备屏幕显示宽度,内容缩放比例为1:1，禁用缩放功能-->
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<!--将下面的 <meta> 标签加入到页面中，可以让部分国产浏览器默认采用高速模式渲染页面：-->
<meta name="renderer" content="webkit">
<title>【配网】mojimoji水杯</title>
<!-- Bootstrap -->
<link href="../Bootstrap/css/bootstrap.css" rel="stylesheet">
<!-- 通用---CSS-->
<link href="../CommonCSSJS/common.css" rel="stylesheet">
<!-- 通用---dialogBox-----CSS -->
<link href="../CommonCSSJS/jquery.dialogbox.css" rel="stylesheet">
<!-- 通用--loaders----------CSS -->
<link href="../CommonCSSJS/loaders.css" rel="stylesheet">

<!-- jQuery 必须 -->
<script src="../Bootstrap/js/jquery-1.11.3.min.js"></script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
		  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
		  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
		<![endif]-->

<!--内嵌式CSS代码-->
<style type="text/css">
.bgwifi {
	background-image: url(configwifiimage/bg.png);
}
.btnwifi {
	background-image: url(configwifiimage/configwif.png);
	top: 48%;
}
</style>
</head>
<body>
<div class="bgall">
  <div class="landscape">请竖屏使用</div>
  <div class="portrait">
    <div class="maindivsize"> 
      <!--  主界面  --- 开始  -->
      <div class="backgroundmain bgwifi">
        <div id="configWXDeviceWiFi" class="btnmain btnwifi"></div>
      </div>
      <!--  主界面  --- 结束  -->
      
      <div id="type-dialogBox"></div>
    </div>
  </div>
</div>
<div id="type-dialogBox"></div>
<!-- Include all compiled plugins (below), or include individual files as needed --> 
<script src="../Bootstrap/js/bootstrap.js"></script> 
<!-- 引入screenlog日志输出的JS文件 --> 
<script src="../Screenlog/screenlog.min.js"></script> 
<!-- 初始化screenlog日志输出 --> 
<script>
//screenLog.init({
//	color:"#000",/////字体颜色
//	bgColor:"0",/////背景色
//	releaseConsole:false
//	});
</script> 
<!-- 通用---dialogBox----JS --> 
<script src="../CommonCSSJS/jquery.dialogBox.js"></script> 
<!-- 通用--JS--> 
<script src="../CommonCSSJS/common.js"></script>
<%
		//定义函数
		WeixinJsSdk weixinJsSdk = new WeixinJsSdk();
			Map<String, String> SignMap = weixinJsSdk.getSignMap(basePathURLString);
			String appid = SignMap.get("appid");////定义appid
			String timestamp =  SignMap.get("timestamp");
			String nonceStr =  SignMap.get("nonceStr");			
			String signature =  SignMap.get("signature");
	%>
<!--步骤二：引入JS文件  --> 
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> 
<script>
    //步骤三：通过config接口注入权限验证配置
  wx.config({
	beta: true, // 开启内测接口调用，注入wx.invoke方法
	debug: false, // 开启调试模式
	appId:'<%=appid%>', // 第三方app唯一标识
	timestamp:<%=timestamp%>, // 生成签名的时间戳
	nonceStr:'<%=nonceStr%>', // 生成签名的随机串
	signature:'<%=signature%>',// 签名
			jsApiList : [ 'checkJsApi', 'configWXDeviceWiFi', 'chooseImage',
					'previewImage', 'uploadImage', 'downloadImage',
					'onMenuShareAppMessage', 'onMenuShareTimeline',
					'onMenuShareQQ', 'onMenuShareWeibo', 'scanQRCode' ]
		});
	</script> 
<script>
		//步骤四：通过ready接口处理成功验证
		wx.ready(function() {
			// 2.配置WiFi
			document.querySelector('#configWXDeviceWiFi').onclick = function() {

				wx.invoke('configWXDeviceWiFi', {}, function(res) {

					if (res.err_msg == "configWXDeviceWiFi:ok") {
						funalert("配置WiFiok");
					} else if (res.err_msg == "configWXDeviceWiFi:cancel") {
						funalert("配置WiFicancel");
					} else {
						funalert("配置WiFifail");
					}

				});
			};
		});

		wx.error(function(res) {
			funalert(res.errMsg);
		});
	</script> 
</body>
</html>
