var refreshRate = 2000;
var USER_LIST_URL = "userlist";
var LOGOUT_URL = "logout";
var ENGINES_URL = "engines";

$(function () {
    ajaxLoggedInUsername();
    ajaxUsersList();
    ajaxEngineList();
    formUploadFileSetEvents();

    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxEngineList, refreshRate);
})

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        data: {
            action: "userList"
        },
        success: refreshUsersList,
        error: function(object) {
            console.log("Couldn't pull the users from the server. Sent: ");
            console.log(object);
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
            '<label id="user-name" class="cut-text">' +
            username +
            '</label>' +
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

function ajaxEngineList() {
    $.ajax({
        url: ENGINES_URL,
        success: refreshEngineList,
        error: function(object) {
            console.log("Couldn't pull the engines from the server. Sent: ");
            console.log(object);
        }
    });
}

function refreshEngineList(engines) {
    window.e = engines;

    // TODO - add the engines to the table
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
    $("#file-input")[0].onchange = function () {
        $(".error-display")[0].innerHTML = "";
        $(".upload-button")[0].disabled = false;
        $(".file-path")[0].innerText = "File Name: " + this.files[0].name;
    };

    $("#upload-form").submit(function () {
        var formData = new FormData();
        var file = $("#file-input")[0].files[0];
        if (file === undefined) {
            $(".error-display")[0].innerText = "Please choose a file and don't press 'cancel'";
            $(".file-path").empty();
            $(".upload-button")[0].disabled = true;
            return false;
        }
        formData.append("file", file);

        $.ajax({
            method: 'POST',
            data: formData,
            url: this.action,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            timeout: 4000,
            success: validateFile
        });
        return false;
    });
}

function validateFile(jsonResponse) {
    // isFileCorrupted - indicate if the file successfully loaded or not.
    // errorMessage - the message what's the problem in the file.
    if (jsonResponse.isFileCorrupted) {
        $(".error-display")[0].innerText = jsonResponse.errorMessage;
    } else {
        $(".error-display")[0].innerHTML = "";
        $("#file-input")[0].value = "";
        $(".file-path").empty();
        $(".upload-button")[0].disabled = true;
    }
}