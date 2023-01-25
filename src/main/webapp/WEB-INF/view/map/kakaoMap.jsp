<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>카카오 맵</title>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9829722c486e4c2d4f6d8477a49a1129&libraries=services"></script>
    
</head>
<body>

<div id="map" style="width:70%;height:700px; "></div>
<p style="text-align:center;">[주소로 위도, 경도 좌표값 얻기]</p>
<input type="text" id="address" value="" size="70"> <input type="button" value="좌표값 검색" onclick="addressChk()">
<div id="coordXY"></div>


<script>
var address = document.getElementById("address");
var mapContainer = document.getElementById("map");
var coordXY   = document.getElementById("coordXY");
var mapOption;
var map;
var x,y          = "";

if (address.value=="") {

 mapOption = {
  center: new kakao.maps.LatLng(37.567411643299266, 127.00568221118185), // 임의의 지도 중심좌표 국립중앙의료원
        level: 4            // 지도의 확대 레벨

 };
}

// 지도 생성
map = new kakao.maps.Map(mapContainer, mapOption);

function addressChk() {
 var gap = address.value; // 주소검색어
 if (gap=="") {
  alert("주소 검색어를 입력해 주십시오.");
  address.focus();
  return;
 }
 
 // 주소-좌표 변환 객체를 생성
 var geocoder = new kakao.maps.services.Geocoder();

 // 주소로 좌표를 검색
 geocoder.addressSearch(gap, function(result, status) {
  
  // 정상적으로 검색이 완료됐으면,
  if (status == kakao.maps.services.Status.OK) {
   
   var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

   y = result[0].x;
   x = result[0].y;

   // 결과값으로 받은 위치를 마커로 표시합니다.
   var marker = new kakao.maps.Marker({
    map: map,
    position: coords
   });
	
   var circle = new kakao.maps.Circle({
	    center : coords,  // 원의 중심좌표 입니다 
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
   // 인포윈도우로 장소에 대한 설명표시
   var infowindow = new kakao.maps.InfoWindow({
    content: '<div style="width:150px;text-align:center;padding:5px 0;">좌표위치</div>'
   });

   infowindow.open(map,marker);
   
   // 지도 중심을 이동
   map.setCenter(coords);

   coordXY.innerHTML = "<br>X좌표 : " + x + "<br><br>Y좌표 : " + y;
  }
 });
}

</script>
</body>
</html>