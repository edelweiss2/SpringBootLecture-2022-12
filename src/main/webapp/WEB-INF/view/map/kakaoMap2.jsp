<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>카카오 맵</title>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9829722c486e4c2d4f6d8477a49a1129&libraries=services"></script>
    <style></style>
</head>
<body>

<div id="map" style="width:30%;height:350px;"></div>

<script>
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	mapOption = { 
	    center: new kakao.maps.LatLng(37.567411643299266, 127.00568221118185), // 지도의 기본좌표
	    level: 8 // 지도의 확대 레벨 
	}; 
	var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	
	
	
	if (navigator.geolocation) {
	    
	    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
	    navigator.geolocation.getCurrentPosition(function(position) {
	        
	        var lat = position.coords.latitude, // 위도
	            lon = position.coords.longitude; // 경도
	        
	        var locPosition = new kakao.maps.LatLng(lat, lon), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
	            message = '<div style="padding:5px;">현 위치</div>'; // 인포윈도우에 표시될 내용입니다
	            
            var circle = new kakao.maps.Circle({
        	    center : new kakao.maps.LatLng(lat, lon),  // 원의 중심좌표 입니다 
        	    radius: 1500, // 미터 단위의 원의 반지름입니다 
        	    strokeWeight: 1, // 선의 두께입니다 
        	    strokeColor: '#00a0e9', // 선의 색깔입니다
        	    strokeOpacity: 1, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        	    strokeStyle: 'solid', // 선의 스타일 입니다
        	    fillColor: '#00a0e9', // 채우기 색깔입니다
        	    fillOpacity: 0.1  // 채우기 불투명도 입니다   
        	}); 

        	// 지도에 원을 표시합니다 
        	circle.setMap(map);     
	        
	        // 마커와 인포윈도우를 표시합니다
	        displayMarker(locPosition, message);
	            
	      });
	    
	} else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
	    
	    var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),    
	        message = 'geolocation을 사용할수 없어요..'
	        
	    displayMarker(locPosition, message);
	}
	
	// 지도에 마커와 인포윈도우를 표시하는 함수입니다
	function displayMarker(locPosition, message) {

	    // 마커를 생성합니다
	    var marker = new kakao.maps.Marker({  
	        map: map, 
	        position: locPosition
	    }); 
	    
	    var iwContent = message, // 인포윈도우에 표시할 내용
	        iwRemoveable = true;

	    // 인포윈도우를 생성합니다
	    var infowindow = new kakao.maps.InfoWindow({
	        content : iwContent,
	        removable : iwRemoveable
	    });
	    
	    // 인포윈도우를 마커위에 표시합니다 
	    infowindow.open(map, marker);
	    
	    // 지도 중심좌표를 접속위치로 변경합니다
	    map.setCenter(locPosition);      
	}    
</script>
</body>
</html>