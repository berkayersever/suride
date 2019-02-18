// In the following example, markers appear when the user clicks on the map.
      // The markers are stored in an array.
      // The user can then click an option to hide, show or delete the markers.
      var map;
      var markers = [];
      var chosenLoc = {lat: 40.9811277, long: 29.0280337};
      function initMap() {
        var Kadikoy = {lat: 40.9811277, lng: 29.0280337};

        map = new google.maps.Map(document.getElementById('map'), {
          zoom: 14,
          center: Kadikoy,
          mapTypeId: 'terrain'
        });


        // This event listener will call addMarker() when the map is clicked.
        map.addListener('click', function(event) {
          deleteMarkers();
          addMarker(event.latLng);
          chosenLoc = {lat: event.latLng.lat(),long:event.latLng.lng()};
        });

        map.addListener('rightclick', function(event){
          deleteMarkers();
        });

        // Adds a marker at the center of the map.
        // addMarker(haightAshbury);
      }

      //function to center map according to dropdown of destination
      function geocodeAddress(resultsMap, address) {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address': address}, function(results, status) {
          if (status === 'OK') {
            resultsMap.setCenter(results[0].geometry.location);
            chosenLoc = {lat: results[0].geometry.location.lat(),long: results[0].geometry.location.lng()};
          } else {
            alert('Geocode was not successful for the following reason: ' + status);
          }
        });
      }

      // Adds a marker to the map and push to the array.
      function addMarker(location) {
        var marker = new google.maps.Marker({
          position: location,
          map: map
        });
        markers.push(marker);
      }

      // Sets the map on all markers in the array.
      function setMapOnAll(map) {
        for (var i = 0; i < markers.length; i++) {
          markers[i].setMap(map);
        }
      }

      // Removes the markers from the map, but keeps them in the array.
      function clearMarkers() {
        setMapOnAll(null);
      }

      // Shows any markers currently in the array.
      function showMarkers() {
        setMapOnAll(map);
      }

      // Deletes all markers in the array by removing references to them.
      function deleteMarkers() {
        clearMarkers();
        markers = [];
      }

  angular.module('ride_form', ['ngMaterial'])
  .controller('toolbar_ctrl', function($scope,$rootScope, $mdSidenav){
  $scope.toggleLeft = function(){
          $mdSidenav('left').toggle();
        };
      })
  .controller('profile_ctrl', function($scope){
        $scope.logout = function() {
          $.ajax({
            type: "POST",
            url: "http://localhost:3000/logout",
            headers: {
        'X-Requested-With': 'XMLHttpRequest'
      },contentType: 'application/json',
      xhrFields: {withCredentials: true},

            success: function(data){
              if(data=="logout") {window.location.replace("http://localhost:3000/");}
              else{alert("Error logging out!");}
            }
          });

        };

          $.get("http://localhost:3000/userinfo",function(data){
            console.log(data);
            $scope.user_info = {user_name:data.name,rating:data.rating,ride_count:data.rides};
          });

        $scope.get_user_name = function(){
          return $scope.user_info.user_name;
        };
        $scope.get_rating = function(){
          return $scope.user_info.rating;
        };
        $scope.get_ride_count = function(){
          return $scope.user_info.ride_count;
        };

      })
  .controller('form_ctrl', function($scope) {
    $scope.form = {
      dest: 'Kadikoy',
      deptime: '12:40',
      seats: 1,
      desc: ''
    };

    $scope.destinations = ('Kadikoy Taksim Besiktas Karakoy Umraniye Uskudar Eminonu Nisantasi Cihangir').split(' ').map(function(dest) {
        return {abbrev: dest};
      });

    $scope.destSelect = function(i){
      geocodeAddress(map,i);
    };
    $scope.post_form = function(){
      //form validation before posting
      if($scope.form.deptime.indexOf(':') != 2)
      {
          alert("Time should be of format hh:mm!");
          return;
      }
      else if($scope.form.deptime.length != 5)
      {
          alert("Time should be of format hh:mm!");
          return;
      }
      else if(parseInt($scope.form.deptime.substring(0,2))>24||parseInt($scope.form.deptime.substring(0,2))<0)
      {
          alert("Time should be of format hh:mm!");
          return;
      }
      else if(parseInt($scope.form.deptime.substring(3))>59||parseInt($scope.form.deptime.substring(3))<0)
      {
          alert("Time should be of format hh:mm!");
          return;
      }
      if(!$scope.form.seats)
      {
        alert("Number of seats should be 1 and 4!");
        return;
      }
      $scope.form.loc = chosenLoc;
      console.log($scope.form);
      $.ajax({
        type: "POST",
        url: "http://localhost:3000/postride",
        data: JSON.stringify($scope.form),
        success: function(){console.log("worked");window.location.replace("http://localhost:3000/home");},
        error : function(){console.log("error");},
        contentType : "application/json"
      });

    };
    $scope.check_time_format = function(){
      if($scope.deptime.indexof(':') != 2)
      {
          return false;
      }
    }
});
