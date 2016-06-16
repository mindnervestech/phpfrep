app.controller('MainController',function($scope,$http,$filter) {

	
	$scope.FromDate = new Date();
	$scope.currentmonth = $filter('date')( $scope.FromDate, 'M');
	$scope.day = $filter('date')($scope.FromDate, 'd'); 
	$scope.currentYear = $filter('date')($scope.FromDate,'yyyy');
	console.log('date is ',$scope.FromDate);
	console.log('day is ',$scope.day);
	console.log('$scope.currentmonth',$scope.currentmonth);
	console.log('$scope.year',$scope.currentYear);

	$scope.selectedyear =$scope.currentYear;
	$scope.selectedmonth = $scope.currentmonth;
	$scope.setmonth = function(month){
		$scope.selectedmonth = month+1;
	};
	$scope.setyear = function(year){
		$scope.selectedyear = year;
	};
	$scope.searchData = function(publication){
		$scope.loading=true;
		console.log('singleSelect',publication);
		console.log("month",$scope.selectedmonth,"year",$scope.selectedyear);
		$http.post('/webapp/gallery/get_publication_gallery_images/'+$scope.selectedmonth+'/'+$scope.selectedyear+'/'+publication).success(function(data) {
			console.log("in ng-change responce");
			$scope.publicationList=data;
			$scope.ukpublication=$scope.publicationList.uk;
			$scope.uspublication=$scope.publicationList.us;
			
			console.log('$scope.publicationList.uk',$scope.publicationList.uk);
			console.log('$scope.publicationList.us',$scope.publicationList.us);
			
			$scope.loading=false;
			
			console.log('$scope.publicationList',$scope.publicationList);
			

		});
		
		
		
	};

	
	
	
	
	
	$scope.init=function(){
		console.log("in publication gallery init method");
		function getParamValue(paramName) {
			var url = window.location.search.substring(1); //get rid of "?" in querystring
			var qArray = url.split('&'); //get key-value pairs
			for (var i = 0; i < qArray.length; i++) {
				var pArr = qArray[i].split('='); //split key and value
				if (pArr[0] == paramName)
					return pArr[1]; //return value
			}
		};
		var param1 = getParamValue('param1');
		$scope.loginUserId=param1;
		console.log('login user is ',$scope.loginUserId);
		
		
	};
	
	
	$scope.monthArray=['Jan','Feb','March','April','May','Jun','July','Aug','Sept','Oct','Nov','Dec'];
	
	$scope.monthArray1=['Jan','Feb','March'];
	
	$scope.yearOptions= [
	                   {id: '2014', name: '2014'},
	                   {id: '2015', name: '2015'},
	                   {id: '2016', name: '2016'},
		               {id: '2017', name: '2017'},
			           {id: '2018', name: '2018'},
			           {id: '2019', name: '2019'},
			           {id: '2020', name: '2020'}

	                   ];
	
	
	});