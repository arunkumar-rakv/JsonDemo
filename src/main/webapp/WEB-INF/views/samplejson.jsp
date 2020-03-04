<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring 4 MVC</title>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
</head>
<body>
	<center>
		<h2>Hello World</h2>
		<h2>
			${message} ${name}
		</h2>
		<input type="button" name="Check" onclick="callcontext()" value="check"/>
		<input type="button" name="download" onclick="callcontext()" value="download"/>
		<pre id="tfvars" style="display:none;">
		${tfvas}
		</pre>
	</center>
	<script>
function callcontext() {
	var ss = {'hi': '1', 'hello': '2'};
	$.ajax({
	    type : "POST",
	    url : "getjson",
	    dataType: 'json',
	    data : {
	        jsonStr : JSON.stringify(ss)
	    },
	    success: function(data){
	        console.log('data', data);
	    }
	});
}

function download(filename, text){
	var element = document.createElement('a');
	element.setAttribute('href', 'data:application/json;charset=utf-8,'+encodeURIComponent(text));
	element.setAttribute('download', filename);
	element.style.display = 'none';
	document.body.appendChild(element);
	element.click();
	document.body.removeChild(element);
}

document.getElementById("").addEventListener("click", function(){
	download("tfvars.j2", $("#tfvars").text());
}, false);

</script>
</body>
</html>