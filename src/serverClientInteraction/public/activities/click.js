UI.click = {
  /**
   * Run the click activity.
   */
  run: function() {
    UI.activityHelper.initExerciseCount();

    const $Token = $("token");

    $Token.addClass("click-style-pointer");

    $Token.on("click", UI.click.handler);
  },

  /**
   * Turn correctly clicked hits green and incorrect ones red.
   */
  handler: function() {
    const $EnhancementElement = $(this);

    if($EnhancementElement.hasClass("hit")) {
      $EnhancementElement.addClass("click-style-correct");
      UI.activityHelper.decreaseExerciseCount();
    } else {
      $EnhancementElement.addClass("click-style-incorrect");
    }

    $EnhancementElement.removeClass("click-style-pointer");

    $EnhancementElement.off("click");
  }
};