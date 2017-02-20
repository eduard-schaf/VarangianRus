UI.cloze = {
  /**
   * Run the cloze activity.
   */
  run: function() {
    UI.activityHelper.initExerciseCount();

    UI.activityHelper.exerciseHandler(UI.cloze.createExercise);

    $(".input").on("keyup", UI.cloze.handler);

    UI["virtual-keyboard"].init();
  },

  /**
   * Create an exercise for the enhancement element.
   *
   * @param {object} $hit the enhancement element the exercise is created for
   */
  createExercise: function($hit) {
    const answers = UI.activityHelper.getCorrectAnswers($hit);

    UI.cloze.createInputBox(answers, $hit);

    UI.activityHelper.createHint($hit);

    UI.cloze.addBaseform($hit);
  },

  /**
   * Create the input box.
   *
   * @param {string} answers the correct answer
   * @param {object} $hit the enhancement element the input box is appended to
   */
  createInputBox: function(answers, $hit) {
    // create input box
    const $InputBox = $("<input>");
    $InputBox.addClass("input");
    $InputBox.addClass("cloze-style-input");

    $InputBox.attr("type", "text");
    // average of 10 px per letter (can fit 10 x "м" with a width of 110)
    $InputBox.css("width", (answers[0].length * 10) + "px");

    $hit.empty();
    $hit.append($InputBox);
  },

  /**
   * Add the baseform (lemma) next to the input box.
   *
   * @param {object} $hit the enhancement element containing the input box
   */
  addBaseform: function($hit) {
    const $baseform = $("<baseform>");
    $baseform.addClass("cloze-style-baseform");
    const lemmaform = $hit.data("lemma");
    if(lemmaform) {
      $baseform.text(" (" + lemmaform + ")");
      $hit.append($baseform);
    }
  },

  /**
   * Call the input handler when the enter key is released.
   *
   * @param {object} e the triggered event
   */
  handler: function(e) {
    const code = e.which;
    if(code === 13) {
      UI.activityHelper.inputHandler(e);
    }
  },
};