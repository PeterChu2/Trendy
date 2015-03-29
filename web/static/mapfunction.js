var map;var pos;

function initialize() {
    var mapOptions = {
        scaleControl: true,
        streetViewControl: false,
        zoom: 6
    };
    map = new google.maps.Map(document.getElementById('map-canvas'),
        mapOptions);

    // Try HTML5 geolocation
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            pos = new google.maps.LatLng(position.coords.latitude,
                position.coords.longitude);

            var marker = new google.maps.Marker({
                map: map,
                position: pos
            });
            var infowindow = new google.maps.InfoWindow({
                    map: map,
                    position: pos,
                    content: 'Search for posts within: <input type="number" value="5" min="1" onkeypress="if(event.keyCode==13) postServer(value);"> km'
                });
                infowindow.open(map, marker);

            google.maps.event.addListener(map, 'click', function (e) {
		pos = e.latLng;
		marker.setMap(null)
                marker = new google.maps.Marker({
                    position: pos,
                    map: map
                });
                map.panTo(pos);

                infowindow = new google.maps.InfoWindow({
                    map: map,
                    position: pos,
                    content: 'Search for posts within: <input type="number" value="5" min="1" onkeypress="if(event.keyCode==13) postServer(value);"> km'
                });
                infowindow.open(map, marker);
            });

            map.setCenter(pos);
        },function () {
                handleNoGeolocation(true);
            });
    }
    else {
        // Browser doesn't support Geolocation
        handleNoGeolocation(false);
    }
}

function postServer(val){
    if(document.location.hostname == "trendy-posts.herokuapp.com" )
    {
        url = "http://trendy-posts.herokuapp.com/nearby"
    }
    else
    {
        url = "http://127.0.0.1:5000/nearby"
    }
	jQuery.post(url, {lat: pos.k, long: pos.D, value: val}, function(data){
	    //alert(data);
	    console.log(data);
	    console.log(typeof data);
            sessionStorage.setItem("data", JSON.stringify(data));
	    window.open("/nearby/trending");
	})
}

function handleNoGeolocation(errorFlag) {
    if (errorFlag) {
        var content = 'Error: The Geolocation service failed.';
    } else {
        var content = 'Error: Your browser doesn\'t support geolocation.';
    }

    var options = {
        map: map,
        position: new google.maps.LatLng(60, 105),
        content: content
    };

    var infowindow = new google.maps.InfoWindow(options);
    map.setCenter(options.position);
}

google.maps.event.addDomListener(window, 'load', initialize);
