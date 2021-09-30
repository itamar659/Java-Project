var refreshRate = 2000;
var USER_LIST_URL = "userlist";
var LOGOUT_URL = "logout";
var ENGINES_URL = "evolutionengine";

function startEngineOnClick() {
    $("#start-engine").on("click", function(e) {
        setInterval(updateUserInfo, refreshRate);

        $.ajax({
            url: ENGINES_URL,
            timeout: 2000,
            data: {
                action: "start"
            }
        });
    });
}

function updateUserInfo() {
    $.ajax({
        url: ENGINES_URL,
        timeout: 2000,
        data: {
            action: "getUserInfo"
        },
        success: function(res) {
            console.log(res);
            updateLabels(res);
        }
    })
}

function updateLabels(res){
    $("#user-name-label").html(res.username);
    $("#current-fitness-label").html(res.bestFitness);
    $("#generation-label").html(res.currentGeneration);
}

$(function () {
    ajaxLoggedInUsername();
    setCheckBoxChanges();
    validations();
    startEngineOnClick();

    $("#config-form").submit(function () {
        var data = objectifyForm($(this).serializeArray());
        data['action'] = 'update';

        console.log(data);

        $("#engine-details-card").removeClass("hider");

        $.ajax({
            url: ENGINES_URL,
            timeout: 2000,
            action : 'update',
            data: data
        });
        return false;
    });

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

function validations(){
    positiveNumberValidation('#population');
    positiveNumberValidation('#elitism');
    positiveNumberValidation('#interval');
    positiveNumberValidation('#hidden-crossover-Input');

    $('#hidden-selection-Input').on('change', function(e) {
        var isValid;
        var temp = $('#hidden-selection-label').html();
        if(temp === "Top Percent"){
            isValid = this.value >= 0 && this.value <= 100;
        }else{
            isValid = this.value >= 0 && this.value <= 1;
        }
        this.classList.toggle('notValid', !isValid);
        checkFormValidationAndToggleDisabledPropOnSubmitButton();
    });
}

function checkFormValidationAndToggleDisabledPropOnSubmitButton(){
    var arr = $('#config-form *').filter(':input');
    var isDisabled = false;

    $('#config-form input[type="text"]').each(function(){
        var input = $(this);
        if(!input.hasClass('ignored')){
            if(input.hasClass('notValid') || input.val().length === 0){
                $("#submit-config").prop("disabled", true);
                isDisabled = true;
                return false;
            }
        }
    });

    if(!isDisabled){
        $("#submit-config").prop("disabled", false);
    }

}

function positiveNumberValidation(id){
    $(id).on('change', function(e) {
        var isValid = this.value >= 0;

        this.classList.toggle('notValid', !isValid);
        checkFormValidationAndToggleDisabledPropOnSubmitButton();
    });
}

function objectifyForm(formArray) {
    //serialize data function
    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    return returnArray;
}

function setCheckBoxChanges() {
    setCheckBoxChangesHelper('#maxGenCheckBox', '#maxGenText');
    setCheckBoxChangesHelper('#maxFitnessCheckBox', '#maxFitnessText');
    setCheckBoxChangesHelper('#maxTimeCheckBox', '#maxTimeText');
}

function setCheckBoxChangesHelper(obj, objToChange){
    $(obj).change(function(){
        if(this.checked){
            $(objToChange).prop("disabled",false);
        } else {
            $(objToChange).prop("disabled",true);
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

function loadSiteInformation(json) {
    console.log(json);

    $(".dayspan").html(json.evoEngine.problem.days);
    $(".hourspan").html(json.evoEngine.problem.hours);

    createTeachersCard(json.evoEngine.problem.teachers, json.evoEngine.problem.courses);
    createClassesCard(json.evoEngine.problem.classes, json.evoEngine.problem.courses);
    createCoursesCard(json.evoEngine.problem.courses);
    createRulesCard(json.evoEngine.problem.rules);

    //todo: check if the engine configed already inorder to fill the config card
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

function TruncationSelected(){
    $("#hidden-selection-form").removeClass("hider");
    $("#hidden-selection-label").html("Top Percent");
}
function TournamentSelected(){
    $("#hidden-selection-form").removeClass("hider");
    $("#hidden-selection-label").html("pte");
}
function RouletteWheelSelected() {
    $("#hidden-selection-form").removeClass("hider").addClass("hider");
}
function AspectOrientedSelected(){
    $("#hidden-crossover-form").removeClass("hider");
    $("#hidden-crossover-label").html("CuttingPoints")

    $("#hidden-crossoverAspect-form").removeClass("hider");
    $("#hidden-crossoverAspect-label").html("Orientation");
}
function DayTimeOrientedSelected(){
    $("#hidden-crossover-form").removeClass("hider");
    $("#hidden-crossover-label").html("CuttingPoints");

    $("#hidden-crossoverAspect-form").removeClass("hider").addClass("hider");
}

var mutationsAdded = 0
function addMutation(){
    if(mutationsAdded === 0){
        $("#mutation-div-body").removeClass("hider");
    }
    else{
        var d = $("#mutation-div-body").clone().attr('id', 'mutation-div-body' + mutationsAdded).appendTo("#mutation-div");
    }

    mutationsAdded++;
}

function flipperSelected(){

}
