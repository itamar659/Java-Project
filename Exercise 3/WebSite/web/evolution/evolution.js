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
            window.location.href = buildUrlWithContextPath(errorObj.responseText);
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
            window.location.href = buildUrlWithContextPath(url);
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

    /*
        evoEngine {
            bestSolution: null,
            crossover: null,
            currentGeneration: 0,
            elitism: 0,
            historyGeneration2Fitness: {},
            isPaused: false,
            isRunning: false,
            mutations: [],
            population: null,
            populationSize: 0,
            problem:{
                        classes: (5) [{…}, {…}, {…}, {…}, {…}],
                        courses: (7) [{…}, {…}, {…}, {…}, {…}, {…}, {…}],
                        days: 6,
                        hours: 8,
                        rules: {rules: Array(4), hardRuleWeight: 60},
                        teachers: (6) [{…}, {…}, {…}, {…}, {…}, {…}],
                    }
            [[Prototype]]: Object,
            selection: null,
            stopConditions: {},
            updateGenerationInterval: 100,
        }
     */
    console.log(json.evoEngine.problem);

    $(".dayspan").html(json.evoEngine.problem.days);
    $(".hourspan").html(json.evoEngine.problem.hours);

    createTeachersCard(json.evoEngine.problem.teachers, json.evoEngine.problem.courses);
    createClassesCard(json.evoEngine.problem.classes, json.evoEngine.problem.courses);
    createCoursesCard(json.evoEngine.problem.courses);
    createRulesCard(json.evoEngine.problem.rules);

}

function createRulesCard(rules){
    var tableBody = $(".rulesInfo-table-body")[0];
    tableBody.innerHTML = "";

    $("#hardRuleWeight").innerText = rules.hardRulesWeight;

    $.each(rules.rules, function (index, element){
        var trRow = document.createElement("tr");
        var tdNo = document.createElement("td");
        var tdName = document.createElement("td");
        var tdType = document.createElement("td");

        tdNo.innerText = index + 1;
        tdName.innerText = element.name;
        tdType.innerText = element.type;

        trRow.appendChild(tdNo);
        trRow.appendChild(tdName);
        trRow.appendChild(tdType);
        tableBody.appendChild(trRow);
    });
}

function createTeachersCard(teachers, courses){
    var tableBody = $(".teachersInfo-table-body")[0];
    tableBody.innerHTML = "";

    $.each(teachers, function (index, element){
        var trRow = document.createElement("tr");

        var tdID = document.createElement("td");
        var tdName = document.createElement("td");
        var tdTeach = document.createElement("td");

        tdID.innerText = element.id;
        tdName.innerText = element.name;
        tdTeach.innerText = createSectionTeachInfo(element.teachesCourses, courses);

        trRow.appendChild(tdID);
        trRow.appendChild(tdName);
        trRow.appendChild(tdTeach);

        tableBody.appendChild(trRow);
    });
}

function createCoursesCard(courses){
    var tableBody = $(".coursesInfo-table-body")[0];
    tableBody.innerHTML = "";

    $.each(courses, function (index, element){
        var trRow = document.createElement("tr");
        var tdID = document.createElement("td");
        var tdName = document.createElement("td");

        tdID.innerText = element.id;
        tdName.innerText = element.name;

        trRow.appendChild(tdID);
        trRow.appendChild(tdName);
        tableBody.appendChild(trRow);
    });
}

function createClassesCard(classes, courses){
    var tableBody = $(".classesInfo-table-body")[0];
    tableBody.innerHTML = "";

    $.each(classes, function (index, element){
        var trRow = document.createElement("tr");

        var tdID = document.createElement("td");
        var tdName = document.createElement("td");
        var tdCourses2Hours = document.createElement("td");
        var tdTotalHours = document.createElement("td");

        tdID.innerText = element.id;
        tdName.innerText = element.name;
        tdCourses2Hours.innerText = createSectionClassesInfo(element.courseID2Hours, courses);
        tdTotalHours.innerText = element.totalHours;
        trRow.appendChild(tdID);
        trRow.appendChild(tdName);
        trRow.appendChild(tdCourses2Hours);
        trRow.appendChild(tdTotalHours);

        tableBody.appendChild(trRow);
    });
}

function createSectionTeachInfo(teachesCourses, courses) {
    var string = "";

    $.each(teachesCourses, function(index, element){
        string += courses.find(item => item.id === element).name;
        if(index + 1 !== teachesCourses.length){
            string += ", ";
        }
    })

    return string;
}

function createSectionClassesInfo(courseID2Hours, courses){
    var string = "";

    $.each(courseID2Hours, function(index, element){
        string += courses.find(item => item.id === index).name;
        string += " : ";
        string += element;
        if(index + 1 !== courseID2Hours.length && index % 2 === 0){
            string += "\n";
        }
        else{
            string += ", ";
        }
    })

    return string;
}