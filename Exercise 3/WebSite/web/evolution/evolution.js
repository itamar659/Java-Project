var refreshRate = 2000;
var USER_LIST_URL = "userlist";
var LOGOUT_URL = "logout";
var ENGINES_URL = "evolutionengine";
var EVO_ENGINE;

var engineStatus = "idle";

$(function () {
    ajaxLoggedInUsername();
    ajaxUserListInfo();

    setCheckBoxChanges();
    validations();

    startEngineOnClick();
    stopEngineOnClick();
    pauseEngineOnClick();
    resumeEngineOnClick();

    setConfigFormSubmitAction();

    setInterval(ajaxUserListInfo, refreshRate);

    $.ajax({
        url: ENGINES_URL,
        timeout: 2000,
        success: function(json) {
            EVO_ENGINE = json.evoEngine;
            loadSiteInformation(json);
            showBestSolutionOnClick();

            //todo: check if user already configured this problem - If so, fill the configuration form (todo low priority)
            alreadyConfigured(json); // <--
        },
        error: function(errorObj) {
            e = errorObj;
            window.location.href = buildUrlWithContextPath(errorObj.responseText);
        }
    });
})

function alreadyConfigured(response) {
    if (response.evoEngine.crossover != null) {
        $("#engine-details-card").removeClass("hider");

        if (response.evoEngine.isRunning) {
            engineStatus = "started";
            startStopEngineAjax("start");
            buttonsConfig_startEngine();
        } else {
            if (response.evoEngine.isPaused) {
                engineStatus = "paused";
                buttonsConfig_pauseEngine();
            } else {
                engineStatus = "stopped";
                buttonsConfig_stopEngine();
            }
        }

        updateUserInfo();
    }
}

function startEngineOnClick() {
    $("#start-engine").on("click", function(e) {
        startStopEngineAjax("start");
        buttonsConfig_startEngine()
    });
}

function stopEngineOnClick() {
    $("#stop-engine").on("click", function(e) {
        simpleEvolutionEngineAjax("stop");
        engineStatus = "stopped";
        buttonsConfig_stopEngine();
    });
}

function pauseEngineOnClick() {
    $("#pause-engine").on("click", function(e) {
        simpleEvolutionEngineAjax("pause");
        engineStatus = "paused";
        buttonsConfig_pauseEngine();
    });
}

function resumeEngineOnClick() {
    $("#resume-engine").on("click", function(e) {
        startStopEngineAjax("resume");
        buttonsConfig_startEngine();
    });
}

function startStopEngineAjax(action) {
    var startEngineInterval = setInterval(function(){
        updateUserInfo(startEngineInterval)
    }, 1000);

    simpleEvolutionEngineAjax(action);
    engineStatus = "started";
}

function simpleEvolutionEngineAjax(action, successFunc) {
    $.ajax({
        url: ENGINES_URL,
        timeout: 2000,
        data: {
            action: action
        },
        success: successFunc
    });
}

function showBestSolutionOnClick(){
    if(EVO_ENGINE !== undefined){
        $("#show-result-engine").on("click", function(e) {
            var ClassesOrTeachers = $("#selectTarget").find(":selected").text();
            setSelectors(EVO_ENGINE.problem, ClassesOrTeachers);
            solutionOrientedChange(ClassesOrTeachers, $("#selectClassOrTeacher").find(":selected").text());

            $("#result-card").removeClass('hider');

        });
    }
}

function setSelectors(problem, value){
    $('#selectClassOrTeacher').find('option').remove();
    if(value === "Classes"){
        setSelectorHelper(problem.classes);
    }else if(value === "Teachers"){
        setSelectorHelper(problem.teachers);
    }
}

function setSelectorHelper(array){
    var select = $("#selectClassOrTeacher")[0];
    $.each(array, function(index, item){
        var option = document.createElement("option");
        $(option).attr('value', item.name);
        $(option).html(item.name);
        select.appendChild(option);
    })
}

function TeachersSelected(){
    TeacherOrClassesSelectedHelper("Teachers");
}

function ClassesSelected(){
    TeacherOrClassesSelectedHelper("Classes");
}

function TeacherOrClassesSelectedHelper(text){
    $("#class-teacher-label").html(text);
    if(EVO_ENGINE !== undefined){
        setSelectors(EVO_ENGINE.problem, text);
        solutionOrientedChange($("#selectTarget").find(":selected").text(), $("#selectClassOrTeacher").find(":selected").text());
    }
}

//this function gets executed via the html file on change of #selectClassOrTeacher <select> element.
function solutionOrientedChange(type, name){
    $("#stamTitle").html(type + " " + name);
    createSolutionTable(type,name);
}

function createSolutionTable(teacherOrClass, name){
    if(EVO_ENGINE !== undefined){
        var days = EVO_ENGINE.problem.days;
        var hours = EVO_ENGINE.problem.hours;

        $("#best-solution-table-head").empty();

        var tableHead = $("#best-solution-table-head")[0];
        var trHead = document.createElement("tr");
        var thDetail = document.createElement("th");
        thDetail.innerText = "Hours/Days";
        $(thDetail).attr('style', 'width:6%');
        trHead.appendChild(thDetail);
        for(var i = 1; i <= days; i++){
            var thDayNo = document.createElement("th");
            thDayNo.innerText = i;
            trHead.appendChild(thDayNo);
        }
        tableHead.appendChild(trHead);
        if(EVO_ENGINE.bestSolution !== null){
            $("#best-solution-table-body").empty();
            var tableBody = $("#best-solution-table-body")[0];
            for(var hour = 1; hour <= hours; hour++){
                var trBody = document.createElement("tr");
                var thHoursNo = document.createElement("th");
                thHoursNo.innerText = hour;
                trBody.appendChild(thHoursNo);
                for(var day = 1; day <= days; day++){
                    var tdItem = document.createElement("td");
                    tdItem.innerText = getTdDetails(teacherOrClass, name, EVO_ENGINE.bestSolution.lessons, day, hour);
                    trBody.appendChild(tdItem);
                }
                tableBody.appendChild(trBody);
            }
        }
    }
}

function  getTdDetails(teacherOrClass, name, lessons, day, hour){
    var string = "";

    var res = lessons.filter(item => {
        if(teacherOrClass === "Classes"){
            return item.aClass.name === name;
        }else{ // === "Teachers"
            return item.teacher.name === name;
        }
    });

    var res2 = res.filter(item => {
        return item.day === day && item.hour === hour;
    })

    if(res2.length > 0){
        if(teacherOrClass === "Classes"){
            string += "Teacher: " + res2[0].teacher.name + "\nCourse: " + res2[0].course.name;
        }else{
            string += "Class: " + res2[0].aClass.name + "\nCourse: " + res2[0].course.name;
        }
    }

    return string;
}

function updateUserInfo(startEngineInterval) {
    $.ajax({
        url: ENGINES_URL,
        timeout: 2000,
        data: {
            action: "getUserInfo"
        },
        success: function(res) {
            console.log(res);
            updateLabels(res);
            if(engineStatus === "stopped" || res.engineStatus === "COMPLETED"){
                buttonsConfig_stopEngine();
                clearInterval(startEngineInterval);
            } else if(engineStatus === "paused") {
                buttonsConfig_pauseEngine();
                clearInterval(startEngineInterval);
            } else if(engineStatus === "started") {
                buttonsConfig_startEngine();
            }
        }
    })
}

function buttonsConfig_simpleChange(startDisable, pauseDisable) {
    $("#pause-engine").prop("disabled", pauseDisable);
    $("#resume-engine").prop("disabled", !(pauseDisable && startDisable));
    $("#stop-engine").prop("disabled", !startDisable);
    $("#start-engine").prop("disabled", startDisable);
}

function buttonsConfig_startEngine() {
    buttonsConfig_simpleChange(true, false);
    $("#submit-config").prop("disabled", true);
    $("#show-result-engine").prop("disabled", true);
}

function buttonsConfig_pauseEngine() {
    buttonsConfig_simpleChange(true, true);
    $("#submit-config").prop("disabled", false);
    $("#show-result-engine").prop("disabled", false);
}

function buttonsConfig_stopEngine() {
    buttonsConfig_simpleChange(false, true);
    $("#submit-config").prop("disabled", false);
    $("#show-result-engine").prop("disabled", false);
}

function updateLabels(res){
    $("#user-name-label").html(res.username);
    $("#current-fitness-label").html(res.bestFitness);
    $("#engine-status-label").html(res.engineStatus);
}

function setConfigFormSubmitAction(){
    $("#config-form").submit(function () {
        if(validateConfigForm()){
            var data = objectifyForm($(this).serializeArray());
            data['action'] = 'update';

            console.log(data);

            $("#engine-details-card").removeClass("hider");

            $.ajax({
                url: ENGINES_URL,
                timeout: 2000,
                data: data
            });
        }
        return false;
    });
}

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
    });
}

function validateConfigForm(){
    var isDisabled = false;
    var d = $("#maxGenCheckBox").checked;

    if($("input#maxGenCheckBox").is(':checked') || $("input#maxFitnessCheckBox").is(':checked') || $("input#maxTimeCheckBox").is(':checked') ){
        $('#config-form input[type="text"]').each(function(){
            var input = $(this);
            if(!input.hasClass('ignored')){
                if(input.val().length === 0) {
                    input.addClass('notValid');
                    isDisabled = true;
                }else if(input.hasClass('notValid') && input.val().length > 0){
                    input.removeClass('notValid');
                }
            }
        });
        $('#error-submit-label').addClass('hider')
    }else{
        isDisabled = true;
        $('#error-submit-label').removeClass('hider').html("Please check one of the stop Condition");
    }

    return !isDisabled;
}

function positiveNumberValidation(id){
    $(id).on('change', function(e) {
        var isValid = this.value >= 0;

        this.classList.toggle('notValid', !isValid);
    });
}

function objectifyForm(formArray) {
    //serialize data function
    var returnArray = {};
    for (var i = 0; i < formArray.length; i++){
        returnArray[formArray[i]['name']] = formArray[i]['value'];
    }
    var mutationsToConverte = {};
    var filterRetMap = {};
    for(var key in returnArray){
        if(/mutation/i.test(key))
            mutationsToConverte[key] = returnArray[key];
        else
            filterRetMap[key] = returnArray[key];
    }

    arrangeJson(mutationsToConverte);

    filterRetMap['mutations'] = mutationsToConverte.mutation;

    return filterRetMap;
}

function arrangeJson(data){
    var initMatch = /^([a-z0-9]+?)\[/i;
    var first = /^\[[a-z0-9]+?\]/i;
    var isNumber = /^[0-9]$/;
    var bracers = /[\[\]]/g;
    var splitter = /\]\[|\[|\]/g;

    for(var key in data) {
        if(initMatch.test(key)){
            data[key.replace(initMatch,'[$1][')] = data[key];
        }
        else{
            data[key.replace(/^(.+)$/,'[$1]')] = data[key];
        }
        delete data[key];
    }


    for (var key in data) {
        processExpression(data, key, data[key]);
        delete data[key];
    }

    function processExpression(dataNode, key, value){
        var e = key.split(splitter);
        if(e){
            var e2 =[];
            for (var i = 0; i < e.length; i++) {
                if(e[i]!==''){e2.push(e[i]);}
            }
            e = e2;
            if(e.length > 1){
                var x = e[0];
                var target = dataNode[x];
                if(!target){
                    if(isNumber.test(e[1])){
                        dataNode[x] = [];
                    }
                    else{
                        dataNode[x] ={}
                    }
                }
                processExpression(dataNode[x], key.replace(first,''), value);
            }
            else if(e.length == 1){
                dataNode[e[0]] = value;
            }
            else{
                alert('This should not happen...');
            }
        }
    }
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
            $(objToChange).removeClass("ignored");
        } else {
            $(objToChange).prop("disabled",true);
            $(objToChange).addClass("ignored");
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
    $("#hardRuleWeight").html("Hard rule weight - " + json.evoEngine.problem.rules.hardRuleWeight);

    createTeachersCard(json.evoEngine.problem.teachers, json.evoEngine.problem.courses);
    createClassesCard(json.evoEngine.problem.classes, json.evoEngine.problem.courses);
    createCoursesCard(json.evoEngine.problem.courses);
    createRulesCard(json.evoEngine.problem.rules);

    //todo: check if the engine configed already inorder to fill the config card
}

function ajaxUserListInfo() {
    $.ajax({
        url: ENGINES_URL,
        timeout: 2000,
        data: {
            action: "getUsersListInformation"
        },
        success: function(res) {
            console.log(res);
            buildUserListTable(res);
        }
    })
}

function buildUserListTable(res){
    var tableBody = $("#user-list-table-body")[0];
    tableBody.innerHTML = "";

    var i = 0;
    $.each(res, function(index, element){
       var trRow = document.createElement("tr");

        var tdNo = document.createElement("td");
        var tdUsername = document.createElement("td");
        var tdBestFitness = document.createElement("td");
        var tdGeneration = document.createElement("td");
        //var tdConfiguration = document.createElement("td");

        tdNo.innerText = ++i;
        tdUsername.innerText = element.username;
        tdBestFitness.innerText = element.bestFitness;
        tdGeneration.innerText = element.currentGeneration;
        //tdConfiguration.innerText = createConfigSection(element.username);

        // TODO: Display configuration after clicking an individual user

        trRow.appendChild(tdNo);
        trRow.appendChild(tdUsername);
        trRow.appendChild(tdBestFitness);
        trRow.appendChild(tdGeneration);
        //trRow.appendChild(tdConfiguration);

        tableBody.appendChild(trRow);
    });
}

function createConfigSection(username){
    var string = ""
    $.ajax({
        url: ENGINES_URL,
        timeout: 2000,
        data: {
            action: "getEngine"
        },
        success: function(res) {
            console.log(res);
            string += buildConfigSection(res);
        }
    });
    return string;
}

function buildConfigSection(evoEngine){
    var string = "";

    string += "Crossover:" + evoEngine.crossover.name + "\n";
    $.each(evoEngine.crossover.configuration, function (key, value) {
       string += key + ": " + value + "\n";
    });
    string += "Elitism: " + evoEngine.elitism + "\n";
    string += "Population: " + evoEngine.populationSize + "\n";
    string += "Selection:" + evoEngine.selection.name + "\n";
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
    $("#hidden-selection-Input").removeClass("ignored");
    $("#hidden-selection-label").html("Top Percent");
}
function TournamentSelected(){
    $("#hidden-selection-form").removeClass("hider");
    $("#hidden-selection-Input").removeClass("ignored");
    $("#hidden-selection-label").html("pte");
}
function RouletteWheelSelected() {
    $("#hidden-selection-form").removeClass("hider").addClass("hider");
    $("#hidden-selection-Input").removeClass("ignored").addClass("ignored");
}
function AspectOrientedSelected(){
    $("#hidden-crossover-form").removeClass("hider");
    $("#hidden-crossover-label").html("CuttingPoints")

    $("#hidden-crossoverAspect-form").removeClass("hider");
    $("#hidden-crossoverAspect-label").html("Orientation");
    $("#hidden-crossoverAspect-Input").removeClass("ignored");
}
function DayTimeOrientedSelected(){
    $("#hidden-crossover-form").removeClass("hider");
    $("#hidden-crossover-label").html("CuttingPoints");

    $("#hidden-crossoverAspect-form").removeClass("hider").addClass("hider");
    $("#hidden-crossoverAspect-Input").removeClass("ignored").addClass("ignored");
}

var mutationsAdded = 0
function addMutation(){
    var d = $("#mutation-div-body").clone().attr('id', 'mutation-div-body' + mutationsAdded).removeClass("hider").appendTo("#mutation-div");
    $(d.find('#selectMutation')).attr('name', 'mutation[' + mutationsAdded + '][name]').removeClass('ignored');
    $(d.find('#mutation-maxTupples')).attr('name', 'mutation[' + mutationsAdded + '][maxTupples]').removeClass('ignored');
    $(d.find('#mutation-probability')).attr('name', 'mutation[' + mutationsAdded + '][probability]').removeClass('ignored');
    $(d.find('#mutation-component')).attr('name', 'mutation[' + mutationsAdded + '][component]').removeClass('ignored');

    mutationsAdded++;
}

function FlipperSelected(obj){
    var input = $($(obj).siblings('#mutation-items-flipper').children("#mutation-component"));
    var label = $($(obj).siblings('#mutation-items-flipper').children("#component-label"));

    input.removeClass('hider');
    input.removeClass('ignored');

    label.removeClass('hider');
    label.removeClass('ignored');
}

function SizerSelected(obj){
    var input = $($(obj).siblings('#mutation-items-flipper').children("#mutation-component"));
    var label = $($(obj).siblings('#mutation-items-flipper').children("#component-label"));

    input.addClass('hider');
    input.addClass('ignored');

    label.addClass('hider');
    label.addClass('ignored');
}
