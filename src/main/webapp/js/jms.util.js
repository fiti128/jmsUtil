/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 17.11.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
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

$(function () {
    $('#fileupload').fileupload({
        dataType: 'json',
        success: function(response) {
            var result = $('#result');
            result.text(response.url);
            result.attr('hidden',false);
        }
});
});
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
    $('#start').attr('style','display: none');
    $('#stop').attr('style','display: block');
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
              setTimeout(startReceivingMessages(),3000);
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
//$(function() {
//    $('#selection').accordion({
//        header: "h2",
//        collapsible: true
//    });
//
//});
//$(document).ready(function() {
//    $('#fileupload').attr('data-url',window.location.pathname +'/upload');
//});

