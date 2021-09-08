var refreshRate = 2000;
var USER_LIST_URL = buildUrlWithContextPath("userlist");

$(function () {
    ajaxUsersList();
    setInterval(ajaxUsersList, refreshRate);
})

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        },
        error: function(object) {
            console.log("Couldn't pull the users from the server. Sent: " + object);
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