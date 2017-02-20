UI.mc = {
  /**
   * Run the multiple choice activity.
   */
  run: function() {
    $(".hit:not([data-distractors])").removeClass("hit");

    UI.activityHelper.initExerciseCount();

    UI.activityHelper.exerciseHandler(UI.mc.createExercise);

    $(".input").on("change", UI.activityHelper.inputHandler);
  },

  /**
   * Create an exercise for the enhancement element.
   *
   * @param {object} $hit the enhancement element the exercise is created for
   */
  createExercise: function($hit) {

    const hitText = $hit.text();

    const capType = UI.utils.detectCapitalization(hitText);

    const options = UI.mc.getOptions($hit, hitText, capType);

    UI.mc.createSelectBox(options, $hit);

    UI.activityHelper.createHint($hit);
  },

  /**
   * Gets the options provided by the server in the distractors attribute.
   *
   * @param {object} $hit the enhancement element
   * @param {string} hitText the original text of the enhancement tag
   * @param {number} capType the capitalization type of the original word
   * @returns {Array} the options
   */
  getOptions: function($hit, hitText, capType) {
    const distractors = $hit.data("distractors").split(";");

    UI.utils.shuffleList(distractors);

    return UI.mc.fillOptions(distractors, hitText, capType);
  },

  /**
   * Add the distractor forms to the options.
   * @param {Array} distractors the distractor forms
   * @param {string} hitText the original text of the enhancement tag
   * @param {number} capType the capitalization type of the original word
   */
  fillOptions: function(distractors, hitText, capType) {
    const options = [];
    let j = 0;

    while (j < distractors.length && options.length < 4) {
      const distractor = distractors[j];
      UI.mc.addOption(options, distractor, capType);
      j++;
    }
    UI.mc.addOption(options, hitText, capType);
    UI.utils.shuffleList(options);

    return options;
  },

  /**
   * Add an option with correct capitalization to all options.
   *
   * @param {Array} options all other options
   * @param {string} option the option to add
   * @param {number} capType the capitalization type of the original word
   */
  addOption: function(options, option, capType) {
    options.push(UI.utils.matchCapitalization(option, capType));
  },

  /**
   * Create the select box with distractors as options.
   *
   * @param {Array} options the selection options
   * @param {object} $hit the enhancement element the input box is appended to
   */
  createSelectBox: function(options, $hit) {
    const $SelectBox = $("<select>");
    $SelectBox.addClass("input");

    UI.mc.addSelectOption($SelectBox, " ");

    for (let j = 0; j < options.length; j++) {
      UI.mc.addSelectOption($SelectBox, options[j]);
    }

    $hit.empty();
    $hit.append($SelectBox);
  },

  /**
   * Add an option to the select box.
   *
   * @param {object} $SelectBox the select box to update
   * @param {string} optionText the text of the option to add
   */
  addSelectOption: function($SelectBox, optionText) {
    $SelectBox.append($("<option>").text(optionText));
  }
};