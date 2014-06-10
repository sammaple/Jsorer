<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>微博自动化</title>

<style type="text/css">  
*{ margin:0; padding:0;}
body{margin:0}
.div_center{ margin:10px auto 0 auto; border:0px solid red; width:600px;}

        table {  
            border: 1px solid #B1CDE3;  
            padding:0;   
            margin:0 auto;  
            border-collapse: collapse;  
        }  
        
        
        td {  
            border: 1px solid #B1CDE3;  
            background: #fff;  
            font-size:12px;  
            padding: 3px 3px 3px 8px;  
            color: #4f6b72;  
        }  
</style>

<script src="js/base64.js"></script> 
<script src="js/json.js"></script>  
<script type="text/javascript">
var startindex = 0;
var limit = 50;
var key = "";

var request = false;
try {
  request = new XMLHttpRequest();
} catch (trymicrosoft) {
  try {
    request = new ActiveXObject("Msxml2.XMLHTTP");
  } catch (othermicrosoft) {
    try {
      request = new ActiveXObject("Microsoft.XMLHTTP");
    } catch (failed) {
      request = false;
    }   
  }
}

var base64 = new Base64(); 


function setWeiboCode(){

	var code = document.getElementById("setWeiboCode").value;
	if(!code){
		alert("请确保code不为空!");
		return;
	}
	var url = "/Jscorer/web_setWeiboCode?code="+code;
	{
    	 request.open("POST", encodeURI(url), true);
         request.onreadystatechange = setWeiboCodeResponese;
         request.send(null);
    }
}

function setWeiboCodeResponese(){
    if (request.readyState == 4) {
        if (request.status == 200) {
			try{
            	//var myObject = JSON.parse(request.responseText);
            	alert(request.responseText);
            	  
    		}catch(err){
    			alert(err);
    		}
        } else
          alert("设置系统微博code失败!");
      }
}

function getCurrentSystemWeiboStatus(){

	var url = "/Jscorer/web_getCurrentSystemWeiboStatus";
	{
    	 request.open("POST", encodeURI(url), true);
         request.onreadystatechange = getCurrentSystemWeiboStatusResponese;
         request.send(null);
    }
};

function getCurrentSystemWeiboStatusResponese(){
    if (request.readyState == 4) {
        if (request.status == 200) {
			try{
				
            	var myObject = JSON.parse(base64.decode(request.responseText));
            	if(myObject.status == "true"){
            		 document.getElementById('count_current_status').innerHTML = base64.decode(myObject.content);
            	}
            	  
    		}catch(err){
    			alert(err);
    		}
        } else
          alert("获取系统微博状态失败!");
      }
}

function restartWeiboService(){

	var url = "/Jscorer/restartWeiboService";
	{
    	 request.open("POST", encodeURI(url), true);
         request.onreadystatechange = restartWeiboServiceResponese;
         request.send(null);
    }
};

function restartWeiboServiceResponese(){
    if (request.readyState == 4) {
        if (request.status == 200) {
			try{
				
            	var myObject = request.responseText;
            	//alert(myObject);
            	if(myObject == "false"){
                    alert("重启微博服务失败!");
            	}else{
                    alert("设置成功，请重新获取微博服务状态查看!");
            	}
            	  
    		}catch(err){
    			alert(err);
    		}
        } else
          alert("重启微博服务失败!");
      }
}


window.onload = getCurrentSystemWeiboStatus();
</script> 


</head>
<body>
<div class="div_center">
<br/>
<br/>
<p >
<a target="_" href="https://api.weibo.com/oauth2/authorize?client_id=736045872&redirect_uri=http://www.baidu.com&response_type=code&state=&scope=">先点击网址，获取code（请不要频繁操作）</a>
</p>

<input type="text" id="setWeiboCode"/>
<input type="button" id="search" onclick="setWeiboCode()" value="设置weibo code" />

<br/>
<br/>
<br/>

</div>


<div class="div_center">
<input type="button" id="search" onclick="getCurrentSystemWeiboStatus()" value="获取当前系统微博状态" />

<p >
<a href="javascript:restartWeiboService()">有异常，尝试重新启动微博同步服务（请不要频繁操作）</a>
</p>
<br/>
<p id="count_current_status"></p>
</div>

</body>
</html>