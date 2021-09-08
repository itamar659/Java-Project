$(function() {
    $("#login-form").submit(function () {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            error: function (errorObject) {
                var holder = $("#error-placeholder");
                holder.empty();
                holder.append(errorObject.responseText);
            },
            success: function (nextPageUrl) {
                $("#error-placeholder").empty();
                window.location.href = buildUrlWithContextPath(nextPageUrl);
            }
        });

        return false;
    });
});