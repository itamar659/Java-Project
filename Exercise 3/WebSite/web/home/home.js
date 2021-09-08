var refreshRate = 2000;
var USER_LIST_URL = "userlist";
var LOGOUT_URL = "logout";

$(function () {
    ajaxLoggedInUsername();
    ajaxUsersList();
    setInterval(ajaxUsersList, refreshRate);
    formUploadFileSetEvents();
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

function refreshUsersList(users) {
    console.log("pulled new user list: " + users);
    $("#users-list").empty();

    $.each($(".users-counter"), function(index, element) {
        element.innerText = (users || []).length;
    });

    $.each(users || [], function(index, username) {
        $('<div class="user">' +
            '<span id="user-name">' +
            username +
            '</span>' +
            '</div>')
            .appendTo($("#users-list"));
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
            window.location.href = buildUrlWithContextPath(relocation.responseText);
        }
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

function formUploadFileSetEvents() {
    // $(".upload-button")[0].onclick = function () {
    //     // this.classList.remove("highlight");
    //
    //     // 2 lines of bugs:
    //     // $("#file-input")[0].value = "";
    //     // $(".upload-button")[0].disabled = true;
    //
    //     // $(".file-path").empty();
    //
    //     return false;
    // };

    $("#file-input")[0].onchange = function () {
        // $(".upload-button")[0].classList.add("highlight");
        $(".upload-button")[0].disabled = false;
        $(".file-path")[0].innerText = "File Name: " + this.files[0].name;

    };

    $("#upload-form").submit(function () {

        var formData = new FormData();
        var file = $("#file-input")[0].files[0];
        formData.append("file", file);

        $.ajax({
            method: 'POST',
            data: formData,
            url: this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 10000,
            success: function (r) {
                console.log(r);
                $("#file-input")[0].value = "";
                $(".upload-button")[0].disabled = true;
            },
            error: function (e) {
                console.error("Failed to get result from server " + e);
                $("#file-input")[0].value = "";
                $(".upload-button")[0].disabled = true;
            }
        });
        return false;
    });
}