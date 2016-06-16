app.controller('MainController',function($scope,$http,$filter,$upload,ngDialog) {

	$scope.task={};
	
	$scope.faqList=[];
	$scope.init=function(){
		console.log("in init method");
		$scope.searchTask='All';
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
		
		
		
		
		$http.get('/webapp/gallery/get_all_faq').success(function(data) {
			
			$scope.faqList=data;
			console.log('data is ',$scope.faqList);
			
			if($scope.faqList.length==0){

				$(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'No FAQ Found'

					});
				});
			}
		});
		
		
		
	};
	
	
	
	
	
	$scope.openCreatefaqPopup=function(){
		console.log("in openCreateTaskPopup function");
		 $('#myModal').modal('show');

	};
	

	$scope.uploadedfile=[];
	$scope.uploadFile = function($files){
		console.log("in $upload function");
		console.log($files);
		$files.forEach(function(value){
			console.log("value===");
			console.log(value);
			$scope.uploadedfile.push(value);
		});		 
	};
	
	$scope.newfaqForm=function(task){
		
		
		console.log("in newTaskForm method");
		$scope.loading = true;
		$scope.task={};
		
		$scope.json={
				"Operation":task.Operation,
				"desc":task.desc,
				"data":task.dataField,
				"createdBy":$scope.loginUserId,
				"alert":task.alert
		};
		
		$('#myModal').modal('hide');
		console.log('task is ',task);
		console.log('$scope.uploadedfile',$scope.uploadedfile);
		
		$upload.upload({
	           url: '/webapp/gallery/save_faq',
	           method: 'POST',
	           file: $scope.uploadedfile,
	           data: $scope.json
	       }).success(function(data) {
	    	   $scope.loading = false;
	    	   $scope.init();
	    	   $scope.uploadedfile=[];
				$(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'FAQ Save Successfully'

					});
				});
	    	   
	       }).error(function() {
				$scope.loading = false;
				$(function(){
					new PNotify({
						
						title: 'failure Notice',
						text: 'Failed'

					});
				});
				
			});
	};
	
	
	$scope.editfaq=function(index){
		console.log("in edit task method");
		console.log('index is ',index);
		/*
		$scope.faqList*/
		$scope.userId=$scope.faqList[index].id;;
		$scope.tempTask=$scope.faqList[index];
		
		$scope.editModel={
				"operation":$scope.tempTask.operation,
				"desc":$scope.tempTask.desc,
				"dataField":$scope.tempTask.dataField,
				"alert":$scope.tempTask.alert,
		};
		
		console.log('temp task is ',$scope.tempTask);
		$('#editTaskModal').modal('show');
		
	};
	
	
	
	$scope.updatefaqForm=function(task){
		
		console.log("in update task method");
		console.log('tempTask',task);
		console.log('user iD is ',$scope.userId);
		
	/*	$scope.UpdateJson={
				"name":tempTask.name,
				"id":$scope.userId,
				"desc":tempTask.desc,
				"status":tempTask.status
		};*/
		
		
		
		$scope.UpdateJson={
				"Operation":task.operation,
				"desc":task.desc,
				"data":task.dataField,
				"alert":task.alert,
				"id":$scope.userId,
				
		};
		
		$upload.upload({
	           url: '/webapp/gallery/update_faq',
	           method: 'POST',
	           file: $scope.uploadedfile,
	           data: $scope.UpdateJson
	       }).success(function(data) {
	    	   $scope.loading = false;
	    	   $scope.uploadedfile=[];
	    	   $scope.init();
				$('#editTaskModal').modal('hide');
				
	    	   $(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'FAQ Save Successfully'

					});
				});
	    	   
	       }).error(function() {
				$scope.loading = false;
				$(function(){
					new PNotify({
						
						title: 'failure Notice',
						text: 'Failed'

					});
				});
				
			});
	};
	
	
	$scope.openImagePopup=function(childid,parentId,imageName){
		console.log("in open Child image");
		
		console.log('childid',childid);
		console.log('parent',parentId);
		console.log('imageName',imageName);
		
		$scope.childIdForDisplay=childid;
		$scope.parentidForDisplay=parentId;
		$scope.imageNameForDisplay=imageName;
		
			 $('#myModalchild').modal('show');
		
	};

	

});