var app = angular.module('app', ['ngMaterial', 'ngSanitize', 'ngMessages'])


app.controller('card_ctrl', function($scope, $rootScope, $mdDialog,  $mdSidenav) {
  $scope.showDetails = function(rideid){
    window.location.href = "http://localhost:3000/details?id="+rideid;
  };



  $scope.info = {
    time: "08:40",
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

  $.get( "http://localhost:3000/getrides", function( data ) {
    $scope.rides = data;
    $scope.rides.forEach(function(r){if(r.desc && r.desc.length > 115){r.desc=r.desc.substring(0,115)+'...';}});
    console.log($scope.rides);
  });

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

    //search ride ridefilter
    $scope.$on('search', function(events, args){
      console.log(args);
      $scope.search = args; //now we've registered!
    });
});
app.controller('profile_ctrl', function($scope, $rootScope){
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
      $scope.user_info = {user_name:data.name,rating:data.rating,ride_count:data.rides,id:data._id};
      $rootScope.$broadcast('userinfo',$scope.user_info);
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

});

app.controller('toolbar_ctrl', function($scope,$rootScope, $mdSidenav){
  $scope.toggleLeft = function(){
          $mdSidenav('left').toggle();
        };

        $scope.$on('userinfo', function(events, args){
          console.log(args);
          $scope.user_info = args; //now we've registered!
        });

        $scope.myrides = false;
          $scope.toggleMyRides = function(){
        
          if($scope.myrides==true){$scope.search = $scope.user_info._id;$rootScope.$broadcast('search', $scope.search)}
          else{$scope.search = "";$rootScope.$broadcast('search', $scope.search)}
  };
       $scope.search = null;
       $scope.showPreSearchBar = function() {
         return $scope.searchOn == null;
       };
       $scope.initiateSearch = function() {
         $scope.searchOn = '';
          $scope.search = '';
       };
       $scope.showSearchBar = function() {
         return $scope.searchOn != null
       };
       $scope.endSearch = function() {
         return $scope.searchOn = null;
       };
       $scope.submit = function() {
         $rootScope.$broadcast('search', $scope.search)
       }

       // to focus on input element after it appears
       $scope.$watch(function() {
         return document.querySelector('#search-bar:not(.ng-hide)');
       }, function(){
           document.getElementById('search-input').focus();
       });
});
// .config(function($mdThemingProvider) {
//   $mdThemingProvider.theme('dark-grey').backgroundPalette('grey').dark();
//   $mdThemingProvider.theme('dark-orange').backgroundPalette('orange').dark();
//   $mdThemingProvider.theme('dark-purple').backgroundPalette('deep-purple').dark();
//   $mdThemingProvider.theme('dark-blue').backgroundPalette('blue').dark();
// });
