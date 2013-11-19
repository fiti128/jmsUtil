/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 17.11.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
function madeAjaxCall(thisId,url,id) {
    var confValue = $('#'+thisId+' option:selected').val();
    console.info(confValue);
    $.ajax({
        type: "post",
        data: ({name : confValue}),
        url: url,
        success: function(response) {
            $('#'+id).text(response.url);
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
