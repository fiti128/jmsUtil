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
    var iframeElement = document.getElementById('my_iframe');
    var xml = iframeElement.contentWindow.document.getElementById('xmlId').innerText;;
    $.ajax({
        type: "post",
        data: ({factoryName : $('#senderHost').val(),
            queueName : $('#senderPort').val(),
            correlationId : $('#senderCorrelationId').val(),
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
            $('#start').attr('style','display: none');
    $.ajax({
        type: "post",
        data: ({factoryName : $('#receiverHost').val(),
            queueName : $('#receiverPort').val(),
            correlationId : $('#receiverCorrelationId').val(),
            timeout : $('#delayId').val()*1000}),
        url: window.location.pathname +'/receive/start',
        success: function(response) {
            var xml = response.messageBody;
            $('#start').attr('style','display: block');
            if (xml.length > 1) {
                 $('#receivedXml').text(xml).attr('hidden',false);
            }


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
        collapsible: true,
        active:false
    });

});
function updateUid() {
    $.ajax({
        type: 'post',
        data: ({xml : $('#xmlId').text()}),
        url: window.location.pathname +'/updateUid',
        success: function(response) {
            var newXml = response.xmlText;
            newXml.replace(/\r\n/g,'\n');
            $('#xmlId').text(newXml);
            $('#updateResult').attr('style','display: block');
        },
        error: function() {
            alert('Error while request...');
        }
    })  ;
}

function resizeIt() {
    var element = $('#xmlId');
    var str = element.text();
    var cols =  element.attr('cols');
    console.info(cols);
    var lineCount = 0 ;
    var rows = str.split('\n');
    rows.forEach(function(entry) {
        var extra =  Math.floor(entry.length/cols);
        console.info(extra);
        lineCount += 1 + extra;
    } );

     element.attr('rows',lineCount);
}