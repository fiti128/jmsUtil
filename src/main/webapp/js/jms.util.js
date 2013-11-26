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

$(function() {
    $('#selection').accordion({
        header: "h3",
        collapsible: true
    });

});
