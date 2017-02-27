const UI = {
  /**
   * Add text options to the text menu.
   *
   * @param {object} textNames contains all text names
   */
  initTextMenuOptions: function(textNames) {
    for(const textKey in textNames) {
      if(Object.prototype.hasOwnProperty.call(textNames, textKey)) {
        const textName = textNames[textKey];
        $("#text-menu").append($("<option>").val(textKey).text(textName))
      }
    }
  },

  /**
   * Initialize the text menu handler.
   */
  initTextMenu: function() {
    $("#text-menu").on("change", UI.getText);
  },

  /**
   * Request to get the text from the server.
   */
  getText: function() {
    UI.hideRestore();
    IO.socket.emit("get text", $("#text-menu").val());
  },

  /**
   * Hide the restore button.
   */
  hideRestore: function() {
    $("#restore").hide();
  },

  /**
   * Add the text to the html page.
   *
   * @param {object} text the text with all data
   */
  addText: function(text) {
    const $TextContent = $("#text-content");
    $TextContent.empty();
    $TextContent.append(text);
  },

  /**
   * Initialize the topic menu handler.
   */
  initTopicMenu: function() {
    $("#topic-menu").on("change", UI.selectSubtopicMenu);
  },

  /**
   * Select the appropriate subtopic depending on topic selection.
   */
  selectSubtopicMenu: function() {
    UI.hideSubtopicMenus();
    $("#" + $(this).val() + "-menu").show();
    UI.updateActivities();
  },

  /**
   * Hide all subtopic menus.
   */
  hideSubtopicMenus: function() {
    $(".subtopic").hide();
  },

  /**
   * Initialize the subtopic menu handler.
   */
  initSubtopicMenus: function() {
    $("#unselected-menu, " +
      "#verbs-menu, " +
      "#nouns-menu, " +
      "#adjectives-menu, " +
      "#pronouns-menu"
    ).on("change", UI.updateActivities);
  },

  /**
   * Update activities when the topic is changed. Enable activities available
   * to the topic and disable the ones that aren't.
   * Activity "Pick an Activity" is always enabled, selected and visible.
   */
  updateActivities: function() {
    const topicData = UI.getTopicData();
    const activitySelectors = UI.fillActivitySelectors();
    const unselected = "unselected";
    const undefinedType = "undefined";

    activitySelectors[unselected].prop("disabled", false).prop("selected", true).show();

    UI.hideEnhance();

    if (
      $(".menu").val() === unselected ||
      typeof topicData === undefinedType) {
      activitySelectors[unselected].next().hide();
    }
    else {
      UI.enableAndShowActivities(topicData, activitySelectors);
    }
  },

  /**
   * Get the topic data from the topic selection.
   *
   * @returns the topic data
   */
  getTopicData: function() {
    const topic = $("#topic-menu").val();
    const topicSelection = $("#" + topic + "-menu").val();
    return UI.data[topicSelection];
  },

  /**
   * Fill up the activity selectors, disable and hide all activities.
   *
   * @returns {Object} all activity selectors
   */
  fillActivitySelectors: function() {
    const activitySelectors = {};

    $("#activity-menu").find("option[value]").each(function() {
      const currentActivity = $(this).val();
      activitySelectors[currentActivity] =
        $("#activity-" + currentActivity).prop("disabled", true).hide();
    });
    return activitySelectors;
  },

  /**
   * Enable and show only activities that are available for the language and
   * topic combination. Show the horizontal separator.
   *
   * @param {string} topicData the data for this topic
   * @param {Array} activitySelectors the object of jquery selectors for each
   * activity
   */
  enableAndShowActivities: function(topicData, activitySelectors) {
    activitySelectors["unselected"].next().show();

    const availableActivities = topicData["activities"].split(",");

    availableActivities.forEach(function(activity) {
      activitySelectors[activity].prop("disabled", false).show();
    });
  },

  /**
   * Initialize the activity menu handler.
   */
  initActivityMenu: function() {
    $("#activity-menu").on("change", UI.toggleEnhance);
  },

  /**
   * Toggle the enhance button depending on activity selection.
   */
  toggleEnhance: function() {
    if($(this).val() === "unselected"){
      UI.hideEnhance();
    }
    else{
      $("#enhance").show();
    }
  },

  /**
   * Hide the enhance button.
   */
  hideEnhance: function() {
    $("#enhance").hide();
  },

  /**
   * Initialize the enhance button handler.
   */
  initEnhance: function() {
    $("#enhance").on("click", UI.enhance);
  },

  /**
   * Enhance the text by first marking the hits and
   * then running the selected topic.
   */
  enhance: function() {
    const topicData = UI.getTopicData();
    const activity = $("#activity-menu").val();

    UI.restore();

    UI.markHits(topicData);

    UI[activity].run();

    $("#restore").show();
  },

  /**
   * Mark the relevant tokens with the class 'hit'.
   *
   * @param {object} topicData all data related to the selected topic
   */
  markHits: function(topicData) {
    const partOfSpeechArray = topicData["part-of-speech"].split(",");
    partOfSpeechArray.forEach(function(partOfSpeech) {
      $("[data-part-of-speech='" + partOfSpeech + "']").each(function() {
        const $Token = $(this);
        const morphology = $Token.data("morphology");
        const searchedFeatures = topicData["features"];
        const featurePositionArray = topicData["feature-positions"].split(",");

        let actualFeatures = "";

        featurePositionArray.forEach(function(featurePosition) {
          if("" !== featurePosition){
            actualFeatures += morphology.charAt(featurePosition);
          }
        });

        if(searchedFeatures === actualFeatures) {
          $Token.addClass("hit");
        }
      });
    });
  },

  /**
   * Initialize the restore button handler.
   */
  initRestore: function() {
    $("#restore").on("click", UI.restore);
  },

  /**
   * Initialize the restore button handler.
   */
  restore: function() {
    UI.activityHelper.restore();

    UI.hideRestore();
  },

  /**
   * The ui state at the beginning.
   */
  initialUiState: function() {
    UI.hideExerciseCounter();
    UI.hideSubtopicMenus();
    UI.updateActivities();
    UI.hideRestore();

    $("#unselected-menu").show();
    $(".menu").val("unselected");
  },


  /**
   * Hide the exercise counter.
   */
  hideExerciseCounter: function() {
    $("#exercise-counter").hide();
  }
};