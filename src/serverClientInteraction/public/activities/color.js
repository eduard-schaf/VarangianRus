UI.color = {
    /**
     * Run the color activity.
     *
     * @param {string} topic the selected topic
     */
    run: function (topic) {
        $(".hit").addClass("color-" + topic);
        $("#restore").show();
    }
};