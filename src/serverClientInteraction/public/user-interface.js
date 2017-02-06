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
    $("#restore").hide();
    IO.socket.emit("get text", $("#text-menu").val());
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
    const topic = $("#topic-menu").val();
    const activity = $("#activity-menu").val();

    UI.restore();

    UI.markHits(UI.data[topic]);

    UI[activity].run();

    $("#restore").show();
  },

  /**
   * Mark the relevant tokens with the class 'hit'.
   *
   * @param {object} topicData all data related to the selected topic
   */
  markHits: function(topicData) {
    $("[data-part-of-speech='" + topicData["part-of-speech"] + "']").each(function() {
      const $Token = $(this);
      const morphology = $Token.data("morphology");
      const searchedFeature = topicData["feature"];
      const actualFeature = morphology.charAt(topicData["feature-position"]);

      if(searchedFeature === actualFeature) {
        $Token.addClass("hit");
      }
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

    $("#restore").hide();
  },

  /**
   * The ui state at the beginning.
   */
  initialUiState: function() {
    $("#restore").hide();
    $(".menu").val("unselected");
  }
};