app.controller('MainController',function($scope,$state,$http,$filter,$window,$rootScope,ngDialog) {
	
	
	 //  $scope.imageUrl = "/webapp/get-all-parent-image?id=1862";
	//	console.log('image url',$scope.imageUrl);
		$scope.myImage='';
        $scope.myCroppedImage='';

        var handleFileSelect=function(evt) {
        var file=evt.currentTarget.files[0];
        var reader = new FileReader();
          reader.onload = function (evt) {
            $scope.$apply(function($scope){
              $scope.myImage=evt.target.result;
            	
            //	$scope.myImage=$scope.base64;
            
            });
          };
          reader.readAsDataURL(file);
        };
        angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);
		console.log('$scope.myCroppedImage',$scope.myCroppedImage);
		
	
	
	
	$scope.status = true;
	$scope.pageSize = 20;
	$scope.currentPage = 0;
	$scope.relevanceData = [];
	var pageRange = 15;
	$scope.lastPage = 0;
	$scope.showRecords = true;
	$scope.allImageList=[];
	$scope.ids = {};
	$scope.dataForComment=[];
	$scope.init=function(){
		console.log("in init method");
		
		$scope.parentList = [];
		 
		$http.get('/webapp/gallery/all_image_list').success(function(data) {
			$scope.status = true; 
			$scope.allImageList=data;
			$scope.dataForComment=data;
			
			
		//	 console.log("in all data");
		 //    console.log($scope.allImageList);
		 //    console.log('lemgth is ',$scope.allImageList.length);
		     if($scope.allImageList.length == 0 ) {
					$scope.showRecords = false;
				} else {
					$scope.lastPage = ($scope.allImageList.length % $scope.pageSize > 0 ? Math.ceil($scope.allImageList.length / $scope.pageSize) : Math.ceil($scope.allImageList.length / $scope.pageSize))-1;
					$scope.gotoPage(1);
				}
			 
		 }).error(function() {
				$scope.showRecords = false;
		});
		
		
	/*	
		 $http.get('/webapp/gallery/parentlist').success(function(data) {
			 $scope.parentList=data;
			 console.log("in all data");
			 console.log(data);
			 console.log($scope.parentList);
			 
		 });*/
		
		
		 $scope.duplicateImageList=[];
		 $http.get('/webapp/gallery/duplicateImageList').success(function(data){
			 $scope.duplicateImageList=data;
			 
			 
		 });
		 
		 $scope.publications=[];
		 $http.get('/webapp/gallery/get_publication_title').success(function(data){
			 $scope.publications=data;
			 
			 console.log('$scope.publications',$scope.publications);
			 
		 });
		 
		 
		 $scope.publicationSector=[];
		 $http.get('/webapp/gallery/get_publication_sector').success(function(data){
			 $scope.publicationSector=data;
			 console.log('$scope.publicationSector',$scope.publicationSector);
			 $scope.tempSector=[];
			 $scope.tempSector.push($scope.publicationSector);
			 console.log('$scope.tempSector',$scope.tempSector);
		 });
		 
		// $scope.simpleChainMatrix = [[0.5,0.5],[0.5,0.5]];
		 var date = new Date();
		 var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
	//	 console.log(lastDay);
		 var n1 = lastDay.getDay();
		 var n=31;
	//	 console.log(n1);
		 $scope.main=[];
		 var temp=[];
		 var k=1;
		 for(var i=1;i<=n;i++){
			 if(i<=n){
				 if(k<=8){
					temp.push(i);
					 k++;
				 }else{
					 $scope.main.push(temp);
					 temp=[];
					 temp.push(i);
					 k=2;
				 }
				if(i==n){
					$scope.main.push(temp);
				} 
			 }
			 
		 }
	//	 console.log('main.....',$scope.main);
		 
	};
	
	
	$scope.cropImagePage=function(parentImageId,imageUrl){
		console.log("in crop image page");
		$scope.imageParentId=parentImageId;
		
		$rootScope.parImageId=parentImageId;
		console.log('$rootScope.parImageId',$rootScope.parImageId);
		console.log('$scope.imageParentId',$scope.imageParentId);
		console.log('imageUrl',imageUrl);
		$state.go('toState', { 'imageId':$scope.imageParentId, 'imageUrl':imageUrl});
		
		// $window.location.href = '/webapp/fracts/crop_image.html';
		
	};
	
	
	$scope.commentVar=[];
	$scope.saveComment=function(index,pagesize,currentpage,pId,com){
		console.log("in save comment");
		
		/*$scope.number = ($scope.list + 1) + ($scope.currentPage - 1) * $scope.pageSize;*/
		console.log('pagesize',pagesize);
		console.log('currentpage is',currentpage);
		console.log('index is',index);
		$scope.number = (index) + (currentpage) * pagesize;
		
		console.log('number is ',$scope.number);
	//	console.log('$scope.allImageList',$scope.allImageList);
		
		console.log('comment is',com);
		$scope.allImageList[$scope.number].DC_SECTION_OTHER=com;
		
		/*console.log('$scope.dataForComment',$scope.dataForComment);
		$scope.commentVar=$scope.dataForComment[list];
		console.log('data is ',$scope.commentVar);
		
		console.log('$scope.commentVar',$scope.commentVar);
		
		console.log($scope.commentVar.DC_SECTION);
		var id=$scope.commentVar.DN_ID;
		*/
		$scope.json={
				 "id":pId,
				"title":com
		 };
		
		$http({url:'/webapp/gallery/save_comment',method:'POST',data:$scope.json}).success(function(data) {
			console.log("comment update successfully");
		//	$scope.init();
	        console.log('comment is',com);
			/*$scope.allImageList[list].comment=com;*/
			
			
		});		
	
		
		/*
		$http.get('/webapp/gallery/save_comment/'+id).success(function(data){
			
		});*/
		
		
	};
	
	$scope.deleteChileImage=function(id){
		console.log("in deleteChileImage");
		console.log('id is ',id);
		$http.post('/webapp/gallery/delete_child_image/'+id).success(function(data){
			$scope.init();
		});
		
		
	};
	
	
	$scope.monthOne=[[1,2,3,4,5,6],[7,8,9,10,11,12]];
	$scope.years=[[2014,2015,2016,2017,2018,2019,2020]];
	
	
	
	
	$scope.openEditImageForm=function(index,pagesize,currentpage,id,imageName){
		
		console.log("in nedit popup")
		$scope.number = (index) + (currentpage) * pagesize;
		
		$scope.universalNumber=$scope.number;
		console.log('$scope.universalNumber',$scope.universalNumber);
		console.log('$scope.number',$scope.number);
		
		$scope.imageName=imageName;
		$scope.parentImageIdforEdit=id;
		console.log($scope.parentImageIdforEdit);
		console.log("in edit image...");
		ngDialog.openConfirm({template: '/webapp/assets/templates/image_edit.html',
			className: 'ngdialog-theme-plain custom-width',
			preCloseCallback:function(){
				//$scope.init();
				console.log('call init method');
			},
			overlay: false,
			  scope: $scope //Pass the scope object if you need to access in the template
			}).then(
				function(value) {
					//save the contact form
				},
				function(value) {
					//Cancel or do nothing
				}
			);
		$scope.editImageDetail;
		$scope.imageissueMonth;
		$scope.imageissueDay;
		$scope.imageissueYear;
		$scope.pageModel;
		$scope.issueDate;
		
		$http.get('/webapp/gallery/edit_image_detail/'+id).success(function(data){
			console.log("in edit image data ajax call");
			$scope.editImageDetail=data;
			console.log('$scope.editImageDetail',$scope.editImageDetail);
			$scope.issueDate=$scope.editImageDetail.DD_ISSUE_DATE;
			console.log('$scope.issueDate',$scope.issueDate);
			$scope.pageModel=$scope.editImageDetail.DC_PAGE;
		//	$scope.pageModel="2345.jpg";
			$scope.r=new Date($scope.editImageDetail.DD_ISSUE_DATE );
			console.log('r',$scope.r);
			$scope.da1 = Date.parse($scope.editImageDetail.DD_ISSUE_DATE);
			console.log($scope.da1);
			
			/*$scope.section="Classified";*/
			$scope.section=$scope.editImageDetail.section;
			$scope.publicatioTitle=$scope.editImageDetail.titleName;
			console.log('$scope.publicatioTitle',$scope.publicatioTitle);
			
			
			$scope.month = $filter('date')($scope.da1, 'M');
			$scope.day = $filter('date')($scope.da1, 'd'); 
			$scope.year = $filter('date')($scope.da1,'yyyy');
			
			console.log('$scope.month',$scope.month);
			console.log('$scope.day',$scope.day);
			console.log('$scope.year',$scope.year);
			
		});
	};
	
	$scope.changeDay=function(day){
		console.log(day);
		$scope.day=day;
	};
	
	$scope.changeMonth=function(month){
		console.log(month);
		$scope.month=month;
	};
	
	$scope.changeYear=function(year){
		console.log(year);
		$scope.year=year;
	};
	
	$scope.changeSector=function(section){
		console.log(section);
		$scope.section=section;
		
	};
	
	$scope.changePage=function(pageModel){
		console.log(pageModel);
		$scope.pageModel=pageModel;
	};
	
	$scope.changeTitle=function(publicatioTitle){
		console.log(publicatioTitle);
		$scope.publicatioTitle=publicatioTitle;
	};
	
	$scope.saveEditImage=function(){
		
		console.log("in save Edit immage");
		console.log($scope.year);
		console.log($scope.month);
		console.log($scope.day);
		console.log('$scope.universalNumber',$scope.universalNumber);
		
		$scope.updatedIssueDate=$scope.year+"-"+$scope.month+"-"+$scope.day;
		
		console.log('$scope.updatedIssueDate',$scope.updatedIssueDate);
		
		$scope.newDate =new Date($scope.updatedIssueDate);
		
		console.log('$scope.newDate',$scope.newDate);
		
		console.log('$scope.publicatioTitle',$scope.publicatioTitle);
		console.log('$scope.pageModel',$scope.pageModel);
		$scope.allImageList[$scope.universalNumber].DC_PAGE=$scope.pageModel;
		$scope.allImageList[$scope.universalNumber].DD_ISSUE_DATE=$scope.newDate;
		$scope.allImageList[$scope.universalNumber].DC_SECTION=$scope.section;
		$scope.allImageList[$scope.universalNumber].DC_PUBLICATION_TITLE=$scope.publicatioTitle;
		
		 $scope.editImageJson={
				 "id":$scope.parentImageIdforEdit,
				"title":$scope.publicatioTitle,
				"date":$scope.updatedIssueDate,
				"page":$scope.pageModel,
				"section":$scope.section
		};
		console.log('$scope.editImageJson',$scope.editImageJson);
		
			$http({url:'/webapp/gallery/update_edited_image',method:'POST',data:$scope.editImageJson}).success(function(data) {
				console.log("update successfully");
			});		
		
	};
	
   $scope.cropImageVm=[];
   $scope.myCrop=function(){
		
		$scope.x1= document.getElementById("x1").value;
		$scope.x2= document.getElementById("x2").value;
		$scope.y1= document.getElementById("y1").value;
		$scope.y2= document.getElementById("y2").value;
		$scope.w= document.getElementById("w").value;
		$scope.h= document.getElementById("h").value;
		
		console.log('$scope.x1',$scope.x1);
		console.log('$scope.x2',$scope.x2);
		console.log('$scope.y1',$scope.y1);
		console.log('$scope.y2',$scope.y2);
		console.log('$scope.w',$scope.w);
		console.log('$scope.h',$scope.h);
		
		$scope.imagejsonData={
				id:$scope.parentImageId,
				x1:$scope.x1,
		        x2:$scope.x2,
		        y1:$scope.y1,
		        y2:$scope.y2,
		        w:$scope.w,
		        h:$scope.h
				};
	   
		
		console.log('$scope.parentImageId',$scope.parentImageId);
		
		
		$http({url:'/webapp/gallery/save_crop_image',method:'POST',data: $scope.imagejsonData,cache: false,
			contentType: "application/x-www-form-urlencoded"}).success(function(data) {
				console.log('data',data);
				$scope.cropImageVm.push(data);
				console.log('length is..',$scope.cropImageVm.length);
				console.log('$scope.cropImageVm',$scope.cropImageVm);
		});
		
		
		
	};
	
	
	$scope.wholeCrop= function(){
		console.log("in whole Crop..");
		console.log('$scope.parentImageId',$scope.parentImageId);
		var id=$scope.parentImageId;
		
		$http.get('/webapp/gallery/save_whole_crop_image/'+id).success(function(data){
			
		});
		
	};
	
	$scope.updateData=function(id){
		console.log("in update data");
		console.log(id);
		
		$http.get('/webapp/gallery/get_filter_image/'+id).success(function(data){
			$scope.allImageList=data;
		});
		
	};

	$scope.saveCropImage=function(myCroppedImage){
		
		console.log(myCroppedImage);
		
	
		
		console.log('$scope.parentImageId',$scope.parentImageId);
	
		$http({url:'/webapp/gallery/save_crop_image',method:'POST',data: {url:myCroppedImage,id:$scope.parentImageId},cache: false,
			contentType: "application/x-www-form-urlencoded"}).success(function(data) {
		});
	
	};
	
	
	
	
	$scope.cropImage=function(index,pagesize,currentpage,parentImageId,imageUrl){
		$scope.parentImageId=parentImageId;
	  
	//	$window.location.href = '/webapp/fracts/crop_image.html';
		console.log("in crop image...");
		
		
		$scope.imagenumberforcrop = (index) + (currentpage) * pagesize;
		console.log('$scope.imagenumberforcrop',$scope.imagenumberforcrop);
		
		$scope.univercropId=$scope.imagenumberforcrop;
		
		
		console.log('parent id is..',parentImageId);
		console.log('imageUrl',imageUrl);
		$scope.imageUrl=imageUrl;
		$scope.cropparentImageId=parentImageId;
		$scope.time=new Date();
		console.log('time is',$scope.time);
		/*var str =Math.floor((Math.random()*127)+956);*/
		var d = new Date();
		$scope.timeimilli=d.getMilliseconds();
		console.log('$scope.timeimilli',$scope.timeimilli);
		
		
		
		$scope.url=$scope.cropparentImageId+"&"+$scope.timeimilli;
		console.log('url is ',$scope.url);
		
		
		console.log('$scope.cropparentImageId',$scope.cropparentImageId);
	//	console.log('$scope.imageIdParen',$scope.imageIdParent);
		var id=parentImageId;
		$scope.cropImageVm=[];
		ngDialog.openConfirm({template: '/webapp/assets/templates/temp.html',
			preCloseCallback:function(){
				/*$scope.init();*/
				console.log('call init method inside temp.html');
				
				$http.get('/webapp/gallery/refresh_crop_image/'+id).success(function(data){
					/*$scope.allImageList=data;*/
					console.log('data',data);
					
					$scope.tempObject=data;
					$scope.allImageList[$scope.univercropId]=$scope.tempObject;
					console.log('$scope.allImageList[$scope.univercropId]',$scope.allImageList[$scope.univercropId]);
					
					
				});
				
				
				
			},
			className: 'ngdialog-theme-plain custom-width',
			overlay: false,
			scope: $scope //Pass the scope object if you need to access in the template
			}).then(
				function(value) {
					//save the contact form
				},
				function(value) {
					//Cancel or do nothing
				}
			);
		
		
	//	$scope.parentImageId=parentImageId;
	//	console.log("in crop parent images");
	//	console.log('id is...',$scope.parentImageId);
		
	};
	
	
	$scope.ids = {};
	$scope.moveTo=function(){
		console.log("in move to");
		/*console.log('ids..',$scope.ids);
		var values=[];
		angular.forEach($scope.ids, function(value, key) {
				console.log(key + ': ' + value);
				if(value==true){
					console.log("in true flag");
					$scope.values.push(key);
					
					values.push({
						id:key
					});
					
					
					
					selectedIds.push({
						croppedId : $scope.relevanceData[i].id,
						liveId : $scope.relevanceData[i].relevanceToBe
					});
					
				}
			});
		
		console.log('values',values);
*/
	//	var trueID=[];
		var tr=[];
		$scope.getValue=document.getElementsByClassName("chackboxclass");
		//console.log($scope.getValue);
		for(var i=0;i<$scope.getValue.length;i++){
		//console.log($scope.getValue[i].value);
			
			if($scope.getValue[i].checked==true){
			//	trueID.push($scope.getValue[i].value);
				tr.push({
					id:$scope.getValue[i].value
				});
			};
		};
		//console.log('trueID',trueID);
		console.log('tr',tr);
		
		$http({url:'/webapp/gallery/move_to_transcription',method:'POST',data:tr}).success(function(data) {
				$scope.init();
		});

	};
	
	$scope.getDuplicateImage = function(){
		 $scope.duplicateImageList=[];
		 $http.get('/webapp/gallery/duplicateImageList').success(function(data){
			 $scope.duplicateImageList=data;
			 $scope.allImageList=data;
			 $scope.status = false;
		 });
	};
	
	
	$scope.selectAll = function(){
		/*for(var i=0;i<$scope.allImageList;i++){
			$scope.allImageList[i].check = true;
		}*/
		
		$("input:checkbox[name=selectInput]").prop('checked', true);
	};
	$scope.deselectAll = function(){
		
		$("input:checkbox[name=selectInput]").prop('checked', false);
	};
	
	$scope.deleteParentImage=function(index,pagesize,currentpage,id){
		
		console.log("in delete parent images");
		console.log('index',index);
		console.log('pagesize',pagesize);
		console.log('currentpage',currentpage);
		
		$scope.imageNumber = (index) + (currentpage) * pagesize;
		
		console.log('$scope.imageNumber',$scope.imageNumber);
		
		console.log('size is ',$scope.allImageList.length);
		console.log('$scope.allImageList[$scope.imageNumber]',$scope.allImageList[$scope.imageNumber]);
		$scope.allImageList.slice($scope.imageNumber,$scope.imageNumber+1);
		$scope.resultArray=$scope.allImageList.splice($scope.imageNumber,1);
		/*console.log('$scope.resultArray',$scope.resultArray);*/
		console.log('size is ',$scope.allImageList.length);
		
//		console.log('id is...',id);
		$http.post('/webapp/gallery/delete_parent_image/'+id).success(function(data){
			/*$scope.init();*/
		});
	};
	
	$scope.gotoLastPage = function() {
		$scope.currentPage = ($scope.allImageList.length % $scope.pageSize > 0 ? Math.ceil($scope.allImageList.length / $scope.pageSize) : Math.ceil($scope.allImageList.length / $scope.pageSize))-1;
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
	$rootScope.tp=25;
	
	$scope.gotoPage = function(page) {
		$scope.currentPage = page-1;
		var totalRows = $scope.allImageList.length;
	//	console.log('totalRows',totalRows);
		var totalPages = ($scope.allImageList.length % $scope.pageSize > 0 ? Math.ceil($scope.allImageList.length / $scope.pageSize) : Math.ceil($scope.allImageList.length / $scope.pageSize))-1;
		$scope.lastPage = totalPages; 
		var pagesLength = Math.min(pageRange, totalPages);
	//	console.log("pages length",pagesLength);
		$scope.pages = [];
		var firstPage = Math.min(Math.max(0, $scope.currentPage - (pageRange / 2)), totalPages - pagesLength);
		for(var i = 0; i <= pagesLength; i++ ) {
			$scope.pages[i] = ++firstPage;
		}
	};
	
	
	/*$scope.childImages=[];
	$scope.child=function(q){
		console.log(q);
		console.log("in child init method");
		$http.get('/webapp/gallery/get_child_images/'+q).success(function(data){
			$scope.childImages=data;
			console.log($scope.childImages);
		});
	}*/
	
	
});

/*app.controller('cropImageController',function($scope,$state,$http,$filter,$window,$rootScope,ngDialog) {
	
$scope.parId=$rootScope.parImageId;
	console.log("in second controller");
	console.log('$rootScope.tp=25',$rootScope.tp);
	
	console.log('$rootScope.parImageId',$rootScope.parImageId);
	console.log('$scope.parId',$scope.parId);
	
	$scope.init=function(){
		console.log("in crop image ...in it method.");
		console.log($scope.id);
		
		console.log('$rootScope.parId',$scope.parId);
		
	};

});
*/