// JavaScript Document
/////弹窗函数
function funalert(msg)
{
	$('#type-dialogBox').dialogBox({
			
			width: 300,
			//弹出层宽度
			height: null,
			//弹出层高度
			autoSize: true,
			//是否自适应尺寸,默认自适应
			autoHide: false,
			//是否自自动消失，配合time参数共用
			time: 3000,
			//自动消失时间，单位毫秒
			zIndex: 99999,
			//弹出层定位层级
			hasMask: true,
			//是否显示遮罩层
			hasClose: true,
			//是否显示关闭按钮
			hasBtn: true,
			//是否显示操作按钮，如取消，确定
			confirmValue: null,
			//确定按钮文字内容
			confirm: function() {},
			//点击确定后回调函数
			cancelValue: null,
			//取消按钮文字内容
			cancel: function() {},
			//点击取消后回调函数，默认关闭弹出框
			effect: '',
			//动画效果：fade(默认),newspaper,fall,scaled,flip-horizontal,flip-vertical,sign,
			type: 'normal',
			//对话框类型：normal(普通对话框),correct(正确/操作成功对话框),error(错误/警告对话框)
			title: '<br/>mojimoji提示您',
			//标题内容，如果不设置，则连同关闭按钮（不论设置显示与否）都不显示标题
			content: msg //正文内容，可以为纯字符串，html标签字符串，以及URL地址，当content为URL地址时，将内嵌目标页面的iframe。

		});	
}
	
	
	
	
//////进入好友选择界面----初始化
function funshowchosefrirnd()
{
	
	
	chosedcount = 0;//////当前选择好友总数
	
	/////添加自己
	var temp1 = '<div class="listdivclass" onClick="funchosefriend(this)" id="listdividmyself"><div class="listdivhead"><div class="listheadimg" style="background-image:url(image/touxinag.jpg);"></div></div><div class="listdivname">发送给自己</div><div class="listdivchose listdivnochose"></div></div>';
	$("#friendListAllDiv").append(temp1);
	
	/////添加显示好友
	for(var i=1; i<50; i++)
	{		
		//console.log(i); class="listdivclass" id="listdivid0"
		var temp = '<div class="listdivclass" onClick="funchosefriend(this)" id="listdivid' + i + '"><div class="listdivhead"><div class="listheadimg" style="background-image:url(image/touxinag.jpg);"></div></div><div class="listdivname">小明' + i + '</div><div class="listdivchose listdivnochose"></div></div>';
		$("#friendListAllDiv").append(temp);
	}
}





var chosedcount;//////当前选择好友总数
var chosedfriend = new Array();////已经选择的好友ID

function funchosefriend(obj)
{
	var userid = obj.id;////当前选择的用户ID
	
	////判断当前选择用户是否选中
	var chosediD = document.getElementById(userid).childNodes[2];
	var chosedIdclassname = chosediD.className;	
	
	
	if(chosedIdclassname == "listdivchose listdivnochose")
	{
		////如果未选择-----进行选择
		if(chosedcount >= 5)
		{
			funalert("最多选择5个");	
			
		}
		else
		{
			chosediD.className = "listdivchose listdivchosed";
			chosedfriend.push(userid);
			chosedcount++;/////已经选择好友数 +++ 1
		}
	}
	else
	{
		////如果选择-------取消选择		
		chosediD.className = "listdivchose listdivnochose";		
		for(var i=0;i<chosedfriend.length;i++)
		{
			if(chosedfriend[i] == userid)
			{
				chosedfriend.splice(i,1);
			}
		}
		chosedcount--;/////已经选择好友数 ---- 1
	}
	document.getElementById("chosedcountdiv").innerHTML = " （" + chosedcount + "/5） ";
}

var xmlhttp;
////发送图片到水杯
function funsendpictocup()
{
	funalert("发送图片到水杯");	
	
	
	
	/////开始发送
	$(".bgall").remove();
	$("body").append('<img src=' + transcanvasdataurl + '>');

	//console.log(transcanvasdataurl)

//		console.log("开始发送");
//	
//		var blob = dataURLtoBlob(transcanvasdataurl);
//		var fd = new FormData();
//		fd.append("image", blob, "image.jpg");
//		//console.log(fd);
//		var url = "http://192.168.4.254:8080/cup_service/rest/images/v1/1/3";
//		
//		xmlhttp = null;
//		if (window.XMLHttpRequest) {// code for IE7, Firefox, Opera, etc.
//			xmlhttp = new XMLHttpRequest();
//		} else if (window.ActiveXObject) {// code for IE6, IE5
//			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
//		}
//		if (xmlhttp != null) {
//			xmlhttp.onreadystatechange = state_Change;
//			xmlhttp.open("POST", url, true);
//			xmlhttp.send(fd);
//		} else {
//			console.log("Your browser does not support XMLHTTP.");
//			funalert("Your browser does not support XMLHTTP.");
//		}
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
		
