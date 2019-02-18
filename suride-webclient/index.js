angular.module('myApp', ['ngMaterial', 'ngCookies'])
.run(['$cookies', function($cookies) {
   $cookies.csrftoken;
}]);
// .controller('login_controller', ['$cookies', function($scope, $cookies){
//   var login_cookie = $cookies.get('login_info');
//   $scope.load = function(){
//     alert(login_cookie);
//   };
// alert();
// }]
// );
