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
		
		
		
		
		$scope.loading = true;
		$scope.parentList = [];

		$http.get('/webapp/gallery/all_image_list').success(function(data) {
			$scope.loading = false;
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


		});


		$scope.publicationSector=[];
		$http.get('/webapp/gallery/get_publication_sector').success(function(data){
			$scope.publicationSector=data;
			$scope.tempSector=[];
			$scope.tempSector.push($scope.publicationSector);
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
		$state.go('toState', { 'imageId':$scope.imageParentId, 'imageUrl':imageUrl});
		// $window.location.href = '/webapp/fracts/crop_image.html';

	};

	
	$scope.closeAll = function () {
		console.log("close button");
		  ngDialog.closeAll();
		};
		
		
	$scope.commentVar=[];
	$scope.saveComment=function(index,pagesize,currentpage,pId,com){
		console.log("in save comment");
		$scope.number = parseInt(index) + parseInt(currentpage) * parseInt(pagesize);
		console.log('$scope.number',$scope.number);
		$scope.allImageList[$scope.number].DC_SECTION_OTHER=com;
		$scope.json={
				"id":pId,
				"title":com
		};

		$http({url:'/webapp/gallery/save_comment',method:'POST',data:$scope.json}).success(function(data) {
			console.log("comment update successfully");

		});		

	};

	$scope.deleteChileImage=function(index,pagesize,currentpage,parentId,childId,childIndex,imageUrl,imageName){
		console.log("in delete child image");

		console.log('parent index',index);
		console.log('pagesize',pagesize);
		console.log('currentpage',currentpage);
		console.log('parentId',parentId);
		console.log('childId',childId);
		console.log('childIndex',childIndex);
		console.log('imageUrl',imageUrl);
		console.log('imageName',imageName);
		
		var childIdTodelete =parseInt(index) +parseInt(currentpage) * parseInt(pagesize);
		console.log('childIdTodelete',childIdTodelete);
		$scope.allImageList[childIdTodelete].listVm.splice(childIndex,1);
		console.log('$scope.allImageList[childIdTodelete]',$scope.allImageList[childIdTodelete]);
		
		$http.post('/webapp/gallery/delete_child_image/'+childId+'/'+$scope.loginUserId).success(function(data){
			
		});
	};
	
	$scope.monthOne=[[1,2,3,4,5,6],[7,8,9,10,11,12]];
	$scope.years=[[2014,2015,2016,2017,2018,2019,2020]];
	$scope.openEditImageForm=function(index,pagesize,currentpage,id,imageName){
		console.log("in edit popup");
		$scope.number = (index) + (currentpage) * pagesize;
		$scope.universalNumber=$scope.number;
		$scope.imageName=imageName;
		$scope.parentImageIdforEdit=id;
		ngDialog.openConfirm({template: '/webapp/assets/templates/image_edit.html',
			className: 'ngdialog-theme-plain custom-width',
			preCloseCallback:function(){
				//$scope.init();
				console.log('call init method');
			},
			overlay: false,
			showClose: false,
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
			$scope.issueDate=$scope.editImageDetail.DD_ISSUE_DATE;
			$scope.pageModel=$scope.editImageDetail.DC_PAGE;
			$scope.r=new Date($scope.editImageDetail.DD_ISSUE_DATE );
			$scope.da1 = Date.parse($scope.editImageDetail.DD_ISSUE_DATE);
			$scope.section=$scope.editImageDetail.section;
			$scope.publicatioTitle=$scope.editImageDetail.titleName;
			$scope.month = $filter('date')($scope.da1, 'M');
			$scope.day = $filter('date')($scope.da1, 'd'); 
			$scope.year = $filter('date')($scope.da1,'yyyy');

		});
	};
	
	
	$scope.moveToTranscription=function(index,pagesize,currentpage,id){
		 $scope.loading = true;
		console.log("inmove to transcription ");
		
		console.log('index',index);
		console.log('currentpage',currentpage);
		console.log('pagesize',pagesize);
		var number = (index) + (currentpage) * pagesize;
		console.log('number ',number);
		console.log('id',id);
		$scope.allImageList.splice(number,1);
		
		$http.post('/webapp/gallery/moveToTranscription_parent/'+id).success(function(data){
			 $scope.loading = false;
		});
		
		
		
	};

	$scope.changeDay=function(day){
		$scope.day=day;
	};
	$scope.changeMonth=function(month){
		$scope.month=month;
	};
	$scope.changeYear=function(year){
		$scope.year=year;
	};
	$scope.changeSector=function(section){
		$scope.section=section;
	};
	$scope.changePage=function(pageModel){
		$scope.pageModel=pageModel;
	};
	$scope.changeTitle=function(publicatioTitle){
		$scope.publicatioTitle=publicatioTitle;
	};
	$scope.saveEditImage=function(){
	
	    $scope.loading = true;
		console.log("in save Edit immage");
		$scope.updatedIssueDate=$scope.year+"-"+$scope.month+"-"+$scope.day;
		$scope.newDate =new Date($scope.updatedIssueDate);
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
		$http({url:'/webapp/gallery/update_edited_image',method:'POST',data:$scope.editImageJson}).success(function(data) {
		    $scope.loading = false;
			console.log("update successfully");
		});		

	};

	$scope.cropImageVm=[];
	$scope.myCrop=function(){
		$scope.loading = true;
	
		$scope.x1= document.getElementById("x1").value;
		$scope.x2= document.getElementById("x2").value;
		$scope.y1= document.getElementById("y1").value;
		$scope.y2= document.getElementById("y2").value;
		$scope.w= document.getElementById("w").value;
		$scope.h= document.getElementById("h").value;
		
	
		
		$scope.imagejsonData={
				id:$scope.parentImageId,
				x1:$scope.x1,
				x2:$scope.x2,
				y1:$scope.y1,
				y2:$scope.y2,
				w:$scope.w,
				h:$scope.h,
				loginUserId:$scope.loginUserId
		};


		console.log('$scope.parentImageId',$scope.parentImageId);


		$http({url:'/webapp/gallery/save_crop_image',method:'POST',data: $scope.imagejsonData,cache: false,
			contentType: "application/x-www-form-urlencoded"}).success(function(data) {
				$scope.cropImageVm.push(data);
				$scope.loading = false;
			});
	};

	$scope.wholeCrop= function(){
		$scope.loading = true;
		$scope.wholeimagejsonData={
				id:$scope.parentImageId,
				loginUserId:$scope.loginUserId
		};

		$http({url:'/webapp/gallery/save_whole_crop_image',method:'POST',data: $scope.wholeimagejsonData,cache: false,
			contentType: "application/x-www-form-urlencoded"}).success(function(data) {
				$scope.cropImageVm.push(data);
				$scope.loading = false;
			});
		
/*		var id=$scope.parentImageId;
		$http.get('/webapp/gallery/save_whole_crop_image/'+id).success(function(data){

		});*/

	};

	$scope.updateData=function(id){
		$scope.loading = true;
		console.log('in update date');
		console.log(id);
		$http.get('/webapp/gallery/get_filter_image/'+id).success(function(data){

			$scope.allImageList=data;
			$scope.loading = false;
		});
	};

	$scope.saveCropImage=function(myCroppedImage){
		$http({url:'/webapp/gallery/save_crop_image',method:'POST',data: {url:myCroppedImage,id:$scope.parentImageId},cache: false,
			contentType: "application/x-www-form-urlencoded"}).success(function(data) {
			});

	};
	$scope.cropImage=function(index,pagesize,currentpage,parentImageId,imageUrl){
		$scope.parentImageId=parentImageId;

		//	$window.location.href = '/webapp/fracts/crop_image.html';
		$scope.imagenumberforcrop = (index) + (currentpage) * pagesize;
		$scope.univercropId=$scope.imagenumberforcrop;
		$scope.imageUrl=imageUrl;
		$scope.cropparentImageId=parentImageId;
		$scope.time=new Date();
		var d = new Date();
		$scope.timeimilli=d.getMilliseconds();
		$scope.url=$scope.cropparentImageId+"&"+$scope.timeimilli;
		var id=parentImageId;
		$scope.cropImageVm=[];
		ngDialog.openConfirm({template: '/webapp/assets/templates/temp.html',
			preCloseCallback:function(){
				$http.get('/webapp/gallery/refresh_crop_image/'+id).success(function(data){
					$scope.tempObject=data;
					$scope.allImageList[$scope.univercropId]=$scope.tempObject;
				});
			},
			className: 'ngdialog-theme-plain custom-width',
			overlay: false,
			showClose: false,
			scope: $scope //Pass the scope object if you need to access in the template
		}).then(
				function(value) {
					//save the contact form
				},
				function(value) {
					//Cancel or do nothing
				}
		);
	};
	$scope.ids = {};
	$scope.moveTo=function(){
		console.log("in move to");
		$scope.loading = true;
		$scope.resultArray=$scope.allImageList;
		var tr=[];
		$scope.getValue=document.getElementsByClassName("chackboxclass");
		var tempId=[];
		for(var i=0;i<$scope.getValue.length;i++){
			if($scope.getValue[i].checked==true){
				$scope.generatedId =parseInt( $scope.getValue[i].value) +parseInt($scope.currentPage) * parseInt( $scope.pageSize);
				tempId.push($scope.generatedId);
				tr.push({
					id:$scope.allImageList[$scope.generatedId].DN_ID
				});
			};
		};
		for(var k=0;k<tempId.length;k++){
			var m=tempId[k]-k;
			$scope.resultArray.splice(m,1);
		}
		$scope.allImageList=$scope.resultArray;
		$http({url:'/webapp/gallery/move_to_transcription',method:'POST',data:tr}).success(function(data) {
			$scope.loading = false;	

		});
	};
	
	$scope.idsadv = {};
	$scope.Advertorial=function(){
		console.log("in move to");
		$scope.loading = true;
		$scope.resultArray=$scope.allImageList;
		var tr=[];
		$scope.getValue=document.getElementsByClassName("chackboxclass");
		var tempId=[];
		for(var i=0;i<$scope.getValue.length;i++){
			if($scope.getValue[i].checked==true){
			console.log('in loop');
				$scope.generatedId =parseInt( $scope.getValue[i].value) +parseInt($scope.currentPage) * parseInt( $scope.pageSize);
				tempId.push($scope.generatedId);
				tr.push({
					id:$scope.allImageList[$scope.generatedId].DN_ID
				});
			};
		};
		for(var k=0;k<tempId.length;k++){
			var m=tempId[k]-k;
			$scope.resultArray.splice(m,1);
		}
		console.log('tr',tr);
		console.log('tempId',tempId);
		
		$scope.allImageList=$scope.resultArray;
		$http({url:'/webapp/gallery/move_to_advertorial',method:'POST',data:tr}).success(function(data) {
			$scope.loading = false;	

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
		$("input:checkbox[name=selectInput]").prop('checked', true);
	};
	$scope.deselectAll = function(){
		$("input:checkbox[name=selectInput]").prop('checked', false);
	};

	$scope.deleteParentImage=function(index,pagesize,currentpage,id){
		$scope.imageNumber = (index) + (currentpage) * pagesize;
		$scope.allImageList.slice($scope.imageNumber,$scope.imageNumber+1);
		$scope.resultArray=$scope.allImageList.splice($scope.imageNumber,1);
		$http.post('/webapp/gallery/delete_parent_image/'+id+'/'+$scope.loginUserId).success(function(data){
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
		var totalPages = ($scope.allImageList.length % $scope.pageSize > 0 ? Math.ceil($scope.allImageList.length / $scope.pageSize) : Math.ceil($scope.allImageList.length / $scope.pageSize))-1;
		$scope.lastPage = totalPages; 
		var pagesLength = Math.min(pageRange, totalPages);
		$scope.pages = [];
		var firstPage = Math.min(Math.max(0, $scope.currentPage - (pageRange / 2)), totalPages - pagesLength);
		for(var i = 0; i <= pagesLength; i++ ) {
			$scope.pages[i] = ++firstPage;
		}
	};
});

