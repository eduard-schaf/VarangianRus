UI.click = {
  /**
   * Run the click activity.
   */
  run: function() {
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
    } else {
      $EnhancementElement.addClass("click-style-incorrect");
    }

    $EnhancementElement.removeClass("click-style-pointer");

    // prevent execution of further event listeners
    return false;
  }
};