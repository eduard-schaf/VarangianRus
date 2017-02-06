UI.activityHelper = {
  /**
   * Generate multiple choice or cloze exercises.
   *
   * @param {function} createExercise either the mc or cloze createExercise
   * function.
   */
  exerciseHandler: function(createExercise) {
    UI.activityHelper.createExercises(createExercise);

    $("hint").on("click", UI.activityHelper.hintHandler);
  },

  /**
   * Create exercises for the activity.
   *
   * {function} createExercise the function to call to create an
   * exercise for the current activity
   */
  createExercises: function(createExercise) {
    $(".hit").each(function() {
      createExercise($(this));
    });
  },

  /**
   * Deals with the hint in the mc and cloze activities.
   */
  hintHandler: function() {
    UI.activityHelper.processCorrect($(this).prev(), "provided");
  },

  /**
   * Deals with the submission in the mc and cloze activities.
   *
   * @param {object} e the triggered event
   */
  inputHandler: function(e) {
    const $ElementBox = $(e.target);

    if($ElementBox.val().toLowerCase() === $ElementBox.data("answer").toLowerCase()) {
      UI.activityHelper.processCorrect($ElementBox, "correct");
    }
    else {
      UI.activityHelper.processIncorrect($ElementBox);
    }
  },

  /**
   * Process the correct submission.
   *
   * @param {object} $ElementBox the select or input box
   * @param {string} inputStyleType either "correct" or "provided"
   */
  processCorrect: function($ElementBox, inputStyleType) {
    const $EnhancementElement = $ElementBox.parent();
    const elementId = $(".input").index($ElementBox);

    $EnhancementElement.addClass("input-style-" + inputStyleType);
    $EnhancementElement.html($ElementBox.data("answer"));

    UI.activityHelper.jumpTo(elementId);
  },

  /**
   * Jump to the element if it exists and to the first element otherwise.
   *
   * @param {number} elementId the element id we currently at
   */
  jumpTo: function(elementId) {
    const $ElementBoxes = $(".input");
    const $Element = $ElementBoxes.eq(elementId);
    const $FirstElement = $ElementBoxes.eq(0);

    if($Element.length) {
      UI.activityHelper.scrollToCenter($Element);
    }
    else {
      UI.activityHelper.scrollToCenter($FirstElement);
    }
  },

  /**
   * Scroll to the middle of the viewport relative to the element.
   *
   * @param $Element the element to focus and center onto
   */
  scrollToCenter: function($Element) {
    const $Window = $(window);

    $Element.focus();

    $Window.scrollTop($Element.offset().top - ($Window.height() / 2));
  },

  /**
   * Process the incorrect submission.
   *
   * @param {object} $ElementBox the select or input box
   */
  processIncorrect: function($ElementBox) {
    // turns all options, the topmost element after selection included, as red
    $ElementBox.addClass("input-style-incorrect");
    // remove assigned classes to all options from previous selections
    $ElementBox.find("option").removeAttr("class");
    // turn the selected option red
    $ElementBox.find("option:selected").addClass("input-style-incorrect");
    // turn the not selected options black
    $ElementBox.find("option:not(:selected)").addClass("input-style-neutral");
  },

  /**
   * Get the correct answer for the mc and cloze activities.
   *
   * @param {object} $hit the enhancement element
   */
  getCorrectAnswer: function($hit) {
    return $hit.text();
  },

  /**
   * Create the hint visible as "?".
   *
   * @param {object} $hit the enhancement element the select box is designed for
   */
  createHint: function($hit) {
    const $hint = $("<hint>");
    $hint.addClass("style-hint");
    $hint.text("?");
    $hit.append($hint);
  },

  /**
   * Remove activity specific markup.
   */
  restore: function() {
    const $Tokens = $("token");
    const $Hints = $("hint");

    // all
    $Tokens.removeAttr("class");

    $Tokens.each(function() {
      const $Token = $(this);
      $Token.text($Token.data("form"));
    });

    // click
    $Tokens.off("click");

    // mc and cloze
    $(".input").off("change keyup");
    $Hints.off("click");
    $Hints.remove();

    // cloze
    $("baseform").remove();
  }
};