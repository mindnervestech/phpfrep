app.controller('MainController',function($scope,$http) {
	$scope.pageSize = 20;
	$scope.currentPage = 0;
	$scope.relevanceData = [];
	var pageRange = 9;
	$scope.lastPage = 0;
	$scope.init = function() {
		$http.get('/webapp/fracts/get-all-live-relevance-list').success(function(data) {
			$scope.relevanceData = data;
			$scope.lastPage = ($scope.relevanceData.length % $scope.pageSize > 0 ? Math.ceil($scope.relevanceData.length / $scope.pageSize) : Math.ceil($scope.relevanceData.length / $scope.pageSize))-1;
			$scope.gotoPage(1);
		});
	};
	$scope.gotoLastPage = function() {
		$scope.currentPage = ($scope.relevanceData.length % $scope.pageSize > 0 ? Math.ceil($scope.relevanceData.length / $scope.pageSize) : Math.ceil($scope.relevanceData.length / $scope.pageSize))-1;
	};
	
	$scope.gotoNext = function() {
		if($scope.currentPage < $scope.lastPage) {
			$scope.currentPage++; 
		}
	};
	
	$scope.gotoPrev = function() {
		if($scope.currentPage > 0) {
			$scope.currentPage--; 
		}
	};
	
	$scope.changePageSize = function(pageSize) {
		$scope.pageSize = pageSize;
		$scope.gotoPage(1);
	};
	
	$scope.gotoPage = function(page) {
		$scope.currentPage = page-1;
		var totalRows = $scope.relevanceData.length;
		var totalPages = ($scope.relevanceData.length % $scope.pageSize > 0 ? Math.ceil($scope.relevanceData.length / $scope.pageSize) : Math.ceil($scope.relevanceData.length / $scope.pageSize))-1;
		$scope.lastPage = totalPages; 
		var pagesLength = Math.min(pageRange, totalPages);
		$scope.pages = [];
		var firstPage = Math.min(Math.max(0, $scope.currentPage - (pageRange / 2)), totalPages - pagesLength);
		for(var i = 0; i <= pagesLength; i++ ) {
			$scope.pages[i] = ++firstPage;
		}
	};
	
	$scope.openCroppedPopup = function(index) {
		index = index + ($scope.currentPage * $scope.pageSize);
		var selectedObj = $scope.relevanceData[index];
		$scope.selectedCropImageId = selectedObj.childImageId;
		$("#cropped-popup").modal();
		$("#cropped-popup #cropped-img").prop('src',"/webapp/get-child-image?id="+$scope.selectedCropImageId);
	};
	
	$scope.comparePopup = function(index, liveChildImageId) {
		index = index + ($scope.currentPage * $scope.pageSize);
		var selectedObj = $scope.relevanceData[index];
		$scope.selectedCropImageId = selectedObj.childImageId;
		$scope.selectedLiveImageId = liveChildImageId;
		$("#compare-popup").modal();
		$("#compare-popup #cropped-img").prop('src',"/webapp/get-child-image?id="+$scope.selectedCropImageId);
		$("#live-img").prop('src',"/webapp/get-child-image?id="+liveChildImageId);
	};
	
});