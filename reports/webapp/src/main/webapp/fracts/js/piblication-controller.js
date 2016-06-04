app.controller('MainController',function($scope,$http,$filter) {


	$scope.FromDate = new Date();
	$scope.month = $filter('date')( $scope.FromDate, 'M');
	$scope.day = $filter('date')($scope.FromDate, 'd'); 
	$scope.year = $filter('date')($scope.FromDate,'yyyy');
	console.log('date is ',$scope.FromDate);
	console.log('day is ',$scope.day);
	console.log('$scope.month',$scope.month);
	console.log('$scope.year',$scope.year);

	$scope.yearId;
	$scope.init = function() {
		console.log("in publication init method");
		$scope.loading=true;
		if($scope.year==2014){
			$scope.yearModel=$scope.yeardata.availableOptions[0];
		}else if($scope.year==2015){
			$scope.yearModel=$scope.yeardata.availableOptions[1];
		}else{
		$scope.yearModel=$scope.yeardata.availableOptions[2];
		}
		
		$scope.monthModel=$scope.monthdata.availableOptions[$scope.month-1];

		
		$scope.publicationList=[];
		$http.post('/webapp/gallery/get_publicatio_images/'+$scope.month+'/'+$scope.year).success(function(data) {
			console.log("in responce");
			$scope.publicationList=data;
			$scope.loading=false;
			
			if($scope.publicationList.length==0){
				$(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'No Images Found'

					});
				});
			}
			console.log('$scope.publicationList',$scope.publicationList);


		});

	};



	$scope.monthdata = {
			repeatSelect: null,
			availableOptions: [
			                   {id: '1', name: 'January'},
			                   {id: '2', name: 'February'},
			                   {id: '3', name: 'march'},
			                   {id: '4', name: 'April'},
			                   {id: '5', name: 'May'},
			                   {id: '6', name: 'jun'},
			                   {id: '7', name: 'July'},
			                   {id: '8', name: 'August'},
			                   {id: '9', name: 'September'},
			                   {id: '10', name: 'October'},
			                   {id: '11', name: 'November'},
			                   {id: '12', name: 'December'}
			                   ],
	};

	$scope.yeardata = {
			repeatSelect: null,
			availableOptions: [
			                   {id: '2014', name: '2014'},
			                   {id: '2015', name: '2015'},
			                   {id: '2016', name: '2016'},
				               {id: '2017', name: '2017'},
					           {id: '2018', name: '2018'},
					           {id: '2019', name: '2019'},
					           {id: '2020', name: '2020'}

			                   ],
	};

	$scope.dateChange=function(month,year){
		console.log('month is ',month);
		console.log('year is ',year);
		$scope.loading=true;
		
		var mnth=month.id;
		var yr=year.id;
		
		$http.post('/webapp/gallery/get_publicatio_images/'+mnth+'/'+yr).success(function(data) {
			console.log("in ng-change responce");
			$scope.publicationList=data;
			$scope.loading=false;
			
			if($scope.publicationList.length==0){
				$(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'No Images Found'

					});
				});
			}
			
			
			console.log('$scope.publicationList',$scope.publicationList);
			

		});
	};
	
	
	$scope.openChildImage=function(childid,parentId,imageName){
		console.log("in open Child image");
		$scope.childIdForDisplay=childid;
		$scope.parentidForDisplay=parentId;
		$scope.imageNameForDisplay=imageName;
		
		console.log('$scope.childIdForDisplay',$scope.childIdForDisplay);
		console.log('$scope.childIdForDisplay',$scope.parentidForDisplay);
		console.log('$scope.childIdForDisplay',$scope.imageNameForDisplay);
		console.log("in open child image");
			 $('#myModalchild').modal('show');
		
	};

	});