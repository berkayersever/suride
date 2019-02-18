angular.module('profile', ['ngMaterial'])

.controller('profile_ctrl', function($scope){
  $scope.user_info = {
    user_name: 'User Name',
    rating: 2,
    ride_count: 10,
  };
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
