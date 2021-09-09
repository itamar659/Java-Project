var refreshRate = 2000;
var USER_LIST_URL = "userlist";
var LOGOUT_URL = "logout";
var PROBLEMS_URL = "problems";

$(function () {
    ajaxLoggedInUsername();
    ajaxUsersList();
    ajaxProblemList();
    formUploadFileSetEvents();

    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxProblemList, refreshRate);
})

// TODO - Move to different more general js file
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

// TODO - Move to different more general js file
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

function ajaxProblemList() {
    $.ajax({
        url: PROBLEMS_URL,
        success: refreshProblemList,
        error: function(object) {
            console.log("Couldn't pull the problems from the server. Sent: ");
            console.log(object);
        }
    });
}

function refreshProblemList(problems) {
    var tableBody = $(".problems-table-body")[0];

    tableBody.innerHTML = "";
    $.each(problems || [], function(index, problem) {
        console.log(problem);
        var row = document.createElement("tr");

        var tdUploader = document.createElement("td");
        var tdProblemInfo = document.createElement("td");
        var tdRules = document.createElement("td");
        var tdUsers = document.createElement("td");
        var tdBestFitness = document.createElement("td");

        tdUploader.innerText = problem.uploader;
        tdProblemInfo.appendChild(createSectionProblemInfo(problem));
        tdRules.appendChild(createSectionRulesInfo(problem));
        console.log(row);
        tdUsers.innerText = problem.users.length;
        tdBestFitness.innerText = problem.bestFitness;


        row.appendChild(tdUploader);
        row.appendChild(tdProblemInfo);
        row.appendChild(tdRules);
        row.appendChild(tdUsers);
        row.appendChild(tdBestFitness);


        tableBody.appendChild(row);
    });
}

function createSectionProblemInfo(problem) {
    var sectionInfo = $(document.createElement("section")).addClass("grid")[0];

    var tdDays = $(document.createElement("label")).text("Days:" + problem.days)[0];
    var tdHours = $(document.createElement("label")).text("Hours:" + problem.hours)[0];
    var tdTeachers = $(document.createElement("label")).text("Teachers:" + problem.teachers)[0];
    var tdClasses = $(document.createElement("label")).text("Classes:" + problem.classes)[0];
    var tdCourses = $(document.createElement("label")).text("Courses:" + problem.courses)[0];

    sectionInfo.appendChild(tdDays);
    sectionInfo.appendChild(tdHours);
    sectionInfo.appendChild(tdTeachers);
    sectionInfo.appendChild(tdClasses);
    sectionInfo.appendChild(tdCourses);

    return sectionInfo;
}

function createSectionRulesInfo(problem) {
    var sectionInfo = $(document.createElement("section")).addClass("grid")[0];

    var tdHard = $(document.createElement("label")).text("Hard rules:" + problem.hardRules)[0];
    var tdSoft = $(document.createElement("label")).text("Soft rules:" + problem.softRules)[0];

    sectionInfo.appendChild(tdSoft);
    sectionInfo.appendChild(tdHard);

    return sectionInfo;
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
        alert("Upload the file successfully!");
    }
}