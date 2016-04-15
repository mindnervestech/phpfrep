var app = angular.module('FractsApp',['ui.router','ngDialog']);
app.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    };
});

/*app.factory('UserService', function() {
	  return {
	      name : 'anonymous'
	  };
	});*/

app.config(['$stateProvider','$urlRouterProvider', function ($stateProvider,$urlRouterProvider) {

	$stateProvider.state('toState', {
		  templateUrl:'/webapp/fracts/crop_image.html',
		  controller:'cropImageController',
		  params: {
		    'imageId': '1862', 
		    'imageUrl': '/webapp/image/' 
		   
		  }
		});
	
}]);