var canvas_width; ////画布宽
var canvas_height; ////画布高
var Main_canvas; /////画布
var canvas_cxt; /////画布2D
var unitsize; //////画布单位大小
var canvasw = 2;
var canvash = 3;
var ListAllPathArray = new Array(); /////所有当前显示路径的所有信息
var ListPointArray; /////每条路径的坐标集合
var PicId = new Image(); /////图片的ID
var NowShowNum = -1; /////当前显示的路径的ID
var transcanvasdataurl;//////画画板的图片数据
var ListImage = new Array();/////资源预加载图片数组
var ListImageUrl = new Array();////资源预加载地址数

var DefaultBgColor = "#FFFFFF"; ////默认背景色----------填充色
var SginPenOrPic = 1; /////画笔还是图片标志位0----清空  1---画笔,橡皮   2----图片
var NowStrokeStyle = "#123456";//////当前画笔颜色
var NowLineWidth = 6;////当前线条宽度;
var PicSize = 50; ////图片默认大小
var flagshowcolorsizerubber = 0;//////当前显示为颜色，画笔大小，橡皮大小标志位
var flagshowbg = "color";//////当前显示为颜色，画笔大小，橡皮大小标志位


/////////窗体就绪事件
$(document).ready(function(e) {
	
	//////图片资源url列表(定义索引)
	funListImageUrlUpdate();
	////资源加载----成功添加图片div
	funpreLoadImage(0);
/////显示画画板界面---隐藏好友选择界面
	$(".backgroundmain.draw").show();
	$(".backgroundmain.bgchose").hide();
	/////显示画笔图片按钮--隐藏图片和画笔选择div	
	$("#penandpicdiv").show();
	$("#chosedivpic").hide();
	$("#chosedivpen").hide();



	var widthtemp = Math.floor($(".showcanvasdiv").width()); ////获取画布宽
	var heighttemp = Math.floor($(".showcanvasdiv").height()); ////获取画布高

	fungetwhfromdiv(widthtemp, heighttemp); /////根据div大小计算canvas大小
	funsetcanvaslocaltion(); //////设置canvas位置		

	Main_canvas = document.getElementById("main_canvas"); /////获取画布
	canvas_cxt = Main_canvas.getContext("2d"); ////获取canvas的2d	
	Main_canvas.width = canvas_width; ////设置宽
	Main_canvas.height = canvas_height; ////设置高

	canvasInit(); ///////canvas初始化	
	/////加在图片	

	document.getElementById('main_canvas').addEventListener("touchstart", handleTouchstart, false); /////绑定Main_Div的触摸开始事件
	document.getElementById('main_canvas').addEventListener("touchend", handleTouchend, false); /////绑定Main_Div的触摸结束事件
	document.getElementById('main_canvas').addEventListener("touchmove", handleTouchmove, false); /////绑定Main_Div的触摸移动事
	
	//funalert("最多选择5个");	
	//funfunalert("最多选择5个");					

});




//////图片预加载
function funpreLoadImage(index){
	var LoadImage  =  new Image();//生成一个LoadImage对象
	LoadImage.src = ListImageUrl[index];//LoadImage对象的src属性指向当前被循环到的url
	LoadImage.onload = function(){//监听LoadImage的onload函数
		ListImage.push(LoadImage);

		index++;//index加1，表示要开始加载下一张图片
		if(index < ListImageUrl.length){//如果没有超过数组长度的话，继续加载下一张图片
			funpreLoadImage(index);
		}
		else
		{	
			funLoadImagesuccess();///////图片资源加在完毕
		}
	}
	LoadImage.onerror = function(){//监听LoadImage的onerror函数
		//funpreLoadImage(index);
	}
}

//////图片资源url列表(定义索引)
function funListImageUrlUpdate(){
	for(var i=0;i<5;i++)
	{
		var temp = "image/hhxywz/" + (i+1) + ".png";
		ListImageUrl.push(temp);
	}
}


//////图片资源加在完毕
function funLoadImagesuccess(){
	
	var imgcount = ListImageUrl.length;
	var subcontainerywzheight = $("#subcontainerywz").innerHeight();
	
	$("#subcontainerywz").width(imgcount * 80 + "px");
	
	for(var i=0;i<imgcount;i++)
	{
		var imglistid = "imglist" + i;
		var bgimage = ListImageUrl[i];
		
		var temp = '<div id=' + imglistid + ' class="imglistclass" style=" width: 80px; background-image: url(' + bgimage + ');" onClick="funChangePic(' + i + ')"></div>';

		$("#subcontainerywz").append(temp);
	}	
}


/////根据div大小计算canvas大小
function fungetwhfromdiv(widthtemp, heighttemp) {
	unitsize = Math.min(widthtemp / canvasw, heighttemp / canvash);
	unitsize = Math.floor(unitsize);
	canvas_width = unitsize * canvasw;
	canvas_height = unitsize * canvash;
}

//////设置canvas位置
function funsetcanvaslocaltion() {
	var marginh = Math.floor(($(".showcanvasdiv").height() - canvas_height) * 0.5) + "px";
	var marginw = Math.floor(($(".showcanvasdiv").width() - canvas_width) * 0.5) + "px";
	$("#main_canvas").css("margin-top", marginh);
	$("#main_canvas").css("margin-left", marginw);
	$("#main_canvas").css("margin-right", marginw);
	$("#main_canvas").css("margin-bottom", marginh);
}

//////canvas初始化
function canvasInit() {
	canvas_cxt.fillStyle = DefaultBgColor; ////背景色
	canvas_cxt.fillRect(0, 0, canvas_width, canvas_height);
	canvas_cxt.strokeStyle = NowStrokeStyle; ////画笔色
	canvas_cxt.lineWidth = NowLineWidth; /////画笔大小
	canvas_cxt.lineCap = "round";
	canvas_cxt.lineJoin = "round";
}

///////Main_Div的触摸开始事件
function handleTouchstart(event) {
	event.preventDefault(); //阻止滚动	

	/////隐藏选择DIV
	$("#chosedivpic").hide();
	$("#chosedivpen").hide();
	flagshowcolorsizerubber = 0;
	
	if (event.changedTouches.length == 1) {
		var point = funEventToPoint(event);
		if (SginPenOrPic == 0) {
			/////0-清空————无操作
		} else if (SginPenOrPic == 1) {
			/////1-画笔				
			///起始圆点
			canvas_cxt.beginPath();
			canvas_cxt.arc(point.x, point.y, canvas_cxt.lineWidth * 0.1, 0, 2 * Math.PI);
			canvas_cxt.stroke();

			canvas_cxt.beginPath();
			canvas_cxt.moveTo(point.x, point.y);
			canvas_cxt.lineTo(point.x, point.y);
			canvas_cxt.stroke();
			ListPointArray = new Array();
			ListPointArray.push(point); ////添加点		
		} else if (SginPenOrPic == 2) {
			/////2-图片	
			funDrawCanvas(); /////绘制canvas
			funDrawPic(point); ////绘制图片		
		}
	}
}

/////Main_Div的触摸移动事件
function handleTouchmove(event) {
	event.preventDefault(); //阻止滚动
	if (event.changedTouches.length == 1) {
		var point = funEventToPoint(event);
		if (SginPenOrPic == 0) {
			/////0-清空————无操作
		} else if (SginPenOrPic == 1) {
			////1-画笔
			canvas_cxt.lineTo(point.x, point.y);
			canvas_cxt.stroke();
			ListPointArray.push(point); ////添加点		
		} else {
			/////2-图片		
			funDrawCanvas(); /////绘制canvas
			funDrawPic(point); ////绘制图片		
		}
	}
}

/////Main_Div的触摸结束事件
function handleTouchend(event) {
	event.preventDefault(); //阻止滚动
	if (event.changedTouches.length == 1) {
		
		var point = funEventToPoint(event);
		if (SginPenOrPic == 0) {
			/////0-清空————无操作
		} else if (SginPenOrPic == 1) {
			////1-画笔
			///起始圆点
			canvas_cxt.beginPath();
			canvas_cxt.arc(point.x, point.y, canvas_cxt.lineWidth * 0.1, 0, 2 * Math.PI);
			canvas_cxt.stroke();

			canvas_cxt.lineTo(point.x, point.y);
			canvas_cxt.stroke();

			ListPointArray.push(point); ////添加点	

			var PointTemp = ListPointArray;
			var color = canvas_cxt.strokeStyle;
			var size = canvas_cxt.lineWidth;
			var AllPathTemp = funAddListAllInfor(1, color, size, PointTemp, ""); ////添加点，性质，颜色，大小

			if (NowShowNum == ListAllPathArray.length - 1) {
				ListAllPathArray.push(AllPathTemp);
			} else {
				ListAllPathArray.splice(NowShowNum + 1, 0, AllPathTemp);
			}
			NowShowNum++; /////当前显示路径 + 1
			console.log("当前显示路径ID-------" + NowShowNum);
		} else {
			/////2-图片
			funDrawCanvas(); /////绘制canvas
			funDrawPic(point); ////绘制图片
			funSaveThisPic(point);/////保存此次绘制	
		}
	}
}

////EVENT转为点
function funEventToPoint(event) {
	var x = event.changedTouches[0].clientX;
	var y = event.changedTouches[0].clientY;
	x -= $("#main_canvas").offset().left;
	y -= $("#main_canvas").offset().top;
	return {
		x: x,
		y: y
	};
}

////添加点，性质，颜色，大小
function funAddListAllInfor(SginPenOrPic, color, size, PointTemp, PicUrl) {
	return {
		sgin: SginPenOrPic,
		color: color,
		size: size,
		point: PointTemp,
		picurl: PicUrl
	};
}

/////////切换为画笔
function funPen() {
	
	
	$("#chosedivpic").hide();
	$("#chosedivpen").toggle();		
	
	
	$("#chosecolorid").hide();
	$("#chosesizeid").show();
	$("#choserubbersizeid").hide();
	
	if($("#chosecolorid").css("display") == "none")
	{
		$(".chosecolorsizerubber.color").css("background-image","url(image/colorbg.png)");
		
	}
	else
	{
		$(".chosecolorsizerubber.color").css("background-image","url(image/size.png)");
	}
	
	SginPenOrPic = 1;
}

/////////切换为图片
function funPic() {
	$("#chosedivpic").toggle();
	$("#chosedivpen").hide();
}

//////画笔大小改变
function funChangeSize(size) {	
	NowLineWidth = size * 3; /////画笔大小
	console.log("画笔大小为----" + canvas_cxt.lineWidth);		
	canvas_cxt.lineWidth = NowLineWidth; /////画笔大小
	canvas_cxt.strokeStyle = NowStrokeStyle; ////画笔色
}


//////橡皮大小改变
function funChangeRubberSize(size) {	
	NowLineWidth = size * 3; /////画笔大小
	console.log("画笔大小为----" + canvas_cxt.lineWidth);		
	canvas_cxt.lineWidth = NowLineWidth; /////画笔大小
	console.log("橡皮");
	canvas_cxt.strokeStyle = DefaultBgColor;
}


//////画笔颜色改变
function funChangeColor(obj){
	NowStrokeStyle = obj.style.backgroundColor;////当前选择的颜色		
	canvas_cxt.strokeStyle = NowStrokeStyle; ////画笔色
}



/////画笔大小和颜色选择切换
function funChangeSizeColor(){

		/////当前显示为橡皮----隐藏橡皮
		$("#choserubbersizeid").hide();	
		$("#chosecolorid").hide();
		$("#chosesizeid").hide();
	
		if(flagshowbg == "color")		
		{
			$("#chosecolorid").show();
			flagshowcolorsizerubber = 1;
			$(".chosecolorsizerubber.color").css("background-image","url(image/size.png)");
			flagshowbg = "size";	
		}
		else
		{
			$("#chosesizeid").show();	
			flagshowcolorsizerubber = 2;
			$(".chosecolorsizerubber.color").css("background-image","url(image/colorbg.png)");
			flagshowbg = "color";		
		}
}


//////橡皮
function funRubber(){			
	console.log("橡皮");
	canvas_cxt.strokeStyle = DefaultBgColor;	
	
	if(flagshowcolorsizerubber == 3)
	{
		/////当前显示为橡皮----隐藏橡皮
		$("#choserubbersizeid").hide();	
		$("#chosecolorid").hide();
		$("#chosesizeid").hide();
	
		if(flagshowbg == "size")		
		{
			$("#chosecolorid").show();
			flagshowcolorsizerubber = 1;	
		}
		else
		{
			$("#chosesizeid").show();	
			flagshowcolorsizerubber = 2;	
		}		
	}
	else
	{
		/////当前显示不是橡皮------显示橡皮
		$("#chosecolorid").hide();
		$("#chosesizeid").hide();
		$("#choserubbersizeid").show();	
		flagshowcolorsizerubber = 3;	
	}
	
}


////清空
function funClear(){		
	canvas_cxt.clearRect(0,0,canvas_width,canvas_height);//////清除背景	
	canvas_cxt.fillStyle = DefaultBgColor;
	canvas_cxt.fillRect(0,0,canvas_width,canvas_height);	
	var color = canvas_cxt.fillStyle;	
	var AllPathTemp = funAddListAllInfor(0,color,"","","");////添加点，性质，颜色，大小

	if(NowShowNum == ListAllPathArray.length - 1)
	{
		ListAllPathArray.push(AllPathTemp);	
	}
	else
	{
		ListAllPathArray.splice(NowShowNum + 1,0,AllPathTemp);
	}
	NowShowNum++;/////当前显示路径 + 1
}

//////撤销
function funUndo(){
	if(NowShowNum >= 0)
	{	
		NowShowNum--;/////当前显示路径 + 1
		console.log("撤销----当前显示路径ID-------" + NowShowNum);
		funDrawCanvas();/////绘制canvas
	}
	else
	{
		console.log("全部撤销--无法再次撤销");
	}
}

//////恢复
function funRedo(){
	if(NowShowNum < ListAllPathArray.length - 1)
	{	
		NowShowNum++;/////当前显示路径 + 1
		console.log("恢复----当前显示路径ID-------" + NowShowNum);
		funDrawCanvas();/////绘制canvas
	}
	else
	{
		console.log("全部恢复--无法再次恢复");
	}
}

////绘制canvas
function funDrawCanvas(){					
	canvas_cxt.clearRect(0,0,canvas_width,canvas_height);//////清除背景	
	canvas_cxt.fillStyle = DefaultBgColor;
	canvas_cxt.fillRect(0,0,canvas_width,canvas_height);
	
	for(var i = 0; i <= NowShowNum; i++ )
	{	
		if(ListAllPathArray[i].sgin == 0)////清空
		{		
			canvas_cxt.clearRect(0,0,canvas_width,canvas_height);//////清除背景	
			canvas_cxt.fillStyle = DefaultBgColor;
			canvas_cxt.fillRect(0,0,canvas_width,canvas_height);	
		}
		else if(ListAllPathArray[i].sgin == 1)////画笔
		{
			canvas_cxt.strokeStyle = ListAllPathArray[i].color;
			canvas_cxt.lineWidth = ListAllPathArray[i].size;
			var temp = ListAllPathArray[i].point;
			canvas_cxt.beginPath();				
			canvas_cxt.moveTo(temp[0].x,temp[0].y);				
			for(var j = 0; j < temp.length; j++ )
			{
				canvas_cxt.lineTo(temp[j].x,temp[j].y);
				canvas_cxt.stroke();
				if(temp.length == 2 && temp[0].x == temp[1].x && temp[0].y == temp[1].y)
				{											///起始圆点
					canvas_cxt.beginPath();
					canvas_cxt.arc(temp[0].x,temp[0].y,canvas_cxt.lineWidth * 0.1,0,2*Math.PI);
					canvas_cxt.stroke();
				}
			}
			
		}
		else if(ListAllPathArray[i].sgin == 2)////图片
		{					
			var PicUrl = ListAllPathArray[i].picurl;
			var PicSize = ListAllPathArray[i].size;
			var pointx = ListAllPathArray[i].point.x;
			var pointy = ListAllPathArray[i].point.y;					
			canvas_cxt.drawImage(PicUrl,pointx - PicSize,pointy - PicSize,PicSize * 2,PicSize * 2);			
		}	
	}
}







////////保存
function funSave(){	

	
	var initcanvasdataurl = Main_canvas.toDataURL("image.jpg");

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
		
		
		$(".backgroundmain.draw").hide();
		$(".backgroundmain.bgchose").show();
				
		funshowchosefrirnd();//////进入好友选择界面----初始化
	};

}



//////改变选择的图片
function funChangePic(s){
	SginPenOrPic = 2 ;
	PicId = ListImage[s];
}

//
//////绘制PIC
function funDrawPic(point){
	canvas_cxt.drawImage(PicId,point.x - PicSize,point.y - PicSize,PicSize * 2,PicSize * 2);			
}



///////////保存此次绘制
function funSaveThisPic(point){
	
	var AllPathTemp = funAddListAllInfor(2,"",PicSize,point,PicId);////添加点，性质，颜色，大小
	if(NowShowNum == ListAllPathArray.length - 1)
	{
		ListAllPathArray.push(AllPathTemp);	
	}
	else
	{
		ListAllPathArray.splice(NowShowNum + 1,0,AllPathTemp);
	}
	NowShowNum++;
				
}



//
//
/////////图片-
//function fundelpicsize(){	
//	PicSize -= 15;
//	funDrawCanvas();/////绘制canvas
//	funDrawPic(PicPoint);////绘制图片	
////funalert("确定添加图片");
//
//}
//
/////////图片+
//function funaddpicsize(){	
//	PicSize += 15;
//	funDrawCanvas();/////绘制canvas
//	funDrawPic(PicPoint);////绘制图片	
////funalert("确定添加图片");
//
//}
//
/////////确定添加图片
//function funSubmitPic(){	
//
//	var AllPathTemp = funAddListAllInfor(2,"",PicSize,PicPoint,PicId);////添加点，性质，颜色，大小
//	if(NowShowNum == ListAllPathArray.length - 1)
//	{
//		ListAllPathArray.push(AllPathTemp);	
//	}
//	else
//	{
//		ListAllPathArray.splice(NowShowNum + 1,0,AllPathTemp);
//	}
//	NowShowNum++;
//}
//
//



//


