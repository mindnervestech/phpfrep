	var App = angular.module('datatableApp', ['schemaForm','mgcrea.ngStrap',
	                                          'schemaForm-datepicker', 'schemaForm-timepicker', 'schemaForm-datetimepicker'/*'datatables','datatables.bootstrap'*/]);

	App.controller('datatableCtrl', function ($scope, $http) {
		$.extend({
			getUrlVars:function() {
				var vars = [],hash;
				var hashes = window.location.href.slice(window.location.href.indexOf('?')+1).split("#")[0].split("&");
				for(var i = 0; i<hashes.length;i++) {
					hash = hashes[i].split("=");
					vars.push(hash[0]);
					vars[hash[0]] = hash[1];
				}
				return vars;
			},
			getUrlVar:function(name){
				return $.getUrlVars()[name];
			}
		});
		$scope.finishedHeader = false;
		$scope.dtColumns = [];
		$scope.detailColumns = [];
		$scope.expanded = false;
		$scope.isSavedTemplate = false;
		$scope.isSavedTemplateTable = false;
		$scope.isReportSaved = false;
		$scope.backBtn = false;
		$scope.subscriberId = $.getUrlVar('subscriberId');
		$scope.userId = $.getUrlVar('userId');
		function registerEvent() {
			$("#rpt_table").on("click",".enlarge-img",function(e) {
				$scope.showLargeImage($(e.currentTarget).attr("src"));
			});
			
			$('#rpt_table').on('click'," tbody a",function(e){
				aPos = window.oTable.fnGetPosition($(this).parents('td')[0]);
				rowData = window.oTable.fnGetData(aPos[0]);
				var link = window.oTable.DataTable.settings[0].aoColumns[aPos[1]].link;
				linkUrl = Mustache.render(link.url, rowData);
				if(link.target == '_blank') {
					
				}
				
				if(link.target == '_modal') {
					
				}
				
				// This is important
				$scope.$apply(function(){
					
				});
				
				
			});
			
		}
		
		registerEvent();
		/*$http.get('/template/reports/file/report1.json').success(function(data){
			$scope.dtColumns = data.columns;
			
			
		});
		
		$http.get('/template/reports/file/report1_data.json').success(function(data){
			$scope.reportData = data.data;
			
			
		});*/
		
		var nrecoPivotExt = new NRecoPivotTableExtensions({
			drillDownHandler: function(dataFiler) {
				var lastQueryCriteria;
				if($scope.lastQueryExecuted==1) {
					lastQueryCriteria = $scope.reportTemplate.model;
				} else {
					lastQueryCriteria = $scope.searchConfig;
				}
				$("#loading").show();
				$http({method:'post',url:'/webapp/reports/drildownreport',data:{searchCriteria:lastQueryCriteria,filters:dataFiler}}).success(function(data) {
					$scope.detailsData = data.data;
					$scope.detailColumns = data.columns;
					$("a[href='#detail-view']").click();
					$("#loading").hide();
				});
			}
		});
		
		$scope.reportMDs = [];
		
		$scope.loadReportsMd = function () {
			$("#loading").show();
			$http.get('/webapp/reports/md',{params:{'subscriberId':$scope.subscriberId,'userId':$scope.userId}}).success(function(resp){
				$scope.reportMDs = resp;
				if(resp.length > 0) {
					$scope.description = resp[0].description;
				}
				if(!$scope.isReportSaved) {
					$('#tab0').click();
				}
				$("#loading").hide();
				$scope.currenttab = 'other';
				$scope.isAnyActiveReport = true;
			});
		};
		
		$scope.loadReportsMdDummy = function () {
			$("#loading").show();
			$http.get('/template/reports/file/md.json').success(function(resp){
				$scope.reportMDs = resp;
				$("#loading").hide();
			});
		};
		
		$scope.loadReportsMd();
		
		$scope.reportTemplate = {
				jsonForm:{},
				jsonSchema:[],
				model:{}
				
		};
		$scope.selectedIndex = 0;
		$scope.showExcButton = false;
		$scope.isAnyActiveReport= false;
		$scope.showReport = function (report) {
			$scope.isCompanyDetail = false;
			$scope.backButton = false;
			if(report.isPlugin == 1) {
				
				$("#notPlugnin").hide();
				$("plugnin").show();
				
				$scope.backBtn = true;
				$scope.isAnyActiveReport = false;
				$scope.isSavedTemplate = false;
				$scope.isSavedTemplateTable = false;
				$scope.reportTemplate.jsonForm = report.jsonForm;
				$scope.reportTemplate.jsonSchema = report.jsonSchema;
				$scope.showPivot = report.isJava;
				$scope.description = report.description;
				setTimeout(function(){
					/*$('[name=DC_AD_ORIENTATION]').unbind();
					$('[name=DC_AD_SECTION]').unbind();
					$('[name=DC_AD_TYPE]').unbind();
					$('[name=DC_AD_SIZE]').unbind();
					$('[name=DC_JOB_DENSITY]').unbind();
					$('[name=DC_AD_CATEGORY]').unbind();
					$('[name=DC_SEARCH_ADVERTISER_TYPE]').unbind();
					$('[name=DC_ADVERTISER_TYPE]').unbind();
					$('[name=DC_PUBLICATION_TITLE]').unbind();*/
				
				
				
					/*$('[name=DC_AD_SECTION]').prop("checked", true);
					$('[name=DC_AD_ORIENTATION]').prop("checked", true);
					$('[name=DC_AD_TYPE]').prop("checked", true);
					$('[name=DC_AD_SIZE]').prop("checked", true);
					$('[name=DC_JOB_DENSITY]').prop("checked", true);
					$('[name=DC_AD_CATEGORY]').prop("checked", true);
					$('[name=DC_SEARCH_ADVERTISER_TYPE]').prop("checked", true);
					$('[name=DC_ADVERTISER_TYPE]').prop("checked", true);
					$('[name=DC_PUBLICATION_TITLE]').prop("checked", true);
					$('[name=DC_SUBSCRIBER_TERRITORY]').prop("checked", true);*/
				
					var d = new Date();
					var fd = d.getMonth()>3 ? d.getMonth()-4 : d.getMonth()+8;
					//$('[name=fromdatemm]').val(fd>9?fd:"0"+fd);
					//$('[name=fromdateyy]').val(d.getMonth()>3 ? d.getFullYear() : d.getFullYear()-1);
					//$('[name=todatemm]').val(d.getMonth()>9?d.getMonth():"0"+d.getMonth());
					//$('[name=todateyy]').val(d.getFullYear());
					$scope.$apply(function(){
						$scope.reportTemplate.model.fromdatemm = d.getMonth()>3 ? d.getMonth()-4 : d.getMonth()+8;
						$scope.reportTemplate.model.fromdateyy = d.getMonth()>3 ? d.getFullYear() : d.getFullYear()-1;
						$scope.reportTemplate.model.todatemm = d.getMonth();
						$scope.reportTemplate.model.todateyy = d.getFullYear();
					});	
				
				
				},1500);
				//$scope.reportTemplate.model = {};
				$scope.showExcButton = true;
				$scope.currenttab = 'other';
				$('#custom-search-tab a').click();
				if(report.pivotConfig!=null) {
					$scope.pivotConfig = report.pivotConfig;
					$scope.searchConfig = JSON.parse(report.searchCriteria);
					$scope.isSavedTemplate = true;
					$scope.runReport(2);
					$scope.expanded = true;
				} else if(report.searchCriteria!=null) {
					$scope.searchConfig = JSON.parse(report.searchCriteria);
					$scope.isSavedTemplateTable = true;
					$scope.runReport(2);
					$scope.expanded = true;
				} else {
					$scope.reportTemplate.model.id = report.id;
				}
			} else {
				$("#notPlugnin").show();
				$("plugnin").hide();
				$scope.storyboard = {
					DC_PUBLICATION_TITLE:[],
					PUBLICATION_YEAR:"",
					PUBLICATION_MONTH:"",
					PUBLICATION_DATE:"",
					id:report.id
				};
					
				$scope.publications;
				$http.get('/webapp/getAllPublication').success(function(data){
	        		$scope.publications = data;
	        		for(var i = 0; i < data.length; i++) {
	        			$scope.selectedPublications[i++] = false;
	        		}
	        	});		
			}
		};
		$scope.years = [];
		$scope.months = [];
		$scope.days = [];
		$scope.selectedPublications = [];
		
		
		
		$scope.allImage=22;
		
		$scope.setAdvertorial=function(){
			
	
			$scope.storyboard.advetorialId=$scope.allImage;
			
			if($scope.filterStatus==1){
				$scope.getPublicationYear();
			}else if($scope.filterStatus==2){
				$scope.getYearData();
			}else if($scope.filterStatus==3){
				$scope.getMonthData();
			}else if($scope.filterStatus==4){
				$scope.getDatePublication();
			}
			
		};
		
		$scope.filterStatus=0;
		
		$scope.getPublicationYear = function() {
			$scope.loading=true;
			$scope.filterStatus=1;
			$scope.storyboard.advetorialId=$scope.allImage;
			$scope.storyboard.DC_PUBLICATION_TITLE = [];
			for (var i = 0; i < $scope.selectedPublications.length; i++) {
				if($scope.selectedPublications[i]) {
					$scope.storyboard.DC_PUBLICATION_TITLE.push($scope.publications[i].value);
				}
			}
			
			if ($scope.storyboard.DC_PUBLICATION_TITLE.length > 0) {
				$http.post('/webapp/getAllPublicationData',$scope.storyboard).success(function(data){
					$scope.years = data.years;
					$scope.gridData = data.reports;
					$scope.loading=false;
				});
			} else {
				$scope.gridData = [];
				$scope.years = [];
				$scope.months = [];
				$scope.days = [];
			}
			$scope.storyboard.PUBLICATION_YEAR = "";
			$scope.storyboard.PUBLICATION_MONTH = "";
			$scope.storyboard.PUBLICATION_DATE = "";
		};
		
		$scope.getYearData = function() {
			$scope.storyboard.advetorialId=$scope.allImage;
			$scope.loading=true;
			$scope.filterStatus=2;
			$scope.storyboard.PUBLICATION_MONTH = "";
			$scope.storyboard.PUBLICATION_DATE = "";
			$http.post('/webapp/get-year-publication',$scope.storyboard).success(function(data) {
				$scope.months = data.months;
				$scope.gridData = data.reports;
				$scope.loading=false;
			});
		};	
		
		$scope.getMonthData = function() {
			$scope.storyboard.advetorialId=$scope.allImage;
			$scope.loading=true;
			$scope.filterStatus=3;
			$scope.storyboard.PUBLICATION_DATE = "";
			$http.post('/webapp/get-month-publication',$scope.storyboard).success(function(data) {
				$scope.days = data.days;
				$scope.gridData = data.reports;
				$scope.loading=false;
			});
		};
		
		$scope.getDatePublication = function(date) {
			$scope.storyboard.advetorialId=$scope.allImage;
			$scope.loading=true;
			$scope.filterStatus=4;
			$scope.storyboard.PUBLICATION_DATE = date;
			$http.post('/webapp/get-date-publication',$scope.storyboard).success(function(data) {
				$scope.gridData = data.reports;
				$scope.loading=false;
			});
		};
		
		var dateFormat =       $.pivotUtilities.derivers.dateFormat;
		var sortAs =           $.pivotUtilities.sortAs;
		var renderers = $.extend(
                 $.pivotUtilities.renderers, 
                 $.pivotUtilities.d3_renderers
                 );
		for(var k in $.pivotUtilities.c3_renderers)
         renderers["C3 "+k] = $.pivotUtilities.c3_renderers[k];
		
		var stdRendererNames = ["Table","Table Barchart","Heatmap","Row Heatmap","Col Heatmap"];
		$.each(stdRendererNames, function() {
			var rName = this;
			renderers[rName] = nrecoPivotExt.wrapTableRenderer(renderers[rName]);
		});
        $scope.lastQueryExecuted = 1;
        window.drillDown = function(v){
        	var obj = $scope.$eval($(v).attr('json'));
        	executeReport(obj);
        };
        window.openPopUp = function(e,id,option) {
        	if(option==1) {
        		$http.get('/webapp/getParentImage?parentImageId='+id).success(function(data){
        			$("#parent-img").attr("src","/files/fracts_files/images/parent/"+data);
        			$("#parent-popup").css("height",window.screen.availHeight+"px");
        			$("#parent-popup").modal({backdrop:"static"});
        			$(".modal-backdrop").removeClass("modal-backdrop");
        		});
        	} else {
        		$http.get('/webapp/getParentImage?parentImageId='+id).success(function(data){
        			$("#imgcrp").attr("src","/files/fracts_files/images/parent/"+data);
        			$("#parent-zoom-popup-title").text("Parent Image");
        			$("#parent-zoom-popup").css("height",(window.screen.availHeight-170)+"px");
        			$("#imgcrp").css("height",(window.screen.availHeight-300)+"px")
        			$("#parent-zoom-popup").modal({backdrop:"static"});
        			$(".modal-backdrop").removeClass("modal-backdrop");
        		});
        	}
        };
        
        window.openChildPopUp = function(e,parentId,id) {
        	$http.get('/webapp/getChildImage?childImageId='+id).success(function(data){
        		$("#imgcrp").attr("src","/files/fracts_files/images/child/"+parentId+"/"+id+"/"+data);
        		$("#parent-zoom-popup-title").text("Child Image");
        		$("#parent-zoom-popup").css("height",(window.screen.availHeight-170)+"px");
        		$("#parent-zoom-popup").modal({backdrop:"static"});
        		$("#imgcrp").css("height",(window.screen.availHeight-300)+"px")
        		$(".modal-backdrop").removeClass("modal-backdrop");
        	});
        };
        
        window.editPopup = function(e,id) {
        	$scope.deId = id;
			$scope.getAllData();
			$scope.getDeData();
        };
        
        window.buttonClicked = function(e,rowData,btnType) {
        	console.log(e);
        	console.log(rowData);
        	console.log(btnType);
        	if(btnType=="ocr") {
        		$http.get('/webapp/getChildImage?childImageId='+rowData['childId']).success(function(data){
            		$("#ocr-text-area-img").attr("src","/files/fracts_files/images/child/"+rowData['parentId']+"/"+rowData['childId']+"/"+data);
            		$(".modal-backdrop").removeClass("modal-backdrop");
            	});
        		$http.get('/webapp/getOcrTextArea?deDataId='+rowData['DN_ID']).success(function(response) {
        			$("#ocr-text-area").val(response);
        			$("#ocr-text-popup").modal({backdrop:"static"});
            		$(".modal-backdrop").removeClass("modal-backdrop");
        		});
        	}
        };
        
        $scope.isAdvertiseTypeLoaded = false;
        $scope.isInstituteTypeLoaded = false;
        $scope.isAdCategoryLoaded = false;
        $scope.isAllCompanyLoaded = false;
        
        $scope.getAllData = function() {
        	$("#loading").show();
        	$scope.getAdvertiserType();
        };
        
        $scope.getAdvertiserType = function() {
        	if(!$scope.isAdvertiseTypeLoaded) {
        		$http.get('/webapp/getAdvertiserType').then(function(response) {
        			$scope.isAdvertiseTypeLoaded = true;
        			$scope.allAdvertiserType = response.data;
        			$scope.getInstituteType();
        		},function(error){
        			$("#loading").hide();
        		});
        	} else {
        		$scope.getInstituteType();
        	}
        };
        
        $scope.getInstituteType = function() {
        	if(!$scope.isInstituteTypeLoaded) {
        		$http.get('/webapp/getInstituteType').then(function(response) {
        			$scope.isInstituteTypeLoaded = true;
        			$scope.allInstituteType = response.data;
        			$scope.getAdCategory();
        		},function(error){
        			$("#loading").hide();
        		});
        	} else {
        		$scope.getAdCategory();
        	}
        };
        
        $scope.getAdCategory = function() {
        	if(!$scope.isAdCategoryLoaded) {
        		$http.get('/webapp/getAdCategory').then(function(response) {
        			$scope.isAdCategoryLoaded = true;
        			$scope.allAdCategory = response.data;
        			$scope.getAllCompany();
        		},function(error){
        			$("#loading").hide();
        		});
        	} else {
        		$scope.getAllCompany();
        	}
        };
        
        $scope.getAllCompany = function() {
        	if(!$scope.isAllCompanyLoaded) {
        		$http.get('/webapp/getAllCompany').then(function(response) {
        			$scope.allCompany = response.data;
        			$scope.isAllCompanyLoaded = true;
        			$("#loading").hide();
        			$("#updateDeId").val($scope.deId);
                	$("#updateDE").modal({backdrop:"static"});
        			$(".modal-backdrop").removeClass("modal-backdrop");
        		},function(error){
        			$("#loading").hide();
        		});
        	} else {
        		$("#loading").hide();
        		$("#updateDeId").val($scope.deId);
            	$("#updateDE").modal({backdrop:"static"});
    			$(".modal-backdrop").removeClass("modal-backdrop");
        	}
        };
        
        $scope.getDeData = function() {
			$scope.deData = {};
			$http.get('/webapp/getDeData',{params:{'id':$scope.deId}}).then(function(response) {
				$scope.deData = response.data;
			},function(error) {
			});
		};
		
		$scope.updateDe = function() {
			console.log($scope.deData);
			if(($scope.deData.endCurrency == '' && $scope.deData.startCurrency == '') ||parseInt($scope.deData.endCurrency) && parseInt($scope.deData.startCurrency) && $scope.deData.endCurrency<$scope.deData.startCurrency) {
				$http.post('/webapp/updateDe',$scope.deData).then(function(response) {
					$("#updateDE").modal('hide');
				},function(error) {
				});
			} else {
				alert("Invalid Currency Range");
			}
		};
        
        $scope.pivotTab = false;
        $scope.showPivotFun = function() {
        	$scope.pivotTab = true;
        	$scope.runReport(1);
        };
        
        $scope.showAdvertorialReport = function(index){
        	if($scope.TabIndex == 0 || angular.isUndefined($scope.TabIndex)){
        		$("#table-view1").show();
        		$("#table-view").hide();
        	} else{
        		$("#table-view1").hide();
        		$("#table-view").show();
        	}
        }
        
        $scope.TabIndex;
        $scope.setIndex = function(index){
        	$scope.TabIndex  = index;
        	if(index == 0) {
        		$("#notPlugnin").show();
        		$("#plugin").hide();
        		$scope.showExcButton = false;
        	} else {
        		$scope.showExcButton = true;
        		$("#notPlugnin").hide();
        		$("#plugin").show();
        	}
        }
        
        $scope.openParentImage = function(id){
        	$scope.option = 2;
        	if($scope.option == 1) {
        		$http.get('/webapp/getParentImage?parentImageId='+id).success(function(data){
        			$("#parent-img").attr("src","/files/fracts_files/images/parent/"+data);
        			$("#parent-popup").css("height",window.screen.availHeight+"px");
        			$("#parent-popup").modal({backdrop:"static"});
        			$(".modal-backdrop").removeClass("modal-backdrop");
        		});
        	} else {
        		$http.get('/webapp/getParentImage?parentImageId='+id).success(function(data){
        			$("#imgcrp").attr("src","/files/fracts_files/images/parent/"+data);
        			$("#parent-zoom-popup-title").text("Parent Image");
        			$("#parent-zoom-popup").css("height",(window.screen.availHeight-170)+"px");
        			$("#imgcrp").css("height",(window.screen.availHeight-300)+"px")
        			$("#parent-zoom-popup").modal({backdrop:"static"});
        			$(".modal-backdrop").removeClass("modal-backdrop");
        		});
        	}
        }

        
        $scope.runReport = function (option) {
			$scope.isAnyActiveReport = false;
			$scope.lastQueryExecuted = option;
			var obj;
			$scope.backButton = false;
			$scope.isCompanyDetail = false;
			if(option==2) {
				obj = $scope.searchConfig;
			} else {
				obj = $scope.reportTemplate.model;
			}
			if(typeof obj.id =="undefined")
				obj.id = $scope.searchConfig.id;
			
			executeReport(obj);
		}
        $scope.backButton = false;
		executeReport = function(obj) {
			$("#loading").show();
			$http.post('/webapp/report/run',obj).success(function(data){
				$("#loading").hide();
				if( obj.id == 19 ) {
					$scope.backButton = true;
				} else {
					$scope.backButton = false;
				}
				if(data.company) {
					$scope.isCompanyDetail = true;
					if(data.company.companyName) {
						$("#companyName").text(data.company.companyName+",");
					}
					if(data.company.addressLine1) {
						$("#addressLine1").text(data.company.addressLine1+",");
					}
					if(data.company.country) {
						$("#country").text(data.company.country);
					}
				} else {
					$scope.isCompanyDetail = false;
				}
				if($scope.pivotTab) {
					$scope.pivotTab = false;
					var parent = $("#pivot-table-output").parent();
					$("#pivot-table-output").remove();
					parent.append("<div id='pivot-table-output' style='margin: 10px;'></div>");
					$("#pivot-table-output").pivotUI(data.data, {
						renderers: renderers,
						rows:[],
						cols:[],
						vals:[],
						rendererName:'Table',
						aggregatorName:'Count',
						derivedAttributes: {
							"Year":       dateFormat("Issue Date", "%y", true),
							"Month": dateFormat("Issue Date", "%n", true),
						},
						sorters: function(attr) {
							if(attr == "Month") {
								return sortAs(["Jan","Feb","Mar","Apr", "May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]);
							}
						},
						onRefresh:$scope.onRefresh,
						hiddenAttributes: data.hiddenpivotcol//["DN_ID","ISSUE_DATE"],
					});
					$scope.isAnyActiveReport = false;
				} else if($scope.isSavedTemplateTable) {
					if($scope.description == "advertorial") {
						$scope.gridData = data.data;
					} else {
						$scope.reportData = data.data;
						$scope.dtColumns = data.columns;
					}
					$('a[href="#table-view"]').click();
					$("#pivot-table-output").hide();
					$('a[href="#pivot-view"]').parent().hide();
					$('a[href="#table-view"]').parent().show();
					$scope.isAnyActiveReport = true;
				} else if($scope.isSavedTemplate) {
					$('a[href="#pivot-view"]').parent().show();
					$('a[href="#table-view"]').parent().hide();
				} else {
					$('a[href="#pivot-view"]').parent().show();
					$('a[href="#table-view"]').parent().show();
					if($scope.description == "advertorial") {
						$scope.gridData = data.data;
					} else {
						$scope.reportData = data.data;
						$scope.dtColumns = data.columns;
					}					
					$('a[href="#table-view"]').click();
					var parent = $("#pivot-table-output").parent();
					$("#pivot-table-output").remove();
					parent.append("<div id='pivot-table-output' style='margin: 10px;'></div>");
					$("#pivot-table-output").pivotUI(data.data, {
						renderers: renderers,
						rows:[],
						cols:[],
						vals:[],
						rendererName:'Table',
						aggregatorName:'Count',
						derivedAttributes: {
							"Year":       dateFormat("Issue Date", "%y", true),
							"Month": dateFormat("Issue Date", "%n", true),
						},
						sorters: function(attr) {
							if(attr == "Month") {
								return sortAs(["Jan","Feb","Mar","Apr", "May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]);
							}
						},
						onRefresh:$scope.onRefresh,
						hiddenAttributes: data.hiddenpivotcol//["DN_ID","ISSUE_DATE"],
					});
					
				}
				$scope.isAnyActiveReport = true;
			});
		};
		
		$scope.onRefresh = function(config) {
			$scope.config = {};
			$scope.config.rendererName = config.rendererName;
			$scope.config.vals = config.vals;
			$scope.config.aggregatorName = config.aggregatorName;
			$scope.config.cols = config.cols;
			$scope.config.rows = config.rows;
		};
		
		$scope.openTemplateModal = function(option) {
			$scope.option = option;
			$("#template-save-modal").modal({backdrop:"static"});
		};
		
		$scope.deleteReport = function(id) {
			bootbox.confirm("Are you sure?", function(result) {
				if(result) {
					$("#loading").show();
					$http({url:"/webapp/deleteReport",params:{id:id},method:"get"}).success(function(data) {
						$("#loading").hide();
						$scope.reportMDs = data;
					});
				}
			});
		};
		
		$scope.back = function() {
			$scope.expanded = false;
			$scope.currenttab = 'report';
			$("a[href='#saved-report']").trigger("click");
			$scope.backBtn = false;
		};
		
		$scope.showLargeImage = function(url) {
			$("#img-enlarge").attr("src",url);
			$("#enlarge-image-modal").modal({backdrop:"static"});
		};
		
		$scope.saveTemplate = function() {
			
			if(typeof $scope.templateName != "undefined" && $scope.templateName.length!=0) {
				$("#template-error").hide();
				$scope.isReportSaved = true;
				if($scope.option==1) { 
					var data = {
						parentId:$scope.reportTemplate.model.id,
						templateName:$scope.templateName,
						searchCriteria:JSON.stringify($scope.reportTemplate.model)
					};
					$("#loading").show();
					$http({url:"/webapp/report/saveTemplate",data:data,method:"post"}).success(function(data) {
						$scope.loadReportsMd();
						$("#template-save-modal").modal('hide');
						$("#loading").hide();
					});
				} else {
					var data = {
						parentId:$scope.reportTemplate.model.id,
						templateName:$scope.templateName,
						data:JSON.stringify($scope.config),
						searchCriteria:JSON.stringify($scope.reportTemplate.model)
					};
					$("#loading").show();
					$http({url:"/webapp/report/saveTemplate",data:data,method:"post"}).success(function(data) {
						$scope.loadReportsMd();
						$("#template-save-modal").modal('hide');
						$("#loading").hide();
					});
				}
			} else {
				$("#template-error").show();
			}
		};
	  
	});
	
	App.directive('myDatatable', function() {
		  function link(scope, element, attrs) {
			  
			decorateColumns = function (cols) {
				var columns = scope.$eval(cols);
				$.each(columns, function(i,e){
					if(e.link) {
						e.render = function(cellData, type, rowData) {
							return "<a href='#'" + e.link + "/" + rowData.ids + ">" + cellData + "</a>";
						
						}
					} 
				});
				return columns;
			};
			
		    scope.$watch('finishedHeader', function(val) {
		      if (val) {
		    	  if(window.oTable) {  
			    		try{
			    		    window.oTable.fnDestroy();
			    		}catch(e) {
			    			window.oTable.fnClearTable();
			    		}
			    		window.oTable.empty();
			    		window.oTable = null;
			    	}
		    	
		    	existingHead = $(element).find('thead');
		    	existingHead.remove();
		    	$(element).empty();
		    	$(element).append($("#tableHeaderTmp").find('thead').clone());  
		        
		    	window.oTable = $(element).dataTable({
		          sDom: '<"clear">TlfCrtip',
		          pageLength: 1000,
		          "paging": false,
		          //sScrollY: "500px",
		          tableTools: {
		        	  "sSwfPath": "/webapp/report/app/swf/copy_csv_xls_pdf.swf",
			            
		              "aButtons": [
		                  "copy",
		                  "print",
		                  {
		                      "sExtends":    "collection",
		                      "sButtonText": "Save",
		                      "aButtons":    [ "csv", "xls", "pdf" ]
		                  }
		              ]
		          },
		          
		          columns: decorateColumns(attrs.aaColumns) ,
		          data:scope.$eval(attrs.aaData)
		        });
		    	$(element).find('thead').css("background","darkgray");
		      }
		    });

		    
		  }
		  return {
		    link: link
		  };
		})
		  .directive('generateDatatable', function() {
		    function link(scope, element, attrs) {
		      if (scope.$last) {
		          scope.$parent.finishedHeader = Math.random();
		      }
		    }
		    return {
		      link: link
		    };
		  });
	App.directive('myDatatable1', function() {
		  function link(scope, element, attrs) {
			  
			decorateColumns = function (cols) {
				var columns = scope.$eval(cols);
				$.each(columns, function(i,e){
					if(e.composite) {
						e.render = function(cellData, type, rowData) {
							console.log(e.composite.split(','));
							var arr = e.composite.split(',');
							var res = "<div ondblclick='editPopup(event,"+rowData['DN_ID']+")' style='width:100%;height:100px;cursor:pointer;'>";
							angular.forEach(arr,function(val,key){
								res += "<div>"+rowData[val]+"</div>";
							});
							return res+"</div>";
						};
					} else if(e.linkpopup) {
						e.render = function(cellData, type, rowData) {
							//return cellData;
							if(cellData == undefined || (cellData[0] == undefined &&  cellData[1] == "TODO") ) return "";
							if(cellData[1] == "TODO") return "<b>" + cellData[0] + "</b>";
							
							return "<a style='cursor:pointer;' onClick='openPopUp(event,"+cellData+",1);return false;'><span class='glyphicon glyphicon-eye-open' style='font-size:14px;color:black;'></span></a>";
						
						};
					}else if(e.link) {
						e.render = function(cellData, type, rowData) {
							//return cellData;
							if(cellData == undefined || (cellData[0] == undefined &&  cellData[1] == "TODO") ) return "";
							if(cellData[1] == "TODO") return "<b>" + cellData[0] + "</b>";
							
							return "<a json='"+cellData[1]+"' onClick='drillDown(this);return false;' href='#'>" + cellData[0] + "</a>";
						
						};
					}else if(e.img) {						
						e.render = function(cellData,type,rowData) {

							if(cellData.indexOf("http")===0 || cellData.indexOf("www.")===0) {
								return "<img src='"+cellData+"' style='width:100px;height:100px;cursor:pointer;'>";
							} else if(cellData.indexOf("Parent")===0) {
								return "<img  onClick='openPopUp(event,"+rowData['parentId']+",2)' src='/webapp/getParentImageThumb?id="+rowData['parentId']+"' style='height:100px;cursor:pointer;'>";
							} else if(cellData.indexOf("Child")===0) {
								return "<img  onClick='openChildPopUp(event,"+rowData['parentId']+","+rowData['childId']+")' src='/webapp/getChildImageThumb?id="+rowData['childId']+"&parentId="+rowData['parentId']+"' style='height:100px;cursor:pointer;'>";
							}
						};
					} else if(e.button) {
						e.render = function(cellData,type,rowData) {
							console.log(e);
							return "<button onClick='buttonClicked(event,"+JSON.stringify(rowData)+",\""+e.btnType+"\")' class='btn btn-primary'>"+cellData+"</button>"
						};
					} else {
						e.render = function(cellData,type,rowData) {
							return "<div ondblclick='editPopup(event,"+rowData['DN_ID']+")' style='width:100%;height:100px;cursor:pointer;'>"+cellData+"</div>";
						};
					}
				});
				return columns;
			};
			
		    scope.$watch('finishedHeader1', function(val) {
		      if (val) {
		    	  if(window.oTable1) {  
			    		try{
			    		    window.oTable1.fnDestroy();
			    		}catch(e) {
			    			window.oTable1.fnClearTable();
			    		}
			    		window.oTable1.empty();
			    		window.oTable1 = null;
			    	}
		    	
		    	existingHead = $(element).find('thead');
		    	existingHead.remove();
		    	$(element).empty();
		    	$(element).append($("#tableHeaderTmp1").find('thead').clone());  
		    	$("#tableHeaderTmp1").find('thead').css("background","darkgray");
		    	window.oTable1 = $(element).dataTable({
		          sDom: '<"clear">TlfCrtip',
		          pageLength: 1000,
		          "paging": false,
		          //sScrollY: "500px",
		          tableTools: {
		        	  "sSwfPath": "/webapp/report/app/swf/copy_csv_xls_pdf.swf",
			            
		              "aButtons": [
		                  "copy",
		                  "print",
		                  {
		                      "sExtends":    "collection",
		                      "sButtonText": "Save",
		                      "aButtons":    [ "csv", "xls", "pdf" ]
		                  }
		              ]
		          },
		          
		          columns: decorateColumns(attrs.aaColumns) ,
		          data:scope.$eval(attrs.aaData)
		        });
		    	$(element).find('thead').css("background","darkgray");
		      }
		    });
		    
		  }
		  return {
		    link: link
		  };
		})
		  .directive('generateDatatable1', function() {
		    function link(scope, element, attrs) {
		      if (scope.$last) {
		          scope.$parent.finishedHeader1 = Math.random();
		      }
		    }
		    return {
		      link: link
		    };
		  });
	
	
	