package com.example.function;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	public void SendMail(String userName , String lostName , String userEmail){
		String dear = "親愛的 "+userName+" 先生/女士";
		String notification = "		您申請走失的 "+lostName+" 先生/女士已獲得我們服務人員的協助，請您盡速前往服務櫃台會合。謝謝。";
		String content = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
		  		"<html xmlns=\"http://www.w3.org/1999/xhtml\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"\r\n" + 
		  		"<head style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"	<!-- If you delete this tag, the sky will fall on your head -->\r\n" + 
		  		"	<meta name=\"viewport\" content=\"width=device-width\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"\r\n" + 
		  		"	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"	<title style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">ZURBemails</title>\r\n" + 
		  		"\r\n" + 
		  		"	<link rel=\"stylesheet\" type=\"text/css\" href=\"stylesheets/email.css\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"	<style style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"		/* ------------------------------------- \r\n" + 
		  		"		GLOBAL \r\n" + 
		  		"------------------------------------- */\r\n" + 
		  		"\r\n" + 
		  		"		* {\r\n" + 
		  		"			margin: 0;\r\n" + 
		  		"			padding: 0;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		* {\r\n" + 
		  		"			font-family: \"Helvetica Neue\", \"Helvetica\", Helvetica, Arial, sans-serif;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		img {\r\n" + 
		  		"			max-width: 100%;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.collapse {\r\n" + 
		  		"			margin: 0;\r\n" + 
		  		"			padding: 0;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		body {\r\n" + 
		  		"			-webkit-font-smoothing: antialiased;\r\n" + 
		  		"			-webkit-text-size-adjust: none;\r\n" + 
		  		"			width: 100%!important;\r\n" + 
		  		"			height: 100%;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* ------------------------------------- \r\n" + 
		  		"		ELEMENTS \r\n" + 
		  		"------------------------------------- */\r\n" + 
		  		"\r\n" + 
		  		"		a {\r\n" + 
		  		"			color: #2BA6CB;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.btn {\r\n" + 
		  		"			text-decoration: none;\r\n" + 
		  		"			color: #FFF;\r\n" + 
		  		"			background-color: #666;\r\n" + 
		  		"			padding: 10px 16px;\r\n" + 
		  		"			font-weight: bold;\r\n" + 
		  		"			margin-right: 10px;\r\n" + 
		  		"			text-align: center;\r\n" + 
		  		"			cursor: pointer;\r\n" + 
		  		"			display: inline-block;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		p.callout {\r\n" + 
		  		"			padding: 15px;\r\n" + 
		  		"			background-color: #ECF8FF;\r\n" + 
		  		"			margin-bottom: 15px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.callout a {\r\n" + 
		  		"			font-weight: bold;\r\n" + 
		  		"			color: #2BA6CB;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		table.social {\r\n" + 
		  		"			/* 	padding:15px; */\r\n" + 
		  		"			background-color: #ebebeb;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.social .soc-btn {\r\n" + 
		  		"			padding: 3px 7px;\r\n" + 
		  		"			font-size: 12px;\r\n" + 
		  		"			margin-bottom: 10px;\r\n" + 
		  		"			text-decoration: none;\r\n" + 
		  		"			color: #FFF;\r\n" + 
		  		"			font-weight: bold;\r\n" + 
		  		"			display: block;\r\n" + 
		  		"			text-align: center;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		a.fb {\r\n" + 
		  		"			background-color: #3B5998!important;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		a.tw {\r\n" + 
		  		"			background-color: #1daced!important;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		a.gp {\r\n" + 
		  		"			background-color: #DB4A39!important;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		a.ms {\r\n" + 
		  		"			background-color: #000!important;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.sidebar .soc-btn {\r\n" + 
		  		"			display: block;\r\n" + 
		  		"			width: 100%;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* ------------------------------------- \r\n" + 
		  		"		HEADER \r\n" + 
		  		"------------------------------------- */\r\n" + 
		  		"\r\n" + 
		  		"		table.head-wrap {\r\n" + 
		  		"			width: 100%;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.header.container table td.logo {\r\n" + 
		  		"			padding: 15px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.header.container table td.label {\r\n" + 
		  		"			padding: 15px;\r\n" + 
		  		"			padding-left: 0px;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* ------------------------------------- \r\n" + 
		  		"		BODY \r\n" + 
		  		"------------------------------------- */\r\n" + 
		  		"\r\n" + 
		  		"		table.body-wrap {\r\n" + 
		  		"			width: 100%;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* ------------------------------------- \r\n" + 
		  		"		FOOTER \r\n" + 
		  		"------------------------------------- */\r\n" + 
		  		"\r\n" + 
		  		"		table.footer-wrap {\r\n" + 
		  		"			width: 100%;\r\n" + 
		  		"			clear: both!important;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.footer-wrap .container td.content p {\r\n" + 
		  		"			border-top: 1px solid rgb(215, 215, 215);\r\n" + 
		  		"			padding-top: 15px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.footer-wrap .container td.content p {\r\n" + 
		  		"			font-size: 10px;\r\n" + 
		  		"			font-weight: bold;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* ------------------------------------- \r\n" + 
		  		"		TYPOGRAPHY \r\n" + 
		  		"------------------------------------- */\r\n" + 
		  		"\r\n" + 
		  		"		h1,\r\n" + 
		  		"		h2,\r\n" + 
		  		"		h3,\r\n" + 
		  		"		h4,\r\n" + 
		  		"		h5,\r\n" + 
		  		"		h6 {\r\n" + 
		  		"			font-family: \"HelveticaNeue-Light\", \"Helvetica Neue Light\", \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;\r\n" + 
		  		"			line-height: 1.1;\r\n" + 
		  		"			margin-bottom: 15px;\r\n" + 
		  		"			color: #000;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		h1 small,\r\n" + 
		  		"		h2 small,\r\n" + 
		  		"		h3 small,\r\n" + 
		  		"		h4 small,\r\n" + 
		  		"		h5 small,\r\n" + 
		  		"		h6 small {\r\n" + 
		  		"			font-size: 60%;\r\n" + 
		  		"			color: #6f6f6f;\r\n" + 
		  		"			line-height: 0;\r\n" + 
		  		"			text-transform: none;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		h1 {\r\n" + 
		  		"			font-weight: 200;\r\n" + 
		  		"			font-size: 44px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		h2 {\r\n" + 
		  		"			font-weight: 200;\r\n" + 
		  		"			font-size: 37px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		h3 {\r\n" + 
		  		"			font-weight: 500;\r\n" + 
		  		"			font-size: 27px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		h4 {\r\n" + 
		  		"			font-weight: 500;\r\n" + 
		  		"			font-size: 23px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		h5 {\r\n" + 
		  		"			font-weight: 900;\r\n" + 
		  		"			font-size: 17px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		h6 {\r\n" + 
		  		"			font-weight: 900;\r\n" + 
		  		"			font-size: 14px;\r\n" + 
		  		"			text-transform: uppercase;\r\n" + 
		  		"			color: #444;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.collapse {\r\n" + 
		  		"			margin: 0!important;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		p,\r\n" + 
		  		"		ul {\r\n" + 
		  		"			margin-bottom: 10px;\r\n" + 
		  		"			font-weight: normal;\r\n" + 
		  		"			font-size: 14px;\r\n" + 
		  		"			line-height: 1.6;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		p.lead {\r\n" + 
		  		"			font-size: 17px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		p.last {\r\n" + 
		  		"			margin-bottom: 0px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		ul li {\r\n" + 
		  		"			margin-left: 5px;\r\n" + 
		  		"			list-style-position: inside;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* ------------------------------------- \r\n" + 
		  		"		SIDEBAR \r\n" + 
		  		"------------------------------------- */\r\n" + 
		  		"\r\n" + 
		  		"		ul.sidebar {\r\n" + 
		  		"			background: #ebebeb;\r\n" + 
		  		"			display: block;\r\n" + 
		  		"			list-style-type: none;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		ul.sidebar li {\r\n" + 
		  		"			display: block;\r\n" + 
		  		"			margin: 0;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		ul.sidebar li a {\r\n" + 
		  		"			text-decoration: none;\r\n" + 
		  		"			color: #666;\r\n" + 
		  		"			padding: 10px 16px;\r\n" + 
		  		"			/* 	font-weight:bold; */\r\n" + 
		  		"			margin-right: 10px;\r\n" + 
		  		"			/* 	text-align:center; */\r\n" + 
		  		"			cursor: pointer;\r\n" + 
		  		"			border-bottom: 1px solid #777777;\r\n" + 
		  		"			border-top: 1px solid #FFFFFF;\r\n" + 
		  		"			display: block;\r\n" + 
		  		"			margin: 0;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		ul.sidebar li a.last {\r\n" + 
		  		"			border-bottom-width: 0px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		ul.sidebar li a h1,\r\n" + 
		  		"		ul.sidebar li a h2,\r\n" + 
		  		"		ul.sidebar li a h3,\r\n" + 
		  		"		ul.sidebar li a h4,\r\n" + 
		  		"		ul.sidebar li a h5,\r\n" + 
		  		"		ul.sidebar li a h6,\r\n" + 
		  		"		ul.sidebar li a p {\r\n" + 
		  		"			margin-bottom: 0!important;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* --------------------------------------------------- \r\n" + 
		  		"		RESPONSIVENESS\r\n" + 
		  		"		Nuke it from orbit. It's the only way to be sure. \r\n" + 
		  		"------------------------------------------------------ */\r\n" + 
		  		"		/* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */\r\n" + 
		  		"\r\n" + 
		  		"		.container {\r\n" + 
		  		"			display: block!important;\r\n" + 
		  		"			max-width: 600px!important;\r\n" + 
		  		"			margin: 0 auto!important;\r\n" + 
		  		"			/* makes it centered */\r\n" + 
		  		"			clear: both!important;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* This should also be a block element, so that it will fill 100% of the .container */\r\n" + 
		  		"\r\n" + 
		  		"		.content {\r\n" + 
		  		"			padding: 15px;\r\n" + 
		  		"			max-width: 600px;\r\n" + 
		  		"			margin: 0 auto;\r\n" + 
		  		"			display: block;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* Let's make sure tables in the content area are 100% wide */\r\n" + 
		  		"\r\n" + 
		  		"		.content table {\r\n" + 
		  		"			width: 100%;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* Odds and ends */\r\n" + 
		  		"\r\n" + 
		  		"		.column {\r\n" + 
		  		"			width: 300px;\r\n" + 
		  		"			float: left;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.column tr td {\r\n" + 
		  		"			padding: 15px;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.column-wrap {\r\n" + 
		  		"			padding: 0!important;\r\n" + 
		  		"			margin: 0 auto;\r\n" + 
		  		"			max-width: 600px!important;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.column table {\r\n" + 
		  		"			width: 100%;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.social .column {\r\n" + 
		  		"			width: 280px;\r\n" + 
		  		"			min-width: 279px;\r\n" + 
		  		"			float: left;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* Be sure to place a .clear element after each set of columns, just to be safe */\r\n" + 
		  		"\r\n" + 
		  		"		.clear {\r\n" + 
		  		"			display: block;\r\n" + 
		  		"			clear: both;\r\n" + 
		  		"		}\r\n" + 
		  		"		/* ------------------------------------------- \r\n" + 
		  		"		PHONE\r\n" + 
		  		"		For clients that support media queries.\r\n" + 
		  		"		Nothing fancy. \r\n" + 
		  		"-------------------------------------------- */\r\n" + 
		  		"\r\n" + 
		  		"		@media only screen and (max-width: 600px) {\r\n" + 
		  		"			a[class=\"btn\"] {\r\n" + 
		  		"				display: block!important;\r\n" + 
		  		"				margin-bottom: 10px!important;\r\n" + 
		  		"				background-image: none!important;\r\n" + 
		  		"				margin-right: 0!important;\r\n" + 
		  		"			}\r\n" + 
		  		"			div[class=\"column\"] {\r\n" + 
		  		"				width: auto!important;\r\n" + 
		  		"				float: none!important;\r\n" + 
		  		"			}\r\n" + 
		  		"			table.social div[class=\"column\"] {\r\n" + 
		  		"				width: auto!important;\r\n" + 
		  		"			}\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.img {\r\n" + 
		  		"			position: relative;\r\n" + 
		  		"			height: 200px;\r\n" + 
		  		"			overflow: hidden;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.img-mask {\r\n" + 
		  		"			position: absolute;\r\n" + 
		  		"			top: 0;\r\n" + 
		  		"			left: 0;\r\n" + 
		  		"			bottom: 0;\r\n" + 
		  		"			right: 0;\r\n" + 
		  		"			background-color: rgba(0, 0, 0, 0.6);\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"		.img-text {\r\n" + 
		  		"			position: absolute;\r\n" + 
		  		"			top: 50%;\r\n" + 
		  		"			left: 50%;\r\n" + 
		  		"			transform: translate(-50%, -50%);\r\n" + 
		  		"			color: #fff;\r\n" + 
		  		"		}\r\n" + 
		  		"\r\n" + 
		  		"	</style>\r\n" + 
		  		"</head>\r\n" + 
		  		"\r\n" + 
		  		"<body bgcolor=\"#FFFFFF\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;-webkit-font-smoothing: antialiased;-webkit-text-size-adjust: none;height: 100%;width: 100%!important;\">\r\n" + 
		  		"\r\n" + 
		  		"	<!-- HEADER -->\r\n" + 
		  		"	<!-- <table class=\"head-wrap\" bgcolor=\"#999999\">\r\n" + 
		  		"	<tr>\r\n" + 
		  		"		<td></td>\r\n" + 
		  		"		<td class=\"header container\">\r\n" + 
		  		"			\r\n" + 
		  		"				<div class=\"content\">\r\n" + 
		  		"					<table bgcolor=\"#999999\">\r\n" + 
		  		"					<tr>\r\n" + 
		  		"						<td><img src=\"http://placehold.it/200x50/\" /></td>\r\n" + 
		  		"						<td align=\"right\"><h6 class=\"collapse\">Hero</h6></td>\r\n" + 
		  		"					</tr>\r\n" + 
		  		"				</table>\r\n" + 
		  		"				</div>\r\n" + 
		  		"				\r\n" + 
		  		"		</td>\r\n" + 
		  		"		<td></td>\r\n" + 
		  		"	</tr>\r\n" + 
		  		"</table> -->\r\n" + 
		  		"	<!-- /HEADER -->\r\n" + 
		  		"\r\n" + 
		  		"\r\n" + 
		  		"	<!-- BODY -->\r\n" + 
		  		"	<table class=\"body-wrap\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;width: 100%;\">\r\n" + 
		  		"		<tr style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"			<td style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\"></td>\r\n" + 
		  		"			<td class=\"container\" bgcolor=\"#FFFFFF\" style=\"margin: 0 auto!important;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;display: block!important;max-width: 600px!important;clear: both!important;\">\r\n" + 
		  		"\r\n" + 
		  		"				<div class=\"content\" style=\"margin: 0 auto;padding: 15px;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;max-width: 600px;display: block;\">\r\n" + 
		  		"					<table style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;width: 100%;\">\r\n" + 
		  		"						<tr style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"							<td style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"\r\n" + 
		  		"								<h3 style=\"margin: 0;padding: 0;font-family: &quot;HelveticaNeue-Light&quot;, &quot;Helvetica Neue Light&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Lucida Grande&quot;, sans-serif;line-height: 1.1;margin-bottom: 15px;color: #000;font-weight: 500;font-size: 27px;\">identiFace走失系統通知</h3>\r\n" + 
		  		"								<p class=\"lead\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;margin-bottom: 10px;font-weight: normal;font-size: 19px;line-height: 1.6;\">"+dear+"</p>\r\n" + 
		  		"								<p class=\"lead\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;margin-bottom: 10px;font-weight: normal;font-size: 17px;line-height: 1.6;\">"+notification+"</p>\r\n" +
		  		"\r\n" + 
		  		"								<!-- A Real Hero (and a real human being) -->\r\n" + 
		  		"								<div class=\"img\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;position: relative;height: 200px;overflow: hidden;\">\r\n" + 
		  		"									<div class=\"img-mask\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;position: absolute;top: 0;left: 0;bottom: 0;right: 0;background-color: rgba(0, 0, 0, 0.6);\"></div>\r\n" + 
		  		"									<div class=\"img-text\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;position: absolute;top: 50%;left: 50%;transform: translate(-50%, -50%);color: #fff;\">課程管理與派課系統</div>\r\n" + 
		  		"									<img src=\"https://s3-ap-northeast-1.amazonaws.com/egroupai/resources/assets/images/header.jpg\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;max-width: 100%;\">\r\n" + 
		  		"								</div>\r\n" + 
		  		"								<!-- /hero -->\r\n" + 
		  		"								<!-- <h3>Title Ipsum <small>This is a note.</small></h3>\r\n" + 
		  		"						<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>\r\n" + 
		  		"						<a class=\"btn\">Click Me!</a> -->\r\n" + 
		  		"								\r\n" + 
		  		"								<br style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"								<small style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"									photo by\r\n" + 
		  		"									<a href=\"https://unsplash.com/@grohsfabian?utm_medium=referral&amp;utm_campaign=photographer-credit&amp;utm_content=creditBadge\" target=\"_blank\" rel=\"noopener noreferrer\" title=\"Download free do whatever you want high-resolution photos from Fabian Grohs\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;color: #2BA6CB;\">\r\n" + 
		  		"										<span style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">Fabian Grohs</span>\r\n" + 
		  		"									</a>\r\n" + 
		  		"								</small>\r\n" + 
		  		"								<!-- /social & contact -->\r\n" + 
		  		"\r\n" + 
		  		"\r\n" + 
		  		"							</td>\r\n" + 
		  		"						</tr>\r\n" + 
		  		"					</table>\r\n" + 
		  		"				</div>\r\n" + 
		  		"\r\n" + 
		  		"			</td>\r\n" + 
		  		"			<td style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\"></td>\r\n" + 
		  		"		</tr>\r\n" + 
		  		"	</table>\r\n" + 
		  		"	<!-- /BODY -->\r\n" + 
		  		"\r\n" + 
		  		"	<!-- FOOTER -->\r\n" + 
		  		"	<table class=\"footer-wrap\" style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;width: 100%;clear: both!important;\">\r\n" + 
		  		"		<tr style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\">\r\n" + 
		  		"			<td style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\"></td>\r\n" + 
		  		"			<td class=\"container\" style=\"margin: 0 auto!important;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;display: block!important;max-width: 600px!important;clear: both!important;\">\r\n" + 
		  		"				<!-- content -->\r\n" + 
		  		"				<!-- <div class=\"content\">\r\n" + 
		  		"					<table>\r\n" + 
		  		"						<tr>\r\n" + 
		  		"							<td align=\"center\">\r\n" + 
		  		"								<p>\r\n" + 
		  		"									<a href=\"#\">Terms</a> |\r\n" + 
		  		"									<a href=\"#\">Privacy</a> |\r\n" + 
		  		"									<a href=\"#\"><unsubscribe>Unsubscribe</unsubscribe></a>\r\n" + 
		  		"								</p>\r\n" + 
		  		"							</td>\r\n" + 
		  		"						</tr>\r\n" + 
		  		"					</table>\r\n" + 
		  		"				</div> -->\r\n" + 
		  		"				<!-- /content -->\r\n" + 
		  		"\r\n" + 
		  		"			</td>\r\n" + 
		  		"			<td style=\"margin: 0;padding: 0;font-family: &quot;Helvetica Neue&quot;, &quot;Helvetica&quot;, Helvetica, Arial, sans-serif;\"></td>\r\n" + 
		  		"		</tr>\r\n" + 
		  		"	</table>\r\n" + 
		  		"	<!-- /FOOTER -->\r\n" + 
		  		"\r\n" + 
		  		"</body>\r\n" + 
		  		"\r\n" + 
		  		"</html>";
		String host = "smtp.gmail.com";
		int port = 587;
		final String username = "identifacesystem@gmail.com";//your account
		final String password = "Identiface000";// your password
		Properties props = new Properties();
		  props.put("mail.smtp.host", host);
		  props.put("mail.smtp.auth", "true");
		  props.put("mail.smtp.starttls.enable", "true");
		  props.put("mail.smtp.port", port);
		  Session sessionmail = Session.getInstance(props, new Authenticator() {
		   protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(username, password);
		   }
		  });
		  try {
			   Message message = new MimeMessage(sessionmail);
			   message.setFrom(new InternetAddress("identifacesystem@gmail.com"));
			   message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
			   message.setSubject("走失通知");
			   message.setContent(content, "text/html ; charset=utf-8");

			   Transport transport = sessionmail.getTransport("smtp");
			   transport.connect(host, port, username, password);

			   Transport.send(message);

			   System.out.println("寄送email結束.");
			  } catch (MessagingException e) {
			   throw new RuntimeException(e);
			  }
	}
}
