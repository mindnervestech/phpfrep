var app = angular.module('myApp', []);
	
app.controller('controller', ['$scope','$http', function($scope,$http) {
	$scope.galleryItems;
	
    $scope.function1 = function(msg) {
    	console.log(msg + ' first function call!');  
        $http.get('/fracts_reports/rest/test').
 		success(function(data, status, headers, config) {
 			console.log("SUCCESS :::: "+data);
 			$scope.galleryItems = data;
 			// this callback will be called asynchronously
 			// when the response is available
 		}).
 		error(function(data, status, headers, config) {
 			console.log("FAILURE ::: "+data);
 			// called asynchronously if an error occurs
 			// or server returns response with an error status.
 		});
    };
    
    
    $scope.callCrop = function(imageID) {
    	console.log("imageID :::: "+imageID);
    	$http.post('/fracts_reports/rest/callCrop?imageID='+imageID).
 		success(function(data, status, headers, config) {
 			console.log("SUCCESS :::: "+data);
 			// this callback will be called asynchronously
 			// when the response is available
 		}).
 		error(function(data, status, headers, config) {
 			console.log("FAILURE ::: "+data);
 			// called asynchronously if an error occurs
 			// or server returns response with an error status.
 		});
    };
}]);