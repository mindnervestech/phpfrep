
console.log(companies);

var myData = companies ;
 
 
var noOfRowstoShow = 100000; //set the maximum number of rows that should be displayed per page.
 
$("#exampleGrid").handsontable({
    startRows: 5,
    startCols: 5,
    rowHeaders: true,
    colHeaders: true,
       columns: [
              { title: "COMPANY NAME", type: "text",readOnly: true },
              { title: "COMPANY URL", type: "text",readOnly: true },
              { title: "CITY", type: "text",readOnly: true },
              { title: "PINCODE", type: "text",readOnly: true },
              { title: "STATE", type: "text",readOnly: true },
              { title: "COUNTRY", type: "text",readOnly: true },
              { title: "ADDRESS LINE 1", type: "text",readOnly: true },
              { title: "ADDRESS LINE 2", type: "text",readOnly: true }
              ],
              colWidths: [250, 140, 150, 80, 80, 80, 190,190],
    afterChange: function (change, source) {
        if (source === 'edit') {
            var datarow = $("#exampleGrid").handsontable('getDataAtRow', change[0][0]);
            for (row = 0, r_len = myData.length; row < r_len; row++) {
                for (col = 0, c_len = myData[row].length; col < c_len; col++) {
                    if (myData[row][col] == datarow[5]) {
                        myData[row][change[0][1]] = change[0][3];
                    }
                }
            }
        }
    },
    afterCreateRow: function (index, amount) {
        var rowvalue = myData[myData.length - 1][5];
        var rowno = rowvalue.split("||");
        var newrow = ["", "", "", "", "", "R||" + (parseInt(rowno[1]) + 1)];
 
        myData.splice(index, 0, newrow);
        getgridData(myData, "1", noOfRowstoShow);
    },
    beforeRemoveRow: function (index, amount) {
        var removerow = $("#exampleGrid").handsontable('getDataAtRow', index);
        var flag = false;
        for (row = 0, r_len = myData.length; row < r_len; row++) {
            for (col = 0, c_len = myData[row].length; col < c_len; col++) {
                if (myData[row][col] == removerow[5]) {
 
                    myData.splice(row, 1);
                    flag = true;
                }
                if (flag == true) {
                    break;
                }
            }
            if (flag == true) {
                break;
            }
        }
 
    }
});
 
loadData();
 
function loadData() {
    getgridData(myData, "1", noOfRowstoShow);
}
 
$('#searchgrid').on('keyup', function (event) {
    var value = ('' + this.value).toLowerCase(), row, col, r_len, c_len, td;
    var example = $('#exampleGrid');
    var data = myData;
    var searcharray = [];
    if (value) {
        for (row = 0, r_len = data.length; row < r_len; row++) {
            for (col = 0, c_len = data[row].length; col < c_len; col++) {
                if (data[row][col] == null) {
                    continue;
                }
                if (('' + data[row][col]).toLowerCase().indexOf(value) > -1) {
                    searcharray.push(data[row]);
                    break;
                }
                else {
                }
            }
        }
        getgridData(searcharray, "1", noOfRowstoShow);
    }
    else {
        getgridData(myData, "1", noOfRowstoShow);
    }
});
 
function getgridData(res, hash, noOfRowstoShow) {
 
    var page = parseInt(hash.replace('#', ''), 10) || 1, limit = noOfRowstoShow, row = (page - 1) * limit, count = page * limit, part = [];
 
    for (; row < count; row++) {
        if (res[row] != null) {
            part.push(res[row]);
        }
    }
 
    var pages = Math.ceil(res.length / noOfRowstoShow);
    $('#gridpage').empty();
    for (var i = 1; i <= pages; i++) {
        var element = $("<a href='#" + i + "'>" + i + "</a>");
        element.bind('click', function (e) {
            var hash = e.currentTarget.attributes[0].nodeValue;
            $('#exampleGrid').handsontable('loadData', getgridData(res, hash, noOfRowstoShow));
        });
        $('#gridpage').append(element);
    }
    $('#exampleGrid').handsontable('loadData', part);
    return part;
}
