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
				$http({method:'post',url:'/webapp/reports/drildownreport',data:{searchCriteria:lastQueryCriteria,filters:dataFiler}}).success(function(data) {
					$scope.detailsData = data.data;
					$scope.detailColumns = data.columns;
					$("a[href='#detail-view']").click();
				});
			}
		});
		
		$scope.reportMDs = [];
		
		$scope.loadReportsMd = function () {
			$http.get('/webapp/reports/md',{params:{'subscriberId':$scope.subscriberId,'userId':$scope.userId}}).success(function(resp){
				$scope.reportMDs = resp;
				if(!$scope.isReportSaved) {
					$('#saved-report-tab a').click();
				}
			});
		};
		
		$scope.loadReportsMdDummy = function () {
			$http.get('/template/reports/file/md.json').success(function(resp){
				$scope.reportMDs = resp;
			});
		};
		
		$scope.loadReportsMd();
		
		$scope.reportTemplate = {
				jsonForm:{},
				jsonSchema:[],
				model:{}
				
		};
		
		$scope.showExcButton = false;
		$scope.isAnyActiveReport= false;
		$scope.showReport = function (report) {
			$scope.backBtn = true;
			$scope.isAnyActiveReport = false;
			$scope.isSavedTemplate = false;
			$scope.isSavedTemplateTable = false;
			$scope.reportTemplate.jsonForm = report.jsonForm;
			$scope.reportTemplate.jsonSchema = report.jsonSchema;
			$scope.showPivot = report.isJava;
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
				$('[name=fromdatemm]').val(fd>9?fd:"0"+fd);
				$('[name=fromdateyy]').val(d.getMonth()>3 ? d.getFullYear() : d.getFullYear()-1);
				$('[name=todatemm]').val(d.getMonth()>9?d.getMonth():"0"+d.getMonth());
				$('[name=todateyy]').val(d.getFullYear());
				$scope.reportTemplate.model.fromdatemm = d.getMonth()>3 ? d.getMonth()-4 : d.getMonth()+8;
				$scope.reportTemplate.model.fromdateyy = d.getMonth()>3 ? d.getFullYear() : d.getFullYear()-1;
				$scope.reportTemplate.model.todatemm = d.getMonth();
				$scope.reportTemplate.model.todateyy = d.getFullYear();
				
				
			},1500);
			//$scope.reportTemplate.model = {};
			$scope.showExcButton = true;
			$scope.currenttab = 'search';
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
		$scope.runReport = function (option) {
			$scope.isAnyActiveReport = false;
			$scope.lastQueryExecuted = option;
			var obj;
			if(option==2) {
				obj = $scope.searchConfig;
			} else {
				obj = $scope.reportTemplate.model;
			}
			if(typeof obj.id =="undefined")
				obj.id = $scope.searchConfig.id;
			
			executeReport(obj);
		}
		executeReport = function(obj) {
			$http.get('/webapp/report/run',{params:{filter:obj}}).success(function(data){
				if($scope.isSavedTemplateTable) {
					$scope.reportData = data.data;
					$scope.dtColumns = data.columns;
					$('a[href="#table-view"]').click();
					$("#pivot-table-output").hide();
					$('a[href="#pivot-view"]').parent().hide();
					$('a[href="#table-view"]').parent().show();
					$scope.isAnyActiveReport = true;
				} else if($scope.isSavedTemplate) {
					$('a[href="#pivot-view"]').parent().show();
					$('a[href="#table-view"]').parent().hide();
					var parent = $("#pivot-table-output").parent();
					$("#pivot-table-output").remove();
					parent.append("<div id='pivot-table-output' style='margin: 10px;'></div>");
					$("#pivot-table-output").pivotUI(data.data, {
						renderers: renderers,
						rendererName:$scope.pivotConfig.rendererName,
						aggregatorName:$scope.pivotConfig.aggregatorName,
						derivedAttributes: {
							"Year":       dateFormat("Issue Date", "%y", true),
							"Month": dateFormat("Issue Date", "%n", true),
						},
						sorters: function(attr) {
							if(attr == "Month") {
								return sortAs(["Jan","Feb","Mar","Apr", "May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]);
							}
						},
						rows:$scope.pivotConfig.rows,
						cols:$scope.pivotConfig.cols,
						vals:$scope.pivotConfig.vals,
						hiddenAttributes: data.hiddenpivotcol//["DN_ID","ISSUE_DATE"],
					});
					$scope.isAnyActiveReport = true;
					$("#pivot-table-output td").hide();
					$(".pvtRendererArea").show();
					$(".pvtTable td").show();
					$('a[href="#pivot-view"]').click();
				} else {
					$('a[href="#pivot-view"]').parent().show();
					$('a[href="#table-view"]').parent().show();
					$scope.reportData = data.data;
					$scope.dtColumns = data.columns;
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
					$http({url:"/webapp/deleteReport",params:{id:id},method:"get"}).success(function(data) {
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
					$http({url:"/webapp/report/saveTemplate",data:data,method:"post"}).success(function(data) {
						$scope.loadReportsMd();
						$("#template-save-modal").modal('hide');
					});
				} else {
					var data = {
						parentId:$scope.reportTemplate.model.id,
						templateName:$scope.templateName,
						data:JSON.stringify($scope.config),
						searchCriteria:JSON.stringify($scope.reportTemplate.model)
					};
					$http({url:"/webapp/report/saveTemplate",data:data,method:"post"}).success(function(data) {
						$scope.loadReportsMd();
						$("#template-save-modal").modal('hide');
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
					if(e.link) {
						e.render = function(cellData, type, rowData) {
							//return cellData;
							if(cellData == undefined || (cellData[0] == undefined &&  cellData[1] == "TODO") ) return "";
							if(cellData[1] == "TODO") return "<b>" + cellData[0] + "</b>";
							
							return "<a json='"+cellData[1]+"' onClick='drillDown(this);return false;' href='#'>" + cellData[0] + "</a>";
						
						};
					} 
					if(e.img) {
						e.render = function(cellData,type,rowData) {
							if(cellData.indexOf("http")===0 || cellData.indexOf("www.")===0) {
								return "<img class='enlarge-img' src='"+cellData+"' style='width:100px;height:100px;cursor:pointer;'>";
							} else {
								return "<img class='enlarge-img' src='/webapp/report/getImage/"+rowData.DN_ID+"' style='width:100px;height:100px;cursor:pointer;'>";
							}
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
	
	
	