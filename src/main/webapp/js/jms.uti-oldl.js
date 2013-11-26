/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 17.11.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
function redirect() {
    document.getElementById('myForm').target = 'my_iframe';
    document.getElementById('myForm').submit();
}
function madeAjaxCall(thisId,url) {
    var confValue = $('#'+thisId+' option:selected').val();
    console.info(confValue);
    $.ajax({
        type: "post",
        data: ({name : confValue}),
        url: url,
        success: function(response) {
            console.info(response.configurationName);
            $('#configurationNameId').text(response.configurationName);
            $('#urlId').text(response.url);
            $('#delayId').text(response.delay);
            $('#queueId').text(response.queueName);
            $('#queueReceiveId').text(response.queueNameReceive);

        },
        error: function() {
            alert('Error while request...');
        }
    })
}

//function uploadFile() {
//
//    $('#fileupload').fileupload({
//        url: '',
////        submit : function (e,data) {
////            var $this = $(this);
////            $.getJSON('upload', function(result) {
////                data.url = result.url;
////                $this.fileupload('send',data);
////            })
////        },
//        dataType: 'json',
//        success: function(response) {
//            var result = $('#result');
//            result.text(response.url);
//            result.attr('hidden',false);
//        }
//});
//}

var interval = 5000;
function receiveJmsMessage() {
    console.info('In receive. Interval is' + interval);
    var jmsConfigurationId = 1;
    var delay =  $('#delayId').val();
    console.info('delay is' + delay);
    if (!isNaN(delay) && delay > 5) {
        if (delay < 1000) {
            delay = delay *1000;
        }
        console.info(delay + " is number");
        interval = delay;
    }
    $.ajax({
        type: 'post',
        url: window.location.pathname +'/receive/get',
        success: function(response) {
            var htmlElement = $('#receivedXml');
            var xmlBody = '';
            var counter = 0;
            $.each(response, function (i,item) {
                if (i > 0) {
                    var count = i+1;
                    xmlBody = xmlBody+ '----------------\nСообщение ' + count+ '\n';
                }
                 xmlBody = xmlBody + item.messageBody + '\n';
                counter++;
            });
                if (response.length > 0) {
                     htmlElement.attr('hidden',false);
                }
                if (xmlBody.length > 1) {
                     htmlElement.text(xmlBody);
                }

        },
        error: function() {
            alert('Error while request...');
        }
    });

}
function sendMessage() {
    $('#sendButton').attr('style','display: none');
    var xml = $('#result').text();
    $.ajax({
        type: "post",
        data: ({host : $('#senderHost').val(),
            port : $('#senderPort').val(),
            channel : $('#senderChannel').val(),
            managerName : $('#senderQueueManagerName').val(),
            destinationName : $('#senderDestinationName').val(),
            correlationId : $('#senderCorrelationId').val(),
            isTopic : $('#senderIsTopic').val(),
            xmlString : xml}),
        url: window.location.pathname +'/send',
        success: function(response) {
            $('#sendResult').text(response.messageBody).attr('hidden',false);
            $('#sendButton').attr('style','display: block');
                },
        error: function() {
            alert('Error while request...');
        }
    })

}
//$(document).ready(receiveJmsMessage());
var refreshIntervalId;
function startReceivingMessages() {
    receiveJmsMessage();
    console.info(interval);
    refreshIntervalId =  setInterval('receiveJmsMessage()',interval);

}
function stopReceivingMessages() {
    clearInterval(refreshIntervalId);
    $('#start').attr('style','display: block');
    $('#stop').attr('style','display: none');
}

function startService() {
    $.ajax({
        type: "post",
        data: ({host : $('#receiverHost').val(),
            port : $('#receiverPort').val(),
            channel : $('#receiverChannel').val(),
            managerName : $('#receiverQueueManagerName').val(),
            destinationName : $('#receiverDestinationName').val(),
            correlationId : $('#receiverCorrelationId').val(),
            isTopic : $('#receiverIsTopic').val()}),
        url: window.location.pathname +'/receive/start',
        success: function(response) {
            $('#start').attr('style','display: none');
            $('#stop').attr('style','display: block');
              window.setTimeout(function() {
                  startReceivingMessages();
              },3000);
        },
        error: function() {
            alert('Error while request...');
        }
    })
}
function stopService() {
    stopReceivingMessages();
    $.ajax({
        type: "post",
        url: window.location.pathname +'/receive/stop',
        success: function(response) {
        },
        error: function() {
            alert('Error while request...');
        }
    })
}
function uploadMyFile() {
    var filename = $('#inputFileUploadId').val();

    $.ajaxFileUpload
    (
        {
            type: "POST",
            url:'',
            secureuri:false,
            fileElementId:'inputFileUploadId',
            dataType: 'json',
            success: function (data, status)
            {
                console.info('success');
                var result = $('#result');
                result.text(data.url);
                result.attr('hidden',false);
                if(typeof(data.error) != 'undefined')
                {
                    if(data.error != '')
                    {
                        alert(data.error);
                    }else
                    {
                        alert(data.msg);
                    }
                }

            } ,
            done : function(response) {
                console.info('done')
            }
        }
    )


}
$(function() {
    $('#selection').accordion({
        header: "h3",
        collapsible: true
    });

});
//$(document).ready(function() {
//    $('#fileupload').attr('data-url',window.location.pathname +'/upload');
//});

//Uploading a file



$("#upload_link").click(function() {
    document.getElementById('uploaded_file').click();
    return false;
});

jQuery.extend({


    createUploadIframe: function(id, uri)
    {
        //create frame
        var frameId = 'jUploadFrame' + id;
        var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="position:absolute; top:-9999px; left:-9999px"';
        if(window.ActiveXObject)
        {
            if(typeof uri== 'boolean'){
                iframeHtml += ' src="' + 'javascript:false' + '"';

            }
            else if(typeof uri== 'string'){
                iframeHtml += ' src="' + uri + '"';

            }
        }
        iframeHtml += ' />';
        jQuery(iframeHtml).appendTo(document.body);

        return jQuery('#' + frameId).get(0);
    },
    createUploadForm: function(id, fileElementId, data)
    {
        //create form
        var formId = 'jUploadForm' + id;
        var fileId = 'jUploadFile' + id;
        var form = jQuery('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');
        if(data)
        {
            for(var i in data)
            {
                jQuery('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo(form);
            }
        }
        var oldElement = jQuery('#' + fileElementId);
        var newElement = jQuery(oldElement).clone();
        jQuery(oldElement).attr('id', fileId);
        jQuery(oldElement).before(newElement);
        jQuery(oldElement).appendTo(form);



        //set attributes
        jQuery(form).css('position', 'absolute');
        jQuery(form).css('top', '-1200px');
        jQuery(form).css('left', '-1200px');
        jQuery(form).appendTo('body');
        return form;
    },

    ajaxFileUpload: function(s) {
        // TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
        s = jQuery.extend({}, jQuery.ajaxSettings, s);
        var id = new Date().getTime();
        var form = jQuery.createUploadForm(id, s.fileElementId, (typeof(s.data)=='undefined'?false:s.data));
        console.debug(form);
        var io = jQuery.createUploadIframe(id, s.secureuri);
        var frameId = 'jUploadFrame' + id;
        var formId = 'jUploadForm' + id;
        // Watch for a new set of requests
        if ( s.global && ! jQuery.active++ )
        {
            jQuery.event.trigger( "ajaxStart" );
        }
        var requestDone = false;
        // Create the request object
        var xml = {}
        if ( s.global )
            jQuery.event.trigger("ajaxSend", [xml, s]);
        // Wait for a response to come back
        var uploadCallback = function(isTimeout)
        {
            var io = document.getElementById(frameId);
            var myIo = $('#'+frameId);
            try
            {
                if(io.contentWindow)
                {
                    xml.responseText = io.contentWindow.document.body?io.contentWindow.document.body.innerHTML:null;

                    xml.responseXML = io.contentWindow.document.XMLDocument?io.contentWindow.document.XMLDocument:io.contentWindow.document;

                }else if(io.contentDocument)
                {
                    xml.responseText = io.contentDocument.document.body?io.contentDocument.document.body.innerHTML:null;
                    xml.responseXML = io.contentDocument.document.XMLDocument?io.contentDocument.document.XMLDocument:io.contentDocument.document;
                }

            }catch(e)
            {
//                jQuery.handleError(s, xml, null, e);

            }
            var response = io.contentWindow.document.body?io.contentWindow.document.getElementsByTagName('pre')[0].innerHTML:null;
            eval( "data = " + response );
            response = JSON.parse(response);
            console.info('resp\n' +response.url);
            var result = $('#result');
            result.html(response.url);
            result.attr('hidden',false);
//            var data = jQuery.uploadHttpData( xml, s.dataType );
//            console.info(data);
            if ( xml || isTimeout == "timeout")
            {
                requestDone = true;
                var status;
                try {
                    status = isTimeout != "timeout" ? "success" : "error";
                    // Make sure that the request was successful or notmodified
                    if ( status != "error" )
                    {
                        // process the data (runs the xml through httpData regardless of callback)
                        var data = jQuery.uploadHttpData( xml, s.dataType );
                        console.info(data);
                        // If a local callback was specified, fire it and pass it the data
                        if ( s.success )
                            s.success( data, status );


                        // Fire the global callback
                        if( s.global )
                            jQuery.event.trigger( "ajaxSuccess", [xml, s] );
                    }
//                    else
//                        jQuery.handleError(s, xml, status);
                } catch(e)
                {
                    status = "error";
//                    jQuery.handleError(s, xml, status, e);
                }

                // The request was completed
                if( s.global )
                    jQuery.event.trigger( "ajaxComplete", [xml, s] );

                // Handle the global AJAX counter
                if ( s.global && ! --jQuery.active )
                    jQuery.event.trigger( "ajaxStop" );

                // Process result
                if ( s.complete )
                    s.complete(xml, status);

                jQuery(io).unbind();

                setTimeout(function()
                {   try
                {
                    jQuery(io).remove();
                    jQuery(form).remove();

                } catch(e)
                {
//                    jQuery.handleError(s, xml, null, e);
                }

                }, 100) ;

                xml = null

            }
        }
        // Timeout checker
        if ( s.timeout > 0 )
        {
            setTimeout(function(){
                // Check to see if the request is still happening
                if( !requestDone ) uploadCallback( "timeout" );
            }, s.timeout);
        }
        try
        {

            var form = jQuery('#' + formId);
            jQuery(form).attr('action', s.url);
            jQuery(form).attr('method', 'POST');
            jQuery(form).attr('target', frameId);
            if(form.encoding)
            {
                jQuery(form).attr('encoding', 'multipart/form-data');
            }
            else
            {
                jQuery(form).attr('enctype', 'multipart/form-data');
            }
            jQuery(form).submit();

        } catch(e)
        {
//            jQuery.handleError(s, xml, null, e);
        }

        jQuery('#' + frameId).load(uploadCallback   );
//        jQuery('#test').load(uploadCallback   );
        return {abort: function () {}};

    },

    uploadHttpData: function( r, type ) {
        var data = !type;
        data = type == "xml" || data ? r.responseXML : r.responseText;
        // If the type is "script", eval it in global context
        if ( type == "script" )
            jQuery.globalEval( data );
        // Get the JavaScript object, if JSON is used.
        if ( type == "json" )
            eval( "data = " + data );
        // evaluate scripts within html
        if ( type == "html" )
            jQuery("<div>").html(data).evalScripts();

        return data;
    }
})