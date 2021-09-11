var refreshRate = 2000;
var USER_LIST_URL = "userlist";
var LOGOUT_URL = "logout";
var ENGINES_URL = "evolutionengine";

$(function () {
    ajaxLoggedInUsername();

    $.ajax({
        url: ENGINES_URL,
        timeout: 2000,
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
    console.log(json);

    /* Json object very simple information:
     * almost each one of the following attribute has properties.
     * it's better to view in console which value have what.
     *
     * Every one of them also saved as json. parse first.
     * generation
     * generationInterval
     * bestSolution
     * stopConditions
     * problem
     * populationSize
     * elitism
     * selection
     * crossover
     * mutations
     */

    // var grid = $(".teachers-tab")[0];
    // // grid.innerText = "";
    //
    // var problem = JSON.parse(json.problem);
    // $.each(problem.teachers, function(index, teacher) {
    //     grid.appendChild(extractInfo(teacher));
    // });
}

// <div className="card">
//     <div className="card-information">
//         <label>Name: xxx</label>
//         <label>ID: 123</label>
//         <label>Hours Working: 200</label>
//         <label>Teaches:</label>
//         <ul>
//             <li>
//                 math - 1
//             </li>
//         </ul>
//     </div>
// </div>

function extractInfo(teacher) {
    // var divContainer = document.createElement("div");
    // divContainer.classList.add("card");
    //
    // var divInformative = document.createElement("div");
    // divInformative.classList.add("card-information");
    //
    // var labelName = document.createElement("Label");
    // var labelID = document.createElement("Label");
    // var labelHoursWorking = document.createElement("Label");
    // var labelTeaches = document.createElement("Label");
    // var ul = document.createElement("ul");
    //
    // labelName.innerText = "name: " + teacher.name;
    // labelID.innerText = "ID: " + teacher.id;
    // labelHoursWorking.innerText = "Working Hours: " + teacher.workingHours;
    // labelTeaches.innerText = "Teaches:";
    //
    // $.each(teacher.teachesCourses, function(index, courseID) {
    //     var li = document.createElement("li");
    //     li.innerText = courseID;
    //
    //     ul.appendChild(li);
    // });
    //
    // divInformative.appendChild(labelName);
    // divInformative.appendChild(labelID);
    // divInformative.appendChild(labelHoursWorking);
    // divInformative.appendChild(labelTeaches);
    // divInformative.appendChild(ul);
    //
    // divContainer.appendChild(divInformative);
    //
    // return divContainer
}