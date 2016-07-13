app.controller('ImageHistoryController',function($scope,$http,$filter) {

	$scope.loading = false;
	$scope.allImageHistory=[];
	
	$scope.init=function(){
		$scope.loading = true;
		
		var today = new Date();
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
		$scope.dateFrom=sevenDayBefore;
		$scope.imageHistoryJsonInit={
				"dateFrom":$scope.dateFrom,
				"dateTo":$scope.dateTo,
		};
		
		$http({url:'/webapp/gallery/all_image_history',method:'POST',data:$scope.imageHistoryJsonInit}).success(function(data) {
			$scope.allImageHistory=data;
			$scope.loading = false;
			var date =$scope.allImageHistory[0].date;
			$scope.main=[];
			var sub=[];
			for(var i=0;i<$scope.allImageHistory.length;i++){
				
				$scope.allImageHistory[i].id=i;
				if($scope.allImageHistory[i].date==date){
					sub.push($scope.allImageHistory[i]);
				}else{
					$scope.main.push(sub);
					sub=[];
					sub.push($scope.allImageHistory[i]);
					date=$scope.allImageHistory[i].date;
				}
				
				if(i==$scope.allImageHistory.length-1){
					$scope.main.push(sub);
				}
			}
		
			
		});
		
	
	};
	
	$scope.listOfImageHistry=[];
	$scope.filterImageHistory=function(dateFrom,dateTo){
		
		$scope.loading = true;
		
		$scope.imageHistoryJson={
				"dateFrom":dateFrom,
				"dateTo":dateTo
		};
		
		$http({url:'/webapp/gallery/get_filter_image_history',method:'POST',data:$scope.imageHistoryJson}).success(function(data) {
		
			$scope.listOfImageHistry=data;
			$scope.allImageHistory=data;
			$scope.loading = false;
			if($scope.listOfImageHistry.length==0){
				$(function(){
					new PNotify({
						
						title: 'failure Notice',
						text: 'No Images Found'

					});
				});
			}else{
				
				var date =$scope.allImageHistory[0].date;
				$scope.main=[];
				var sub=[];
				for(var i=0;i<$scope.allImageHistory.length;i++){
					
					
					$scope.allImageHistory[i].id=i;
					
					if($scope.allImageHistory[i].date==date){
						sub.push($scope.allImageHistory[i]);
					}else{
						$scope.main.push(sub);
						sub=[];
						sub.push($scope.allImageHistory[i]);
						date=$scope.allImageHistory[i].date;
					}
					
					if(i==$scope.allImageHistory.length-1){
						$scope.main.push(sub);
					}
				}
			
				
			}
			
		});
		
	};
	
});