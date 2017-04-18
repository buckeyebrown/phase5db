function sendRequestAndObtainResponse(){
	sendRequest.sendRequestToServlet();
}
var showTable = true;

var sendRequest = {
	sendRequestToServlet : function() {
		var queryCode = sendRequest.obtainQueryCodeFromHTML();
		sendRequest.checkIfValidCode(queryCode);
	},
		
	obtainQueryCodeFromHTML : function() {
		var queryCode = $("#selectedQueryCode").val();
		return queryCode;
	},

	checkForValidMarketCode : function(queryCode) {
		return !(queryCode == 0);
	},

	checkIfValidCode : function(queryCode) {

		var validQueryCode = sendRequest.checkForValidMarketCode(queryCode);
		if (!validQueryCode) {
			sendRequest.alertIfInvalidQueryCode();
		}
		else {
			buttonState.changeButtonState(2);
			sendRequest.sendRequestWithQueryCode(queryCode);
		}
	},

	alertIfInvalidQueryCode : function() {
		var message = "Please enter a correct Query Code.";
		$(".ErrorMessage").html(message);
	},

	sendRequestWithQueryCode : function(queryCode) {
		$.ajax({
			"url" : "http://localhost:8060//MainServ",
			data : {
				"queryCode" : queryCode
			},
			"error" : function(){
				var message = "The query was not found.";
				$(".ErrorMessage").html(message);
				buttonState.changeButtonState(1);
				},
			"success" : function(responseData) {
				$(".ErrorMessage").html("");
				buttonState.changeButtonState(1);
				handleResponse.writeResponseTables(responseData);
			}
		});
	}
}

var buttonState = {
		changeButtonState : function(buttonState) {
			if (buttonState == 1){
				$('#find').val('Search');
			}
			else if (buttonState == 2){
				$('#find').val('Searching...');
			}
		}
}

var handleResponse = {
	writeResponseTables: function (responseData) {
		var responseDataArrayLength = responseData.length;
		for (var a = 0; a < responseDataArrayLength; a++) {
			handleResponse.createTables(responseData[a], a);
		}
	},

	createTables: function (responseData, tableVal) {
		handleResponse.writeTableToHTML(responseData);
	},

	toggleShowTable: function (tableClassName) {
		$(tableClassName).slideToggle('fast');
	},

	writeTableToHTML: function (responseData) {
		var tableName = 'Query ' + $("#selectedQueryCode").val() + ' Table';
		var tableID = 'requestedTable';
		var tableHTML = handleResponse.createTableHTML(responseData,
			tableName, tableID);
		$('#tableContents').html(tableHTML);
		handleResponse.togglePipelineTables(tableID);
		var contentsName = '.' + tableID + 'Contents';
		if (!showTable) {
			handleResponse.toggleShowTable(contentsName);
		}

	},
	togglePipelineTables: function (tableID) {
		var headerName = '.' + tableID + 'Header';
		var contentsName = '.' + tableID + 'Contents';
		$(headerName).click(function () {
			handleResponse.toggleShowTable(contentsName);
		});
	},
	createTableHTML: function (responseData, tableName, tableID) {
		showTable = true;
		var tableHTML = '<h1>';
		tableHTML += '<b>';
		tableHTML += '<a class = "' + tableID + 'Header">';
		tableHTML += tableName;
		tableHTML += "</a>";
		tableHTML += '</b>';
		tableHTML += '</h1>';
		tableHTML += '<p>'
		tableHTML += '<div class = ' + tableID + 'Contents>';
		tableHTML += '<table style="width: 250px">';
		tableHTML += '<tr>';
		for (var prop in responseData[0]){
			tableHTML += '<th>';
			tableHTML += prop;
			tableHTML += '</th>';
		}
		for (var a = 0; a <= responseData.length; a++) {
			tableHTML += '<tr>';
			for (var prop in responseData[a]){
				tableHTML += '<td>';
				tableHTML += responseData[a][prop];
				tableHTML += '</td>';
			}
			tableHTML += '</tr>';
		}
		tableHTML += '</table>';
		tableHTML += '</div>'
		return tableHTML;
	}
}


module.exports = {
		sendRequest : sendRequest,
		buttonState : buttonState,
		handleResponse : handleResponse
	};
