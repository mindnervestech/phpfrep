app.controller('cropImageController',function($scope,$rootScope,$http,$filter,$location) {
	//$scope.id = getUrlParameters()["id"];
	//alert($location.search().id);
	$scope.parId=$rootScope.parImageId;
	
	console.log('$rootScope.parImageId',$rootScope.parImageId);
	console.log('$scope.parId',$scope.parId);
	
	$scope.init=function(){
		console.log("in crop image ...in it method.");
		console.log($scope.id);
		console.log('$rootScope.parId',$scope.parId);
		
	};
});