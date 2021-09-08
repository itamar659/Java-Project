var USER_LIST_URL = buildUrlWithContextPath("userlist");

$(function() {
    // TODO: Check if the user have a session already. if so, skip the login page.

    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            setUsersCounter(users);
        },
        error: function(object) {
            setUsersCounter([]);
        }
    });

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

function setUsersCounter(users) {
    $.each($(".users-counter"), function(index, element) {
        element.innerText = (users || []).length;
    });
}