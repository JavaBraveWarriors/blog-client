$(document).on("click", "#add_tag", function () {
    event.preventDefault();
    loadModal();
});

$(document).on("click", "#send_tag", function () {
    event.preventDefault();
    sendTag('result_form', 'tag')
});

function sendTag(result_form, ajax_form) {
    $.ajax({
        url: "/blog/tags/add",
        type: "POST",
        dataType: "html",
        data: $("#" + ajax_form).serialize(),
        success: function (data) {
            $('#modalTagForm').modal('toggle');

            $('#modalTagForm').on('hidden.bs.modal', function () {
                $('#modal').html(data);
                $('#centralModalSuccess').modal();
            });

        },
        error: function (response) {
        }
    });
}

function loadModal() {
    $.ajax({
        url: "/blog/tags/add",
        type: "GET",
        success: function (data) {
            if (data !== null) {
                $('#modal').html(data);
                $('#modalTagForm').modal();
            }
        },
        error: function (jqXHR, testStatus, errorThrown) {
            this.alert(errorThrown);
        }
    });
}