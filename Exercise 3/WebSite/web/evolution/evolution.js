var refreshRate = 2000;
var USER_LIST_URL = "userlist";
var LOGOUT_URL = "logout";
var ENGINES_URL = "evolutionengine";

$(function () {
    ajaxLoggedInUsername();

    $.ajax({
        url: ENGINES_URL,
        success: loadSiteInformation,
        error: function(errorObj) {
            e = errorObj;
            window.location.href = errorObj.responseText;
        }
    });
})

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
        }
    });
}

function changeTab(evt, type) {
    $.each($(".tab-content"), function (index, content) {
        content.style.display = "none";
    });

    $.each($(".tab-link"), function (index, tab) {
        tab.classList.remove("active-tab");
    });

    $("#" + type)[0].style.display = "block";
    evt.currentTarget.classList.add("active-tab");
}

function loadSiteInformation(json) {
    /* Json object information:
     * Same as the engine.......
     */

    window.e = json;
    console.log("Finish");
}