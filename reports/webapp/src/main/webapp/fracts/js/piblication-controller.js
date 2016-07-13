app.controller('MainController',function($scope,$http,$filter) {


	$scope.FromDate = new Date();
	$scope.currentmonth = $filter('date')( $scope.FromDate, 'M');
	$scope.day = $filter('date')($scope.FromDate, 'd'); 
	$scope.currentYear = $filter('date')($scope.FromDate,'yyyy');

	$scope.selectedyear =$scope.currentYear;
	$scope.selectedmonth = $scope.currentmonth;
	$scope.setmonth = function(month){
		$scope.selectedmonth = month+1;
	};
	$scope.setyear = function(year){
		$scope.selectedyear = year;
	};
	$scope.searchData = function(){
		$scope.loading=true;
		$http.post('/webapp/gallery/get_publicatio_images/'+$scope.selectedmonth+'/'+$scope.selectedyear).success(function(data) {
			$scope.publicationList=data;
			
			$scope.loading=false;
			
			if($scope.publicationList.length==0){
				$(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'No Images Found'

					});
				});
			}else{
				
				$scope.main=[];
				var pub=[];
				
				var date=$scope.publicationList[0].DD_ISSUE_DATE;
				var publication=$scope.publicationList[0].DC_PUBLICATION_TITLE;
				var sub=[];
				for(var i=0;i<$scope.publicationList.length;i++){
					
					if($scope.publicationList[i].DC_IMAGENAME!=null){
						
						var thumChildImageName=$scope.publicationList[i].DC_IMAGENAME.split(".")[0]+"_thumb.jpg";
						$scope.publicationList[i].thumChildImageName=thumChildImageName;
						
					}
					if($scope.publicationList[i].parrentStatus==0 &&
							$scope.publicationList[i].jobStatus==0){
						
						$scope.publicationList[i].status="Gallery";
						$scope.publicationList[i].color="red";
						
					}else if($scope.publicationList[i].parrentStatus==2 &&
							$scope.publicationList[i].jobStatus==1){
						
						
						$scope.publicationList[i].status="Live";
						$scope.publicationList[i].color="green";
						
					}else if($scope.publicationList[i].parrentStatus==2 &&
							$scope.publicationList[i].jobStatus==0){
						
						$scope.publicationList[i].status="Transcription";
						$scope.publicationList[i].color="yellow";
						
					}else {
						
						$scope.publicationList[i].status="Advertorial";
						$scope.publicationList[i].color="blue";
						
					}
					
					
					if(publication==$scope.publicationList[i].DC_PUBLICATION_TITLE){
						sub.push($scope.publicationList[i]);
					}else{
						sub.publication=publication;
						pub.push(sub);
						sub=[];
						sub.push($scope.publicationList[i]);
						var publication=$scope.publicationList[i].DC_PUBLICATION_TITLE;
					}
					if(date!=$scope.publicationList[i].DD_ISSUE_DATE){
						pub.date=date;
						$scope.main.push(pub);
						pub=[];
						var date=$scope.publicationList[i].DD_ISSUE_DATE;
					}
					
					
					
					if(i==$scope.publicationList.length-1){
						pub.date=date;
						$scope.main.push(pub);
					}
				}
				
			}

		});
		
	};
	
	$scope.yearId;
	
	$scope.monthArray=['Jan','Feb','March','April','May','Jun','July','Aug','Sept','Oct','Nov','Dec'];
	$scope.yearArray=['2014','2015','2016','2017','2018','2019','2020'];
	
	$scope.yearOptions= [
	                   {id: '2014', name: '2014'},
	                   {id: '2015', name: '2015'},
	                   {id: '2016', name: '2016'},
		               {id: '2017', name: '2017'},
			           {id: '2018', name: '2018'},
			           {id: '2019', name: '2019'},
			           {id: '2020', name: '2020'}

	                   ];
	
	
	$scope.init = function() {
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
		$http.post('/webapp/gallery/get_publicatio_images/'+$scope.currentmonth+'/'+$scope.currentYear).success(function(data) {
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
		$scope.loading=true;
		var mnth=month.id;
		var yr=year.id;
		
		$http.post('/webapp/gallery/get_publicatio_images/'+mnth+'/'+yr).success(function(data) {
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
			

		});
	};
	
	
	$scope.openChildImage=function(parentId,imageName){
		$scope.parentidForDisplay=parentId;
		$scope.imageNameForDisplay=imageName;
		
			 $('#myModalchild').modal('show');
		
	};

	});