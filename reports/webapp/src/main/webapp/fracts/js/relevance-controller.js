app.controller('MainController',function($scope,$http) {
	$scope.pageSize = 20;
	$scope.currentPage = 0;
	$scope.relevanceData = [];
	var pageRange = 9;
	$scope.lastPage = 0;
	$scope.showRecords = true;
	$scope.init = function() {
		$http.get('/webapp/fracts/get-relevance-list').success(function(data) {
			$scope.relevanceData = data;
			if($scope.relevanceData.length == 0 ) {
				$scope.showRecords = false;
			} else {
				$scope.lastPage = ($scope.relevanceData.length % $scope.pageSize > 0 ? Math.ceil($scope.relevanceData.length / $scope.pageSize) : Math.ceil($scope.relevanceData.length / $scope.pageSize))-1;
				$scope.gotoPage(1);
			}
		}).error(function() {
			$scope.showRecords = false;
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
	$scope.showSelects = function(last) {
		if(last) {
			$(".select-relevance").find(".select-element").show();
			$(".select-relevance").find(".bootstrap-select").hide();
		}
	};
	
	$scope.markLive = function(index) {
		index = index + ($scope.currentPage * $scope.pageSize);
		var selectedObj = $scope.relevanceData[index];
		var obj = {
			croppedId : selectedObj.id,
			liveId : selectedObj.relevanceToBe
		};
		$http({url:'/webapp/fracts/mark-live',method:'POST',data:obj}).success(function(data) {
			$scope.relevanceData.splice(index, 1);
			adjustIndexes();
		});
	};
	
	$scope.markNotDuplicate = function(index) {
		index = index + ($scope.currentPage * $scope.pageSize);
		var selectedObj = $scope.relevanceData[index];
		$http({url:'/webapp/fracts/mark-not-duplicate',method:'POST',data:{croppedId:selectedObj.id, liveId : selectedObj.relevanceToBe}}).success(function(data) {
			$scope.relevanceData.splice(index, 1);
			adjustIndexes();
		});
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
	
	$scope.markAllLive = function() {
		var index = $scope.currentPage * $scope.pageSize;
		var lastIndex = Math.min(index+$scope.pageSize, $scope.relevanceData.length);
		var indexesToRemove = [];
		var selectedIds = [];
		for(var i = lastIndex-1; i>=index; i--) {
			if($scope.relevanceData[i].isChecked) {
				indexesToRemove.push(i);
				selectedIds.push({
					croppedId : $scope.relevanceData[i].id,
					liveId : $scope.relevanceData[i].relevanceToBe
				});
			}
		}
		console.log(indexesToRemove);
		
		if(selectedIds.length == 0) {
			bootbox.alert("Please select items to set Live.");
		} else {
			bootbox.confirm("Are you sure? you want to move this "+selectedIds.length+" items to Live", function(result) {
				if(result) {
					$http({url:'/webapp/fracts/mark-all-live',method:'POST',data:selectedIds}).success(function(data) {
						for(var i in indexesToRemove) {
							$scope.relevanceData.splice(indexesToRemove[i], 1);
						}
						adjustIndexes();
					});
				}
			});
		}
	};
	$scope.markAllNotDuplicate = function(index) { 
		var index = $scope.currentPage * $scope.pageSize;
		var lastIndex = Math.min(index+$scope.pageSize, $scope.relevanceData.length);
		var indexesToRemove = [];
		var selectedIds = [];
		for(var i = lastIndex-1; i>=index; i--) {
			if($scope.relevanceData[i].isChecked) {
				indexesToRemove.push(i);
				selectedIds.push({
					croppedId : $scope.relevanceData[i].id,
					liveId : $scope.relevanceData[i].relevanceToBe
				});
			}
		}
		if(selectedIds.length == 0) {
			bootbox.alert("Please select items to set Not Duplicated.");
		} else {
			bootbox.confirm("Are you sure? you want to move this "+selectedIds.length+" items to Not Duplicated", function(result) {
				if(result) {
					$http({url:'/webapp/fracts/mark-all-not-duplicate',method:'POST',data:selectedIds}).success(function(data) {
						for(var i in indexesToRemove) {
							$scope.relevanceData.splice(indexesToRemove[i], 1);
						}
						adjustIndexes();
					});
				}
			});
		}
	};
	function adjustIndexes() {
		var last = ($scope.relevanceData.length % $scope.pageSize > 0 ? Math.ceil($scope.relevanceData.length / $scope.pageSize) : Math.ceil($scope.relevanceData.length / $scope.pageSize))-1;
		if(last < $scope.currentPage) {
			$scope.currentPage = last;
		}
		if(last < $scope.lastPage) {
			$scope.lastPage = last;
		}
	}
});