$(document).foundation();

new WOW().init();

var app = angular.module('phonebookApp', ['ngRoute']);

/**
 * Application Controllers
 */

app.controller('homeCtrl', function ($scope) {

   $scope.header = 'Welcome! Add Contacts Now!';

 });

app.controller('addCtrl', function ($scope) {

  $scope.header = 'Add Contact';

});

app.controller('displayCtrl', function ($scope, $http) {

  $scope.header = 'View Contacts';

  $http({
      method: 'GET',
      url: '/getcontacts',
      headers: {'Content-type': 'application/json'}

  }).then(function successCallback(response) {

      $scope.contacts = response.data;

  }, function errorCallback(response) {

      throw new Error(response.responseText);

  });

  $scope.update = function(data){

    var href = $(data.target).attr('href');

    $http({
        method: 'GET',
        url: href,
        headers: {'Content-type': 'application/json'}

    }).then(function successCallback(response) {

        $scope.fullname = response.data.fullName;
        $scope.number = response.data.number;
        $scope.id = response.data.id;
        $('#update-modal').foundation('reveal','open');

    }, function errorCallback(response) {

        throw new Error(response.responseText);

    });

    data.preventDefault();
  }

  $scope.delete = function(data){

    var href = $(data.target).attr('href');

    $http({
        method: 'POST',
        url: href
    }).then(function successCallback(response) {

        window.location.reload();

    }, function errorCallback(response) {

        throw new Error(response.responseText);

    });

    data.preventDefault();

  }

});


