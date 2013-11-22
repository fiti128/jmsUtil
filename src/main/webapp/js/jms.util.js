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
    var jmsConfigurationId = 1;
    var delay =  $('delayId').text();
    if (!isNaN(delay)) {
        interval = delay;
    }
    $.ajax({
        type: 'post',
        url: 'receive',
        data: ({receiveConfigurationId : jmsConfigurationId}),
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
            htmlElement.text(xmlBody);

        },
        error: function() {
            alert('Error while request...');
        }
    });

}
function sendMessage() {
    var jmsConfigurationId = 1;
    var xml = $('#result').text();
    $.ajax({
        type: "post",
        data: ({sendConfigurationId : jmsConfigurationId, xmlString : xml}),
        url: 'send',
        success: function(response) {
            $('#sendResult').text(response).attr('hidden',false);
                },
        error: function() {
            alert('Error while request...');
        }
    })
}
//$(document).ready(receiveJmsMessage());
setInterval('receiveJmsMessage()',interval);

