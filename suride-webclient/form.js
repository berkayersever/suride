
  angular.module('ride_form', ['ngMaterial'])
  .controller('form_ctrl', function($scope) {
    $scope.form = {
      destination: 'Kadikoy',
      time_of_departure: 'now',
      available_seats: 1,
      description: ''
    };

    $scope.destinations = ('Kadikoy Taksim Besiktas Karakoy Umraniye Beyoglu Uskudar Eminonu Kabatas Bebek Nisantasi Cihangir').split(' ').map(function(destination) {
        return {abbrev: destination};
      });

})
