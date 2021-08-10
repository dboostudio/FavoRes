var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

function post(url, json, success, beforeSend, complete, error){
    $.ajax({
        type: 'POST',
        contentType: 'application/json;charset=UTF-8',
        url: url,
        data: JSON.stringify(json),
        beforeSend : function (xhr) {
            xhr.setRequestHeader(header, token)
            if('function' === typeof (beforeSend)){
                beforeSend();
            }
        },
        success: function (result) {
            if ('function' === typeof (success)) {
                success(result);
            }
        },
        complete: function () {
            if ('function' === typeof (complete)) {
                complete();
            }
        },
        error: function (xhr, textStatus, e) {
            if ('function' === typeof (error)) {
                error(JSON.parse(xhr.responseText), textStatus, e);
            }
            else {
                console.log(xhr, textStatus, e);
            }
        }
    })
}