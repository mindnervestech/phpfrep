app.controller('ImageHistoryController',function($scope,$http,$filter) {

	$scope.loading = false;
	$scope.allImageHistory=[];
	
	$scope.init=function(){
		$scope.loading = true;
		console.log("in image history inint method");
		
		
		var today = new Date();
	//	console.log('date is',today);
		var dd = today.getDate();
		var mm = today.getMonth()+1; //January is 0!

		var yyyy = today.getFullYear();
		if(dd<10){
			dd='0'+dd;
		} 
		if(mm<10){
			mm='0'+mm;
		} 
		var todayTemp = mm+'/'+dd+'/'+yyyy;
	//	console.log('todayTemp',todayTemp);
		$scope.dateTo=todayTemp;
		
		var last = new Date(today.getTime() - (7 * 24 * 60 * 60 * 1000));
		var day =last.getDate();
		var month=last.getMonth()+1;
		var year=last.getFullYear();
		
		if(day<10){
			day='0'+day;
		} 
		if(month<10){
			month='0'+month;
		}
		var sevenDayBefore = month+'/'+day+'/'+year;
	//	console.log('sevenDayBefore',sevenDayBefore);
		$scope.dateFrom=sevenDayBefore;
		
		
		
		
		$scope.imageHistoryJsonInit={
				"dateFrom":$scope.dateFrom,
				"dateTo":$scope.dateTo,
		};
		
		$http({url:'/webapp/gallery/all_image_history',method:'POST',data:$scope.imageHistoryJsonInit}).success(function(data) {
			$scope.allImageHistory=data;
			$scope.loading = false;
			console.log('$scope.allImageHistory',$scope.allImageHistory);
			
		
		});
		
	
	};
	
	$scope.listOfImageHistry=[];
	$scope.filterImageHistory=function(dateFrom,dateTo){
		
		$scope.loading = true;
		console.log("in filterImageHistory function");
		console.log('date from is ',dateFrom);
		console.log('date to is ',dateTo);
		
		
		$scope.imageHistoryJson={
				"dateFrom":dateFrom,
				"dateTo":dateTo
		};
		
		$http({url:'/webapp/gallery/get_filter_image_history',method:'POST',data:$scope.imageHistoryJson}).success(function(data) {
		
			console.log("in success");	
			$scope.listOfImageHistry=data;
			$scope.allImageHistory=data;
			console.log('listOfImageHistry',$scope.listOfImageHistry);
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