/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* 
    Created on : Feb 8, 2018, 1:45:53 PM
    Author     : aktharhussainis
*/

var host = null;
$(document).ready(function() {

    host = 'http://' + location.host;
    var table = $('#followup').DataTable({
        "language": {
            "zeroRecords": "No records available",
            "infoEmpty": "No records available"
        },
		"autoWidth": true,
        "ajax": {
            url: '/oxalis-client/services/getFollowUpDocuments',
            dataSrc: 'messages'
        },
        "columns": [
            { "data": "senderId"},
            { "data": "receiverId"},
            { "data": "fileName" },
            {  "data": null,
               "defaultContent": "<button>Retry</button>"
            }
        ]
    });
    
    $('#followup tbody').on( 'click', 'button', function () {

        $('#loading').show();
        var data = table.row( $(this).parents('tr') ).data();
        var message = new messageJSON();
        message.senderId = data.senderId;
	message.receiverId = data.receiverId;
	message.fileName = data.fileName;
        $.ajax({
                url: host + "/oxalis-client/services/sendFollowUpDocuments",
                dataType: 'json',
                contentType: "application/json",
                type: "POST",
                data: JSON.stringify(message),
                success: function(result) {

                    $('#loading').hide();
                    $('#followup').DataTable().clear().draw();
                    $('#followup').DataTable().ajax.reload();
                     if (result) {
                        $('#success').text("File uploaded successfully");
                        $( "div.success" ).fadeIn( 300 ).delay( 1500 ).fadeOut( 400 );
                    } else {
                        $('#failure').text("Filed to upload file");
                        $( "div.failure" ).fadeIn( 300 ).delay( 1500 ).fadeOut( 400 );
                    }
                }
        });
    });
});

function messageJSON() {
	
    this.senderId= '';
    this.receiverId= '';
    this.fileName= '';
}