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
