app.controller('MainController',function($scope,$http,notificationService,$filter) {
	console.log("in manageUser Controller");
	
	function getParamValue(paramName) {
		console.log("In param");
		var url = window.location.search.substring(1); //get rid of "?" in querystring
		console.log("In param1",url);
		var qArray = url.split('&'); //get key-value pairs
		console.log("In param2",qArray);
		for (var i = 0; i < qArray.length; i++) {
			var pArr = qArray[i].split('='); //split key and value
			if (pArr[0] == paramName)
				return pArr[1]; //return value
		}
	};
	var param1 = getParamValue('paramI2');
	$scope.loginUserId=param1;
	console.log('login user is ',$scope.loginUserId);
	
	
		$scope.getAllUsers= function() {
			$http.get('/webapp/fracts/getAllUsers').success(function(data) {
				console.log('data is ',data);
				$scope.users=data;
			}).error(function(err) {
				console.log('err is ',err);
			});
		}
		$scope.getAllUsers();
		
		$scope.getAllRoles= function() {
			$http.get('/webapp/fracts/getAllRoles').success(function(data) {
				console.log('data is ',data);
				$scope.roles=data;
			}).error(function(err) {
				console.log('err is ',err);
			});
		}
		$scope.getAllRoles();
		
		$scope.check = {
				userIds : []
		};
		/*$scope.flag = false;
		$scope.checkAll = function(role){
			if($scope.flag == false || ($scope.flag == true && role != $scope.tempRole))
				{
				 $scope.check.userIds = [];
				$scope.tempRole = role;
				console.log("check in");
				$scope.flag = true;
					$scope.temp = [];
					 $scope.temp = $scope.users.map(function(item)
							 {
						 		$scope.arr = [];
						 		if(role == item.roleName)
						 			{
						 				$scope.arr.push(item.userId);
						 			}
						 		$scope.arr1 = [];
						 		if($scope.arr.length != 0)
						 			{	
							 			return $scope.arr[0];
							 			
						 			}else{
						 				return null;
						 			}
						 	});
					 
					 angular.forEach($scope.temp, function(value){
						 
						 if(value != null)
						 $scope.check.userIds.push(value);
					 });
					 console.log("Check all",$scope.check.userIds);
				}else if($scope.flag == true){
					console.log("check out");
					 $scope.check.userIds = [];
					 $scope.flag = false;
				}
			}*/
		
		$scope.activateUser = function()
		{
			console.log("ID",$scope.check);
			if($scope.check.userIds != 0)
				{
					$http.post('/webapp/fracts/activeUser',$scope.check.userIds).success(function(data) {
						console.log('data is ',data);
						angular.forEach(data, function(value,key){
							notificationService.success(value+'!!!');
						})	
						$scope.getAllUsers();
					}).error(function(err) {
						console.log('err is ',err);
					});
				}else{
					notificationService.error("Plese select atleast one user");
				}
		}
		
		$scope.deactivateUser = function()
		{
			console.log("DeID",$scope.check);
			if($scope.check.userIds != 0)
			{
				$http.post('/webapp/fracts/deactiveUser',$scope.check.userIds).success(function(data) {
					console.log('data is ',data);
					angular.forEach(data, function(value,key){
						notificationService.success(value+'!!!');
					})	
					$scope.getAllUsers();
				}).error(function(err) {
					console.log('err is ',err);
				});
			}else{
				notificationService.error("Plese select atleast one user");
			}
		}
		
		$scope.deleteUser = function()
		{
			console.log("DeID",$scope.check);
			
			if($scope.check.userIds != 0)
			{
				$http.post('/webapp/fracts/deleteUser',$scope.check.userIds,{params:{"loginUserId" :$scope.loginUserId }}).success(function(data) {
					console.log('data is ',data);
					$scope.getAllUsers();
					notificationService.success("User deleted successfully");
				}).error(function(err) {
					console.log('err is ',err);
				});
			}else{
				notificationService.error("Plese select atleast one user");
			}
		}
		
		$scope.addUser=function(){
			if($scope.showFields == false)
				{
					$scope.newUser.gender = "0";
					$scope.newUser.phone = "";
					$scope.newUser.address = "";
					$scope.newUser.city = "";
					$scope.newUser.state = "";
					$scope.newUser.country = "";
					$scope.newUser.zipCode = "";
				}
			console.log("new user: ",$scope.newUser);
			$scope.newUser.createdBy = $scope.loginUserId;
			$http.post('/webapp/fracts/addUser',$scope.newUser).success(function(data) {
				console.log('data is ',data);
				if(data != "")
					{
						notificationService.error(data+"!!!");
						$scope.confirmPassword = "";
						$scope.newUser.password = "";
						var message = document.getElementById('confirmMessage');
						message.innerHTML = "";
					}else{
						$scope.getAllUsers();
						$scope.confirmPassword = "";
						var message = document.getElementById('confirmMessage');
						message.innerHTML = "";
						$('#addUser').modal('hide');
						notificationService.success("New user added successfully");
					}
			}).error(function(err) {
				console.log('err is ',err);
			});
		}
		
		$scope.editUserDetails = function() {
			console.log("Edit user",$scope.editUser);
			$http.post('/webapp/fracts/updateUser',$scope.editUser).success(function(data) {
				console.log('data is ',data);
				$scope.getAllUsers();
				$('#editUser').modal('hide');
				$scope.showEditFields = false;
				notificationService.success("User updated successfully");
			}).error(function(err) {
				console.log('err is ',err);
			});
		}
		
		$scope.addUserGroup=function(){
			console.log("new user Group: ",$scope.newUserGroup);
			$http.post('/webapp/fracts/addUserGroup',$scope.newUserGroup).success(function(data) {
				console.log('data is ',data);
				$scope.getAllRoles();
				var form = document.getElementById("addUGrp");
				form.reset();
				$('#addUserGroup').modal('hide');
				notificationService.success("New user group added successfully");
			}).error(function(err) {
				console.log('err is ',err);
			});
		}
		
		$scope.edituserGroup = function(name,grpId)
		{
			console.log("role Id",name,grpId);
			$scope.updateUserGroup = {};
			$scope.updateUserGroup.id = grpId;
			$scope.updateUserGroup.userType = name;
			$('#editUserGroup').modal('show');
		}
		
		$scope.editGroup = function()
		{
			console.log("Check",$scope.updateUserGroup);
			$http.post('/webapp/fracts/updateGroup',$scope.updateUserGroup).success(function(data) {
				console.log('data is ',data);
				$scope.getAllRoles();
				$('#editUserGroup').modal('hide');
				notificationService.success("User group updated successfully");
			}).error(function(err) {
				console.log('err is ',err);
			})
		}
		
		$scope.clearForm = function(){
			var form = document.getElementById("addUGrp");
			form.reset();
			$scope.confirmPassword = "";
			var message = document.getElementById('confirmMessage');
			message.innerHTML = "";
		}
		
		$('#roleSelect').prop('disabled', true);
		$scope.openAddUser = function(roleId){
			$scope.newUser = {};
			$scope.newUser.roleId = roleId;
			$('#addUser').modal('show');
		}
		
		$scope.openEditUser = function(Id){
			console.log("User",Id);
			$http.get('/webapp/fracts/getUserDetailsById',{params:{"userId":Id}}).success(function(data) {
				console.log('data is ',data);
				$scope.editUser = {};
				$scope.editUser = data;
				$('#editUser').modal('show');
			}).error(function(err) {
				console.log('err is ',err);
			});
			
		}
		
		
		$scope.viewUserDetails = function(Id){
			console.log("User",Id);
			$http.get('/webapp/fracts/getUserDetailsById',{params:{"userId":Id}}).success(function(data) {
				console.log('data is ',data);
				$scope.viewUser = {};
				$scope.viewUser = data;
				$('#viewUser').modal('show');
			}).error(function(err) {
				console.log('err is ',err);
			});
		}
		
		$scope.showFields = false;
		$scope.showMore =function()
		{
			console.log("in show");
			$scope.showFields = true;
		}
		$scope.hideFields =function()
		{
			console.log("in hide");
			$scope.showFields = false;
		}
		
		$scope.showEditFields = false;
		$scope.showMoreEdit =function()
		{
			console.log("in show");
			$scope.showEditFields = true;
		}
		$scope.hideFieldsEdit =function()
		{
			console.log("in hide");
			$scope.showEditFields = false;
		}
		
		
		
		$scope.loading=false;
		$scope.init=function(){
			$scope.loading=true;
			$http.get('/webapp/gallery/get_all_tab_permissions').success(function(data) {
				$scope.tabPermissins=data;
				console.log("Permision",data);
				$scope.loading=false;
			}).error(function() {
				$scope.loading=false;
			});
		};
		
		$scope.changePermission=function(tab){
			console.log("Tab",tab);
			$scope.loading=true;
			var data={
					"userTypeId":tab.userTypeId,
					"tabId":tab.tabId,
					"status":tab.status
			};
			
			$http.post('/webapp/gallery/change_tab_permissions',data).success(function(data) {
				$scope.init();
			}).error(function() {
				$scope.loading=false;
			});
			
		};
		
		
		$scope.init();
		
		$scope.openpermision = function(name)
		{
			console.log("name",name);
			$("#managePermision").modal('show');
			$scope.rolePermisionName = name;
			angular.forEach($scope.tabPermissins, function(value,key){
				console.log("key",key)
				if(key == $scope.rolePermisionName)
					{
						$scope.manageRolePermision = value;
					}
			});
		}
		
});