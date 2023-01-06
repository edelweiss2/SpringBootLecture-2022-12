<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>객체 탐색</title>
</head>
<body>
    <h3>객체 탐색 File upload</h3>
	<hr>
	<form action ="/detect/naver" method="post" enctype="multipart/form-data">
		<h4>Detect 파일 선택</h4>
		<input type="file" name="upload" multiple><br>
		<input type="submit" value="업로드">	
		
	</form>
</body>
</html>