app.controller('ImageHistoryController',function($scope,$http,$filter) {

	$scope.loading = false;
	$scope.allImageHistory=[];
	$scope.init=function(){
		$scope.loading = true;
		console.log("in image history inint method");
		
		
		$http.get('/webapp/gallery/all_image_history').success(function(data) {
			$scope.allImageHistory=data;
			$scope.loading = false;
		//	console.log('$scope.allImageHistory',$scope.allImageHistory);
			
			
		});
	};
	
	$scope.listOfImageHistry=[];
	$scope.filterImageHistory=function(dateFrom,dateTo){
		
		$scope.loading = true;
	//	console.log("in filterImageHistory function");
	//	console.log('date from is ',dateFrom);
	//	console.log('date to is ',dateTo);
		
		
		$scope.imageHistoryJson={
				"dateFrom":dateFrom,
				"dateTo":dateTo
		};
		
		$http({url:'/webapp/gallery/get_filter_image_history',method:'POST',data:$scope.imageHistoryJson}).success(function(data) {
		
			console.log("in success");	
			$scope.listOfImageHistry=data;
			$scope.allImageHistory=data;
	//		console.log('listOfImageHistry',$scope.listOfImageHistry);
			$scope.loading = false;
			if($scope.listOfImageHistry.length==0){
				$(function(){
					new PNotify({
						
						title: 'failure Notice',
						text: 'No Images Found'

					});
				});
			}
			
			
		});
		
	};
	
});