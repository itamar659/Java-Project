var refreshRate = 2000;
var USER_LIST_URL = buildUrlWithContextPath("userlist");
var LOGOUT_URL = "logout";

$(function () {
    ajaxLoggedInUsername();
    ajaxUsersList();
    setInterval(ajaxUsersList, refreshRate);
})

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        data: {
            action: "userList"
        },
        success: refreshUsersList,
        error: function(object) {
            console.log("Couldn't pull the users from the server. Sent: " + object);
        }
    });
}

function ajaxLoggedInUsername() {
    $.ajax({
        url: USER_LIST_URL,
        data: {
            action: "username"
        },
        success: function(username) {
            $.each($(".username-logged-in") || [], function(index, element) {
                element.innerHTML = username;
            });
        },
        error: function(relocation) {
            console.log(relocation);
            window.location.href = buildUrlWithContextPath(relocation.responseText);
        }
    });
}

function refreshUsersList(users) {
    console.log("pulled new user list: " + users);
    $("#users-list").empty();

    $.each($(".users-counter"), function(index, element) {
        element.innerText = (users || []).length;
    });

    $.each(users || [], function(index, username) {
        $('<li>' + username + '</li>')
            .appendTo($("#users-list"));
    });
}

function logout() {
    $.ajax({
        url: LOGOUT_URL,
        success: function(url) {
            window.location.href = url;
        },
        error: function() {
            window.location.href = "/";
        }
    });
}