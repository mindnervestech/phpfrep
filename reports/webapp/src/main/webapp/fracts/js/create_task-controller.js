app.controller('MainController',function($scope,$http,$filter,$upload) {

	$scope.task={};
	
	$scope.taskList=[];
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
		
		
		
		
		$http.get('/webapp/gallery/get_all_task').success(function(data) {
			
			$scope.taskList=data;
			console.log('data is ',$scope.taskList);
			
			if($scope.taskList.length==0){

				$(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'No Task Found'

					});
				});
			}
		});
		
	};
	
	
	
	
	
	$scope.openCreateTaskPopup=function(){
		console.log("in openCreateTaskPopup function");
		 $('#myModal').modal('show');

	};
	
	
	
	$scope.updateTaskForm=function(tempTask){
		$scope.loading = true;
		console.log("in update task method");
		console.log('tempTask',tempTask);
		console.log('user iD is ',$scope.userId);
		
		$scope.UpdateJson={
				"name":tempTask.name,
				"id":$scope.userId,
				"desc":tempTask.desc,
				"status":tempTask.status
		};
		$('#editTaskModal').modal('hide');
		
		$upload.upload({
	           url: '/webapp/gallery/update_task',
	           method: 'POST',
	           file: $scope.uploadedfile,
	           data: $scope.UpdateJson
	       }).success(function(data) {
	    	   $scope.loading = false;
	    	   $scope.uploadedfile=[];
	    	   $scope.init();
				
				
	    	   $(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'Task Save Successfully'

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
		

		
	/*	$http({url:'/webapp/gallery/update_task',method:'POST',data:$scope.UpdateJson}).success(function(data) {
			$scope.init();
			$('#editTaskModal').modal('hide');
			
			$(function(){
				new PNotify({
					title: 'Success Notice',
					text: 'Task Update Successfully'

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
			
		});*/
		
		
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
	
	$scope.newTaskForm=function(task){
		
		
		console.log("in newTaskForm method");
		$scope.loading = true;
		$scope.task={};
		
		$scope.json={
				"name":task.name,
				"desc":task.desc,
				"status":task.status,
				"createdBy":$scope.loginUserId
		};
		
		$('#myModal').modal('hide');
		console.log('task is ',task);
		console.log('$scope.uploadedfile',$scope.uploadedfile);
		
		$upload.upload({
	           url: '/webapp/gallery/save_task',
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
						text: 'Task Save Successfully'

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
		
		
/*		$http({url:'/webapp/gallery/save_task',method:'POST',data:$scope.json}).success(function(data) {
			$scope.init();
			
			$(function(){
				new PNotify({
					title: 'Success Notice',
					text: 'Task Save Successfully'

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
			
		});*/
		
	};
	
	
	$scope.editTask=function(index){
		console.log("in edit task method");
		console.log('index is ',index);
		
		$scope.userId=$scope.taskList[index].id;;
		$scope.tempTask=$scope.taskList[index];
		$scope.editModel={
				"name":$scope.tempTask.name,
				"desc":$scope.tempTask.desc,
				"status":$scope.tempTask.status
		}	;	
		console.log('temp task is ',$scope.tempTask);
		$('#editTaskModal').modal('show');
		
	};
	
	$scope.searchTaskByStatus=function(searchTask){
		console.log("in searchTaskByStatus method");
		console.log('searchTask',searchTask);
		
		$http.get('/webapp/gallery/get_filter_task/'+searchTask).success(function(data){
			
			console.log("in success responce");
			$scope.taskList=data;
			
			console.log('$scope.taskList',$scope.taskList);
			if($scope.taskList.length==0){

				$(function(){
					new PNotify({
						title: 'Success Notice',
						text: 'No Task Found'

					});
				});
			}
			
			
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
		
		console.log('$scope.childIdForDisplay',$scope.childIdForDisplay);
		console.log('$scope.childIdForDisplay',$scope.parentidForDisplay);
		console.log('$scope.childIdForDisplay',$scope.imageNameForDisplay);
		console.log("in open child image");
		
			 $('#myModalchild').modal('show');
		
	};

	
	

});