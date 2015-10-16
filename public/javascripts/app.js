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

app.controller('bankCtrl', function ($scope, $http) {

  $('#add-bank-div').toggle();
  $scope.header = 'Add Banks';

  $scope.addbank = function(){
     $('#add-bank-div').slideToggle();
  }

  $http({
      method: 'GET',
      url: '/getuserbanks',
      headers: {'Content-type': 'application/json'}

  }).then(function successCallback(response) {

      $scope.userbanks = response.data;

  }, function errorCallback(response) {

      throw new Error(response.responseText);

  });

  $http({
      method: 'GET',
      url: '/getallusersbanks',
      headers: {'Content-type': 'application/json'}

  }).then(function successCallback(response) {

      $scope.alluserbanks = response.data;

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

        $scope.id = response.data.id;
        $scope.bank_name = response.data.bank_name;
        $scope.branch = response.data.branch;
        $scope.acct_type = response.data.acct_type;
        $scope.acct_number = response.data.acct_number;
        $scope.acct_owner = response.data.acct_owner;
        $scope.status = response.data.status;

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


