<%@page import="cc.rinoux.server.weixin.util.WeixinJsSdk"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cc.rinoux.server.weixin.*"%>
<%
	String serverId = request.getParameter("serverId");////获取提交的数据
	//定义函数
	WeixinJsSdk weixinJsSdk = new WeixinJsSdk();
	String ImgUrl = weixinJsSdk.getImgUrl(serverId);
	out.println(ImgUrl);
%>