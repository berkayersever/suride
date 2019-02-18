angular.module('card', ['ngMaterial', 'ngSanitize', 'ngMessages'])

.controller('card_ctrl', function($scope, $mdDialog) {
  $scope.info = {
    time_of_departure: "08:40",
    destination: 'Kadikoy',
    available_seats: 2,
    description: "This is a ride to Kadikoy from FMAN, I need to be there at 9 so I can't delay the departure time.",
    owner: "user_name"
  };
  $scope.title = function(){
    return $scope.info.destination;
  };
  $scope.rider = function(){
    return $scope.info.owner;
  };
  $scope.get_description = function(){
    return $scope.info.description;
  };
  $scope.get_seats = function(){
    return $scope.info.available_seats;
  };
  $scope.get_time = function(){
    return $scope.info.time_of_departure;
  };

  $scope.get_rides = function(){
    $.ajax({
      type: "POST",
      url: "http://localhost:3000/postride",
      data: JSON.stringify($scope.form),
      success: function(){console.log("worked");},
      dataType: "json",
      contentType : "application/json"
    });
  }
  var maps_img = {
    img_ref : "/img/maps.png"
  };

  $scope.maps_img = maps_img;
  $scope.showAdvanced = function(ev) {
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'profile.html',
        parent: angular.element(document.body),
        targetEvent: ev,
        clickOutsideToClose:true,
        fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
      })
      .then(function(answer) {
        $scope.status = 'You said the information was "' + answer + '".';
      }, function() {
        $scope.status = 'You cancelled the dialog.';
      });
    };
})
.config(function($mdThemingProvider) {
  $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
  $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
  $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
  $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
});
