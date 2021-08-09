function post(url, json_string, success, beforeSend, complete, error){
    $.ajax({
        type: 'POST',
        contentType: 'application/json;charset=UTF-8',
        url: url,
        data: json_string,
        beforeSend : function () {
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