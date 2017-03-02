app.controller('managePermissionController',function($scope,$http,$filter) {

	$scope.loading=false;
	$scope.init=function(){
		$scope.loading=true;
		$http.get('/webapp/gallery/get_all_tab_permissions').success(function(data) {
			$scope.tabPermissins=data;
			$scope.loading=false;
		}).error(function() {
			$scope.loading=false;
		});
	};
	
	$scope.changePermission=function(tab){
		$scope.loading=true;
		var data={
				"userTypeId":tab.userTypeId,
				"tabId":tab.tabId,
				"status":tab.status
		};
		
		$http.post('/webapp/gallery/change_tab_permissions',data).success(function(data) {
			$scope.init();
		}).error(function() {
			$scope.loading=false;
		});
		
	};
	
	$scope.init();
});