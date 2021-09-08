var USER_LIST_URL = "userlist";

$(function() {
    ajaxLoggedInUsername();
    ajaxUserList();

    setLoginSubmitButtonEvent();
});

function setLoginSubmitButtonEvent() {
    $("#login-form").submit(function () {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            success: function (nextPageUrl) {
                $("#error-placeholder").empty();
                window.location.href = buildUrlWithContextPath(nextPageUrl);
            },
            error: function (errorObject) {
                $("#error-placeholder").empty().append(errorObject.responseText);
            }
        });

        return false;
    });
}

function ajaxLoggedInUsername() {
    $.ajax({
        url: USER_LIST_URL,
        data: {
            action: "username"
        },
        success: function(username) {
            if (username != null) {
                window.location.href = buildUrlWithContextPath("home.html");
            }
        }
    });
}

function ajaxUserList() {
    $.ajax({
        url: USER_LIST_URL,
        data: {
            action: "userList"
        },
        success: setUsersCounter,
        error: function() {
            setUsersCounter([]);
        }
    });
}

function setUsersCounter(users) {
    $.each($(".users-counter"), function(index, element) {
        element.innerText = (users || []).length;
    });
}