var map;

function initialize() {
    var mapOptions = {
        scaleControl: true,
        zoom: 6
    };
    map = new google.maps.Map(document.getElementById('map-canvas'),
        mapOptions);

    // Try HTML5 geolocation
    if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos = new google.maps.LatLng(position.coords.latitude,
                position.coords.longitude);

            var marker = new google.maps.Marker({
                map: map,
                position: pos
            });

            //google.maps.event.addListener(marker, 'click', function() {
            //    var infowindow = new google.maps.InfoWindow({
            //        map: map,
            //        position: pos,
            //        content: '<a href="https://www.google.ca">hello world</a>'
            //    });
            //    infowindow.open(map, marker);
            //});
            google.maps.event.addListener(map, 'click', function(e) {
                marker.setMap(null)
                marker = new google.maps.Marker({
                    position: e.latLng,
                    map: map
                });
                map.panTo(e.latLng);

                jQuery.post("http://127.0.0.1:8080/proxy/", {position: e.latLng.toString()}, window.open("https://www.google.ca"))
            });

            map.setCenter(pos);
        }, function() {
            handleNoGeolocation(true);
        });
    } else {
        // Browser doesn't support Geolocation
        handleNoGeolocation(false);
    }
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