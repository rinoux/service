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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>微信JSSDK</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css"> 
	-->

</head>

<body>
	<%
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
	appId:'<%=appid%>', 
	timestamp:<%=timestamp%>, // 生成签名的时间戳
	nonceStr:'<%=nonceStr%>', 
	signature:'<%=signature%>',
			jsApiList : [ 'checkJsApi', 'configWXDeviceWiFi', 'chooseImage',
					'previewImage', 'uploadImage', 'downloadImage',
					'onMenuShareAppMessage', 'onMenuShareTimeline',
					'onMenuShareQQ', 'onMenuShareWeibo', 'scanQRCode' ]
		});
	</script>
	<h3 id="menu-basic">基础接口</h3>
	判断当前客户端是否支持指定JS接口
	<button class="btn btn_primary" id="checkJsApi">checkJsApi</button>
	<br />
	<h3 id="menu-pay">调出WiFi设置页面</h3>
	调出WiFi设置页面
	<button class="btn btn_primary" id="configWXDeviceWiFi">configWXDeviceWiFi</button>
	<br />
	<h3 id="menu-image">图像接口</h3>
	拍照或从手机相册中选图接口
	<button class="btn btn_primary" id="chooseImage">chooseImage</button>
	<br /> 预览图片接口
	<button class="btn btn_primary" id="previewImage">previewImage</button>
	<br /> 上传图片接口
	<button class="btn btn_primary" id="uploadImage">uploadImage</button>
	<br /> 下载图片接口
	<button class="btn btn_primary" id="downloadImage">downloadImage</button>
	<br />
	<h3 id="menu-share">分享接口</h3>
	获取“分享到朋友圈”按钮点击状态及自定义分享内容接口
	<button class="btn btn_primary" id="onMenuShareTimeline">onMenuShareTimeline</button>
	<br /> 获取“分享给朋友”按钮点击状态及自定义分享内容接口
	<button class="btn btn_primary" id="onMenuShareAppMessage">onMenuShareAppMessage</button>
	<br /> 获取“分享到QQ”按钮点击状态及自定义分享内容接口
	<button class="btn btn_primary" id="onMenuShareQQ">onMenuShareQQ</button>
	<br /> 获取“分享到腾讯微博”按钮点击状态及自定义分享内容接口
	<button class="btn btn_primary" id="onMenuShareWeibo">onMenuShareWeibo</button>
	<br />
	<h3 id="menu-scan">微信扫一扫</h3>
	调起微信扫一扫接口
	<button class="btn btn_primary" id="scanQRCode0">scanQRCode(微信处理结果)</button>
	<button class="btn btn_primary" id="scanQRCode1">scanQRCode(直接返回结果)</button>
	<br />
	<div id="submit_div" style="display:none;">
		<img id="myimg" width="60%" /> <input type="button" value="提交"
			onClick="funsubmitimg()" />
	</div>
</body>
<script>
	//步骤四：通过ready接口处理成功验证
	wx
			.ready(function() {

				// 1 判断当前版本是否支持指定 JS 接口，支持批量判断
				document.querySelector('#checkJsApi').onclick = function() {
					wx.checkJsApi({
						jsApiList : [ 'checkJsApi', 'configWXDeviceWiFi',
								'chooseImage', 'previewImage', 'uploadImage',
								'downloadImage', 'onMenuShareAppMessage',
								'onMenuShareTimeline', 'onMenuShareQQ',
								'onMenuShareWeibo', 'scanQRCode' ],
						success : function(res) {
							alert(JSON.stringify(res));
						}
					});
				};

				// 2.配置WiFi
				document.querySelector('#configWXDeviceWiFi').onclick = function() {

					wx.invoke('configWXDeviceWiFi', {}, function(res) {

						if (res.err_msg == "configWXDeviceWiFi:ok") {
							alert("ok");
						} else if (res.err_msg == "configWXDeviceWiFi:cancel") {
							alert("cancel");
						} else {
							alert("fail ");
						}

					});
				};

				// 3 图片接口
				// 3.1 拍照、本地选图
				//var images = {
				////	localId : [],
				//	serverId : []
				//};
				document.querySelector('#chooseImage').onclick = function() {

					wx
							.chooseImage({
								count : 1, // 默认9
								sizeType : [ 'original', 'compressed' ], // 可以指定是原图还是压缩图，默认二者都有
								sourceType : [ 'album', 'camera' ], // 可以指定来源是相册还是相机，默认二者都有
								success : function(res) {
									var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
									images.localId = res.localIds;
									//alert('已选择 ' + res.localIds.length + ' 张图片');

									document.getElementById("myimg").src = res.localIds;
									document.getElementById("submit_div").style.display = "inline";

								}
							});
				};

				// 3.2 图片预览
				document.querySelector('#previewImage').onclick = function() {
					wx
							.previewImage({
								current : 'http://img5.douban.com/view/photo/photo/public/p1353993776.jpg',
								urls : [
										'http://img3.douban.com/view/photo/photo/public/p2152117150.jpg',
										'http://img5.douban.com/view/photo/photo/public/p1353993776.jpg',
										'http://img3.douban.com/view/photo/photo/public/p2152134700.jpg' ]
							});
				};

				// 3.3 上传图片
				document.querySelector('#uploadImage').onclick = function() {
					if (images.localId.length == 0) {
						alert('请先使用 chooseImage 接口选择图片');
						return;
					}
					var i = 0, length = images.localId.length;
					images.serverId = [];
					function upload() {
						wx.uploadImage({
							localId : images.localId[i],
							success : function(res) {
								i++;
								alert('已上传：' + i + '/' + length);
								images.serverId.push(res.serverId);
								if (i < length) {
									upload();
								}
							},
							fail : function(res) {
								alert(JSON.stringify(res));
							}
						});
					}
					upload();
				};

				// 3.4 下载图片
				document.querySelector('#downloadImage').onclick = function() {
					if (images.serverId.length === 0) {
						alert('请先使用 uploadImage 上传图片');
						return;
					}
					var i = 0, length = images.serverId.length;
					images.localId = [];
					function download() {
						wx.downloadImage({
							serverId : images.serverId[i],
							success : function(res) {
								i++;
								alert('已下载：' + i + '/' + length);
								images.localId.push(res.localId);
								if (i < length) {
									download();
								}
							}
						});
					}
					download();
				};

				// 4. 分享接口
				// 4.1 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口
				document.querySelector('#onMenuShareAppMessage').onclick = function() {
					wx.onMenuShareAppMessage({
						title : '分享给朋友',
						desc : '分享给朋友。',
						link : 'http://godyangpeng123.oicp.net/',
						imgUrl : 'http://godyangpeng123.oicp.net/test.jpg',
						//								trigger : function(res) {
						//									// 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
						//									alert('用户点击发送给朋友');
						//								},
						success : function(res) {
							alert('已分享');
						},
						cancel : function(res) {
							alert('已取消');
						},
						fail : function(res) {
							alert(JSON.stringify(res));
						}
					});
					alert('已注册获取“发送给朋友”状态事件');
				};

				// 4.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
				document.querySelector('#onMenuShareTimeline').onclick = function() {
					wx.onMenuShareTimeline({
						title : '分享给朋友圈',
						desc : '分享给朋友圈。',
						link : 'http://godyangpeng123.oicp.net/',
						//imgUrl : 'http://godyangpeng123.oicp.net/test.jpg',
						//								trigger : function(res) {
						//									// 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
						//									alert('用户点击分享到朋友圈');
						//								},
						success : function(res) {
							alert('已分享');
						},
						cancel : function(res) {
							alert('已取消');
						},
						fail : function(res) {
							alert(JSON.stringify(res));
						}
					});
					alert('已注册获取“分享到朋友圈”状态事件');
				};

				// 4.3 监听“分享到QQ”按钮点击、自定义分享内容及分享结果接口
				document.querySelector('#onMenuShareQQ').onclick = function() {
					wx.onMenuShareQQ({
						title : '分享给QQ',
						desc : '分享给QQ。',
						link : 'http://godyangpeng123.oicp.net/',
						imgUrl : 'http://godyangpeng123.oicp.net/test.jpg',

						//								trigger : function(res) {
						//									alert('用户点击分享到QQ');
						//								},
						//								complete : function(res) {
						//									alert(JSON.stringify(res));
						//								},
						success : function(res) {
							alert('已分享');
						},
						cancel : function(res) {
							alert('已取消');
						},
						fail : function(res) {
							alert(JSON.stringify(res));
						}
					});
					alert('已注册获取“分享到 QQ”状态事件');
				};

				// 4.4 监听“分享到微博”按钮点击、自定义分享内容及分享结果接口
				document.querySelector('#onMenuShareWeibo').onclick = function() {
					wx.onMenuShareWeibo({
						title : '分享给微博',
						desc : '分享给微博。',
						link : 'http://godyangpeng123.oicp.net/',
						imgUrl : 'http://godyangpeng123.oicp.net/test.jpg',

						//								trigger : function(res) {
						//									alert('用户点击分享到微博');
						//								},
						//								complete : function(res) {
						//									alert(JSON.stringify(res));
						//								},
						success : function(res) {
							alert('已分享');
						},
						cancel : function(res) {
							alert('已取消');
						},
						fail : function(res) {
							alert(JSON.stringify(res));
						}
					});
					alert('已注册获取“分享到微博”状态事件');
				};

				// 5 微信原生接口
				// 5.1 扫描二维码并返回结果
				document.querySelector('#scanQRCode0').onclick = function() {
					wx.scanQRCode();
				};
				// 5.2 扫描二维码并返回结果
				document.querySelector('#scanQRCode1').onclick = function() {
					wx.scanQRCode({
						needResult : 1,
						desc : 'scanQRCode desc',
						success : function(res) {
							alert(JSON.stringify(res));
						}
					});
				};

			});

	wx.error(function(res) {
		alert(res.errMsg);
	});

	function funsubmitimg() {
		//var image = 
		alert("提交图片");
		var image = document.getElementById('myimg').src;
		$.ajax({
			type : 'POST',
			url : 'ajax/uploadimage',
			data : {
				image : image
			},
			async : false,
			dataType : 'json',
			success : function(data) {
				if (data.success) {
					alert('上传成功');
				} else {
					alert('上传失败');
				}
			},
			error : function(err) {
				alert('网络故障');
			}
		});

	}
</script>
</html>
