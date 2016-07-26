<%@page import="cc.rinoux.server.weixin.util.WeixinJsSdk"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cc.rinoux.server.weixin.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	String basePathURLString;
	if (request.getQueryString() == null) {
		basePathURLString = request.getScheme() + "://"
		+ request.getServerName() + request.getRequestURI();
	} else {
		basePathURLString = request.getScheme() + "://"
		+ request.getServerName() + request.getRequestURI()
		+ "?" + request.getQueryString();
	}
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!--针对移动设备,为了确保适当的绘制和触屏缩放，网站显示宽度等于设备屏幕显示宽度,内容缩放比例为1:1，禁用缩放功能-->
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<!--将下面的 <meta> 标签加入到页面中，可以让部分国产浏览器默认采用高速模式渲染页面：-->
<meta name="renderer" content="webkit">
<title>【照片】mojimoji水杯</title>
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
<style type="text/css" >
.bgmain {
	background-image: url(image/bg.png);
}
.bgcut {
	background-image: url(image/bgg.png);
}
.bgstyle {
	background-image: url(image/bgg.png);
}
.btnmain {
	width: 50%;
	height: 10%;
	max-width: 200px;
	max-height: 50px;
	margin: auto;
	background-size: 100% 100%;
	position: relative;
}
.btnopencamera {
	background-image: url(image/camera1.png);
	top: 48%;
}
.btnopenalbum {
	background-image: url(image/album1.png);
	top: 58%;
}
.btncut {
	background-image: url(image/btncut.png);
	top: 2%;
}
.topdivcut {
	width: 100%;
	height: 5%;
	margin: auto;
}
.topdivstyle {
	width: 100%;
	height: 5%;
	margin: auto;
}
.showcat {
	width: 100%;
	height: 75%;
	margin: auto;
}
.cutsize {
	width: 100%;
	height: 100%;
}
.showstyle {
	width: 100%;
	height: 73%;
	margin: auto;
}
.chosestylediv {
	width: 100%;
	height: 22%;
	margin: auto;
	max-height: 100px;
	padding-top: 20px;
}
.originaldiv {
	height: 100%;
	width: 20%;
	float: left;
	max-width: 100px;
}
.stylediv {
	height: 100%;
	float: left;
	width: 60%;
	overflow: scroll;
}
.subcontainer {
	width: 960px;
	height: 100%;
}
.chosedivstyle {
	height: 100%;
	float: left;
	min-width: 80px;
}
.styletitle {
	width: 100%;
	height: 20px;
	font-family: "Adobe Heiti Std";
	color: rgb(255, 255, 255);
	line-height: 20px;
	text-align: center;
	background-color: #2b2b2b;
}
.thumbnaildivimage {
	border-radius: 50%;
	width: 60px;
	height: 60px;
	margin: auto;
	background-size: 100% 100%;
	border: 1px solid #FFFFFF;
}
.thumbnailimg {
	border-radius: 50%;
	width: 100%;
	height: 100%;
}
.btnsurediv {
	height: 100%;
	width: 20%;
	float: right;
	font-family: "Adobe Heiti Std";
	color: rgb(255, 255, 255);
	line-height: 20px;
	text-align: center;
	background-color: #2b2b2b;
	vertical-align: middle;
	background-size: 100% 100%;
	background-image: url(image/btnnext.png);
}
</style>
</head>
<body>
<div class="bgall">
  <div class="landscape">请竖屏使用</div>
  <div class="portrait">
    <div class="maindivsize"> 
      <!--  主界面  --- 开始  -->
      <div class="backgroundmain bgmain">
        <div id="btncamera" class="btnmain btnopencamera"></div>
        <div id="btnalbum" class="btnmain btnopenalbum"></div>
      </div>
      <!--  主界面  --- 结束  --> 
      
      <!--  裁剪界面  --- 开始  -->
      <div class="backgroundmain bgcut">
        <div class="topdivcut"></div>
        <div class="showcat">
          <div class="container cutsize"><img></div>
        </div>
        <div id="btncutid" class="btnmain btncut"></div>
      </div>
      <!--  裁剪主界面  --- 结束  --> 
      
      <!--  风格选择界面  --- 开始  -->
      <div class="backgroundmain bgstyle">
        <div class="topdivstyle"></div>
        <div class="showstyle">
          <div id="showstylesize"></div>
        </div>
        <div class="chosestylediv">
          <div class="originaldiv">
            <div class="styletitle">原图 </div>
            <div class="thumbnaildivimage"> <img onClick="funchosestyle(0)" class="thumbnailimg" id="originalimage"/> </div>
          </div>
          <div class="stylediv"> 
            <!--  ----------- 开始  -->
            <div class="subcontainer"> 
              <!--  ----------- 风格----1--  -->
              <div class="chosedivstyle">
                <div class="styletitle">腐蚀 </div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(1)" class="thumbnailimg" id="styleimage1"/> </div>
              </div>
              <!--  ----------- 风格----2--  -->
              <div class="chosedivstyle">
                <div class="styletitle">灰度</div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(2)" class="thumbnailimg" id="styleimage2"/> </div>
              </div>
              <!--  ----------- 风格----3--  -->
              <div class="chosedivstyle">
                <div class="styletitle">马赛克</div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(3)" class="thumbnailimg" id="styleimage3"/> </div>
              </div>
              <!--  ----------- 风格----4--  -->
              <div class="chosedivstyle">
                <div class="styletitle">浮雕 </div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(4)" class="thumbnailimg" id="styleimage4"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
              <!--  ----------- 风格----5--  -->
              <div class="chosedivstyle">
                <div class="styletitle">锐化 </div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(5)" class="thumbnailimg" id="styleimage5"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
              <!--  ----------- 风格---6--  -->
              <div class="chosedivstyle">
                <div class="styletitle">自然增强 </div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(6)" class="thumbnailimg" id="styleimage6"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
              <!--  ----------- 风格---7--  -->
              <div class="chosedivstyle">
                <div class="styletitle">美肤 </div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(7)" class="thumbnailimg" id="styleimage7"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
              <!--  ----------- 风格----8-  -->
              <div class="chosedivstyle">
                <div class="styletitle">暖秋</div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(8)" class="thumbnailimg" id="styleimage8"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
              <!--  ----------- 风格---9--  -->
              <div class="chosedivstyle">
                <div class="styletitle">仿lomo </div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(9)" class="thumbnailimg" id="styleimage9"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
              <!--  ----------- 风格----10--  -->
              <div class="chosedivstyle">
                <div class="styletitle">素描</div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(10)" class="thumbnailimg" id="styleimage10"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
              <!--  ----------- 风格---11--  -->
              <div class="chosedivstyle">
                <div class="styletitle">复古 </div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(11)"  class="thumbnailimg" id="styleimage11"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
              <!--  ----------- 风格---12--  -->
              <div class="chosedivstyle">
                <div class="styletitle">亮度增强 </div>
                <div class="thumbnaildivimage"> <img onClick="funchosestyle(12)"  class="thumbnailimg" id="styleimage12"/> </div>
              </div>
              <!--  ----------- - 结束  --> 
            </div>
            <!--  ----------- - 结------束  --> 
          </div>
          <div class="btnsurediv"  onClick="funbtnsurenext()" ></div>
        </div>
      </div>
      <!--  风格选择主界面  --- 结束  --> 
      
      <!--  选择好友界面  --- 开始  -->
      <div class="backgroundmain bgchose">
        <div class="topdivchose">
          <div style="width: 100%; height: 50px; padding: 10px;">
            <div style="float:left; width:20%; height:30px;">
              <div class="searchimgdiv"></div>
            </div>
            <div style="float:left; width:40%; height:30px;">
              <form>
                <input type="text" class="form-control" placeholder="搜索" style="border:0;">
              </form>
            </div>
            <div style="float:left; width:20%; height:30px; text-align:center; line-height:30px;">
              <div id="chosedcountdiv">（ 0/5 ）</div>
            </div>
            <div onClick="funsendpictocup()" class="btnnextdiv"> </div>
          </div>
          <div style=" height:5px; width:100%; border-top: 2px solid #888888;"></div>
          <div style=" height:20px; width:100%; background-color:#888888"></div>
        </div>
        <div id="friendListAllDiv" class="friendlistclass"> </div>
      </div>
      <!--  选择好友界面  --- 结束  --> 
      
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
<script>
<!-- 内嵌式JS代码 -->
///////窗体就绪事件
var orgimgbasedata;//////原始图片像素数据
$(document).ready(function(e) {	
	$(".backgroundmain.bgmain").show();
	$(".backgroundmain.bgcut").hide();
	$(".backgroundmain.bgstyle").hide();
	$(".backgroundmain.bgchose").hide();
	
	//orgimgbasedata = "image/test/4.PNG";
	//funchosestylefrom("image/test/4.PNG");////进入风格选择界面
});
</script>
<%


            WeixinJsSdk weixinJsSdk =  new WeixinJsSdk();
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
			jsApiList : [ 'chooseImage', 'uploadImage', 'downloadImage' ]
		});
	</script> 
<script>
		//步骤四：通过ready接口处理成功验证
		wx.ready(function() {
			////打开相机
			document.querySelector("#btncamera").onclick = function(){			
				wx.invoke('chooseImage', {
					count : 1, // 默认9
					sizeType : [ 'original', 'compressed' ], // 可以指定是原图还是压缩图，默认二者都有
					sourceType : [ 'camera' ], // 可以指定来源是相册还是相机，默认二者都有				
				}, function(res) {
					if (res.err_msg == "chooseImage:ok") {
						upLoadImage(res.localIds);
					} else if (res.err_msg == "chooseImage:cancel") {
						funalert("取消拍照");
					} else {
						funalert("拍照失败");
					}
				});
			};

			////打开相册		
			document.querySelector("#btnalbum").onclick = function(){
				wx.invoke('chooseImage', {
					count : 1, // 默认9
					sizeType : [ 'original', 'compressed' ], // 可以指定是原图还是压缩图，默认二者都有
					sourceType : [ 'album' ], // 可以指定来源是相册还是相机，默认二者都有				
				}, function(res) {
					if (res.err_msg == "chooseImage:ok") {
						timetemp = setInterval("funtest()", 1);
						upLoadImage(res.localIds);
					} else if (res.err_msg == "chooseImage:cancel") {
						funalert("取消图片选择");
					} else {
						funalert("图片选择失败");
					}
				});
			};
		});
		wx.error(function(res) {
			funalert(res.errMsg);
		});
	</script> 
<script>
//////上传图片
function upLoadImage(localIds) {
								
	$(".backgroundmain.bgmain").hide();
	$(".backgroundmain.bgcut").show();
	$(".backgroundmain.bgstyle").hide();
	$(".backgroundmain.bgchose").hide();	
	
	var localId = dealLocalIds(localIds);////处理localIds
	console.log(localId);
	wx.uploadImage({
		localId : localId, // 需要上传的图片的本地ID，由chooseImage接口获得
		isShowProgressTips : 0, // 默认为1，显示进度提示
		success : function(res) {
			downLoadImage(res.serverId);
		}
	});
}
</script> 
<script>
		//////处理localIds
		function dealLocalIds(localIds) {
			var localId;//////选择图片src
			var signtemp = localIds.indexOf("[");
			if (signtemp == 0) {
				/////Android设备
				var localIdall = localIds.toString();
				var localId1 = localIdall.substr(2, 7);
				var localId2 = localIdall.substr(13, 10);
				var localId3 = localIdall.substring(25, localIdall.length - 2);
				localId = localId1 + "//" + localId2 + "/" + localId3;
			} else {
				////IOS设备
				localId = localIds[0];
			}
			return localId;
		}
	</script> 
<script>
		//////服务器下载图片
		function downLoadImage(serverId) {
			//funalert("服务器下载图片");
			$.post("DownLoadImage.jsp", {
				serverId : serverId
			}, function(data, status) {
				if (status == "success") {
					openPic(data);
				} else {
					console.log("获取排名异常");
					funalert("服务器下载图片异常");
				}
			});
		}
	</script> 
<script>
		//////服务器下载图片成功之后显示导裁剪框
		function openPic(imgSrcs) {
			var imgaa = new Image();
			imgaa.src = imgSrcs;
			imgaa.onload = function() {
				///////显示导裁剪框
				
				$('.container > img').cropper('replace', imgSrcs);
//								
//				$(".background.bgmain").hide();
//				$(".background.bgcut").show();
//				$(".background.bgstyle").hide();
				///$(".background.bgchose").hide();
				
			};
			imgaa.onerror = function() {//监听LoadImage的onerror函数
				funalert("LoadImage的onerror函数");
			};
		}
	</script> 
<!-- jQuery is required -->
<link href="cutPicCss/cropper.css" rel="stylesheet">
<script src="cutPicJs/cropper.js"></script> 
<!--<script src="jpeg_encoder_basic.js"></script>--> 
<script>
		$('.container > img').cropper({

			aspectRatio : 2 / 3,
			autoCropArea : 0.6, // 80%
			movable : false, // Enable to move the crop box
			dragCrop : false,
			resizable : false,

			crop : function(data) {
				// Output the result data for cropping image.
			}
		});
	</script> 
<script>
		var xmlhttp;
		document.querySelector("#btncutid").onclick = function(){
			
			console.log($('.container > img').cropper('getCroppedCanvas'));
			var main_canvas = $('.container > img').cropper('getCroppedCanvas',
					{
						width : 320,
						height : 480
					});
			var dataurl = main_canvas.toDataURL("image.jpg");

			orgimgbasedata = dataurl;
			console.log("获取图片像素数据");
			
			funchosestylefrom(dataurl);////进入风格选择界面
			
//			var blob = dataURLtoBlob(dataurl);
//			var fd = new FormData();
//			fd.append("image", blob, "image.jpg");
//			
//			var url = "http://192.168.4.254:8080/cup_service/rest/images/v1/1/3";
//			
//			xmlhttp = null;
//			if (window.XMLHttpRequest) {// code for IE7, Firefox, Opera, etc.
//				xmlhttp = new XMLHttpRequest();
//			} else if (window.ActiveXObject) {// code for IE6, IE5
//				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
//			}
//			if (xmlhttp != null) {
//				xmlhttp.onreadystatechange = state_Change;
//				xmlhttp.open("POST", url, true);
//				xmlhttp.send(fd);
//			} else {
//				console.log("Your browser does not support XMLHTTP.");
//				funalert("Your browser does not support XMLHTTP.");
//			}
		}

		////接收到数据之后处理
		function state_Change() {
			if (xmlhttp.readyState == 4) {// 4 = "loaded"
				if (xmlhttp.status == 200) {// 200 = "OK"
					console.log(xmlhttp.status);
					console.log(xmlhttp.statusText);
					//console.log(xmlhttp.responseText);
					funalert(xmlhttp.responseText);
				} else {
					console.log("Problem retrieving XML data:"
							+ xmlhttp.statusText);
				}
			}
		}
		//////画布dataurl转blob
		function dataURLtoBlob(dataurl) {
			var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(
					n);
			while (n--) {
				u8arr[n] = bstr.charCodeAt(n);
			}
			return new Blob([ u8arr ], {
				type : mime
			});
		}
	</script> 
<script>
<!-- 内嵌式JS代码 -->
var showstylewidth;////showstyle宽
var showstyleheight;////showstyle高
var unitsize;//////showstyle单位大小
var canvasw = 2;
var canvash = 3;


/////进入风格选择界面
function funchosestylefrom(jsonstring)
{
	//funalert("进入风格选择界面");
	$(".backgroundmain.bgmain").hide();
	$(".backgroundmain.bgcut").hide();
	$(".backgroundmain.bgstyle").show();
	$(".backgroundmain.bgchose").hide();

	var widthtemp = Math.floor($(".showstyle").width());////获取showstyle宽
	var heighttemp = Math.floor($(".showstyle").height());////获取showstyle高
	
	fungetwhfromdiv(widthtemp,heighttemp);/////根据showdiv大小计算showstyle大小
	funsetshowstylediv();//////设置showstyle位置		
	funsetchosestylesize();/////设置底部风格选择区域的
	
	funchosestyle(0);	///////选择风格进行切换

}

 /////根据div大小计算canvas大小
function fungetwhfromdiv(widthtemp,heighttemp){
	unitsize = Math.min(widthtemp / canvasw,heighttemp / canvash);		
	unitsize = Math.floor(unitsize);	
	showstylewidth = unitsize * canvasw;
	showstyleheight = unitsize * canvash;
	//showstylewidth = Math.min(showstylewidth,320);
	//showstyleheight = Math.min(showstyleheight,480);	
} 

//////设置canvas位置
function funsetshowstylediv()
{
	$("#showstylesize").css("width",showstylewidth);
	$("#showstylesize").css("height",showstyleheight);	
	var marginh = Math.floor(($(".showstyle").height() - showstyleheight) * 0.5) + "px";
	var marginw = Math.floor(($(".showstyle").width() - showstylewidth) * 0.5) + "px";
	$("#showstylesize").css("margin-top",marginh);	
	$("#showstylesize").css("margin-left",marginw);	
	$("#showstylesize").css("margin-right",marginw);	
	$("#showstylesize").css("margin-bottom",marginh);		
}

/////设置底部风格选择区域的大小
function funsetchosestylesize()
{
	$(".originaldiv").css("width",Math.floor($(".chosestylediv").height()));
	$(".stylediv").css("width",Math.floor($(".chosestylediv").width()) - Math.floor($(".originaldiv").width()) - Math.floor($(".btnsurediv").width()));
	$(".chosedivstyle").css("width",Math.floor($(".chosestylediv").height()));
}
</script> 
<!-- 图像处理 --> 
<script src="http://alloyteam.github.io/AlloyPhoto/js/combined/alloyimage.js"></script> 
<script>
<!-- 内嵌式JS代码 -->
///////选择风格进行切换
function funchosestyle(styleId)
{
	//thumbnaildivimage
	document.getElementById("showstylesize").innerHTML = "";	
	
	funshowstylethumbnail(styleId);///////显示风格缩略图
	var pics = new Image();
	pics.src = orgimgbasedata;

	switch(styleId)
	{
		case 0://////原始图片
			/////显示原图
						  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).act("brightness",0,0).show("#showstylesize");					
			});
			
		break;
		case 1://///风格1
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).act("corrode").show("#showstylesize");					
			});
						
			////显示风格1
		break;
		case 2://///风格2
			
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).act("灰度处理").show("#showstylesize");			
			});
			
			////显示风格2
		break;
		case 3://///风格3
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).act("mosaic",2).show("#showstylesize");				
			});
			
			////显示风格3
		break;
		case 4://///风格4
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).act("embossment").show("#showstylesize");				
			});
			
			////显示风格4
		break;
		case 5://///风格5
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).act("sharp",10).show("#showstylesize");				
			});
			
			////显示风格5
		break;
		case 6://///风格6
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).ps("自然增强").show("#showstylesize");				
			});
			
			////显示风格6
		break;
		case 7://///风格7
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).ps("softenFace").show("#showstylesize");				
			});
			
			////显示风格8
		break;
		case 8://///风格8
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).ps("warmAutumn").show("#showstylesize");				
			});
			
			////显示风格8
		break;
		case 9://///风格9
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).ps("lomo").show("#showstylesize");				
			});
			
			////显示风格9
		break;
		case 10://///风格10
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).ps("sketch").show("#showstylesize");				
			});
			
			////显示风格10
		break;
		case 11://///风格11
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).ps("vintage").show("#showstylesize");				
			});
			
			////显示风11
		break;
		case 12://///风格12
		
			  
			pics.loadOnce(function(){			
				AlloyImage(this,showstylewidth,showstyleheight).act("brightness",10,20).show("#showstylesize");				
			});
			
			////显示风格12
		break;
		
		default:
			//////n 与 case 1 和 case 2 不同时执行的代码
	}
}

var transcanvasdataurl;/////最终数据

///////显示风格缩略图
function funshowstylethumbnail(styleId)
{
	var thumbnailimg = document.getElementsByClassName("thumbnailimg");
	for(var i = 0;i < thumbnailimg.length;i ++){
		thumbnailimg[i].src = orgimgbasedata;
	}
	var thumbnaildivimage = document.getElementsByClassName("thumbnaildivimage");
	for(var i = 0;i < thumbnaildivimage.length;i ++){
		thumbnaildivimage[i].style.border = "1px solid #FFFFFF";
	}
	thumbnaildivimage[styleId].style.border = "2px solid #0000FF";
}

///////选好风格下一步选择好友
function funbtnsurenext()
{	
	var initcanvas = document.getElementById("showstylesize").childNodes[0];	
	var initcanvasdataurl = initcanvas.toDataURL("image.jpg");

	var transcanvas = document.createElement('canvas'); 
	transcanvas.id = "canvasimage"; 
	transcanvas.width = 320; 
	transcanvas.height = 480; 			
	transcanvas.style.display = "none"; 
	document.body.appendChild(transcanvas);

	var initimage = new Image();
	initimage.src = initcanvasdataurl;
	initimage.onload = function() {
	
		var transcanvas=document.getElementById("canvasimage");
		var transcanvasctx=transcanvas.getContext("2d");		
		transcanvasctx.drawImage(initimage,0,0,320,480);		
		
		transcanvasdataurl = transcanvas.toDataURL("image.jpg");

		$(".backgroundmain.bgmain").hide();
		$(".backgroundmain.bgcut").hide();
		$(".backgroundmain.bgstyle").hide();
		$(".backgroundmain.bgchose").show();
				
		funshowchosefrirnd();//////进入好友选择界面----初始化
	};

}

</script>
</body>
</html>