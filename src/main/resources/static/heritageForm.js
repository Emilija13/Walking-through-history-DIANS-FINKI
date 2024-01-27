$(document).ready(function() {
    //your code here
    function handleEnterKey(event) {
        if (event.key === 'Enter') {
            // Check if the focused element is the "lon" input field
            if (document.activeElement.id === 'lon' || document.activeElement.id === 'lat') {
                // Call the updateMapMarker function with the current values of lat and lon inputs
                updateMapMarker(parseFloat(document.getElementById('lat').value), parseFloat(document.getElementById('lon').value));
            }else{
                event.preventDefault();
            }

            return false;
        }
        return true;
    }


    let mapOptions = {
        center: [41.628, 21.742],
        zoom: 8,
        zoomControl: false
    }

    let map = new L.map('map1', mapOptions);
    let layer = new L.TileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
    map.addLayer(layer);

    L.control.zoom({ position: 'topright' }).addTo(map);

    // Declare marker variable outside the function
    let marker;
    let currentZoom = map.getZoom(); // Store the initial zoom level

    // Function to update the map marker based on lat and lon inputs
    function updateMapMarker(lat, lon) {
        console.log('Updating marker:', lat, lon);

        // Check if marker exists and remove it
        if (marker) {
            map.removeLayer(marker);
        }

        // Add a new marker
        marker = L.marker([lat, lon]).addTo(map);
        map.setView([lat, lon], currentZoom); // Set the map view to the marker with the current zoom

        // Update the lat and lon inputs
        document.getElementById('lat').value = lat.toFixed(7);
        document.getElementById('lon').value = lon.toFixed(7);
    }

    // Attach the updateMapMarker function to the change event of lat and lon inputs
    document.getElementById('lat').addEventListener('change', function () {
        updateMapMarker(parseFloat(this.value), parseFloat(document.getElementById('lon').value));
    });

    document.getElementById('lon').addEventListener('change', function () {
        updateMapMarker(parseFloat(document.getElementById('lat').value), parseFloat(this.value));
    });

    // Attach the updateMapMarker function to the click event of the map
    map.on('click', function (event) {
        updateMapMarker(event.latlng.lat, event.latlng.lng);
    });

    // Update the currentZoom variable when the map is zoomed
    map.on('zoomend', function () {
        currentZoom = map.getZoom();
    });

    console.log('I just ran');
});