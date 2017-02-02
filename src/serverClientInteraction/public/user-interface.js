const UI = {
    /**
     * Add text options to the text menu.
     *
     * @param {object} textNames contains all text names
     */
    initTextMenuOptions: function (textNames) {
        const $TextMenu = $("#text-menu");

        for (const textKey in textNames) {
            if (Object.prototype.hasOwnProperty.call(textNames, textKey)) {
                const textName = textNames[textKey];
                $TextMenu.append($("<option>").val(textKey).text(textName))
            }
        }
    },

    /**
     * Initialize the text menu handler.
     */
    initTextMenu: function () {
        $("#text-menu").on("change", UI.getText);
    },

    /**
     * Request to get the text from the server.
     */
    getText: function () {
        $("#restore").hide();
        IO.socket.emit("get text", $("#text-menu").val());
    },

    /**
     * Add the text to the html page.
     *
     * @param {object} text the text with all data
     */
    addText: function (text) {
        const $TextContent = $("#text-content");
        $TextContent.empty();
        $TextContent.append(text);
    },

    /**
     * Initialize the enhance button handler.
     */
    initEnhance: function () {
        $("#enhance").on("click", UI.enhance);
    },

    /**
     * Enhance the text by first marking the hits and
     * then running the selected topic.
     */
    enhance: function () {
        const topic = $("#topic-menu").val();
        const activity = $("#activity-menu").val();

        if("verbs-aorist-tense" === topic){
            UI.markHits();

            UI[activity].run(topic);
        }
    },

    /**
     * Mark the relevant tokens with the class 'hit'.
     */
    markHits: function () {
        $("[data-part-of-speech='V-']").each(function () {
            const $Token =$(this);
            const morphology = $Token.data("morphology");
            const tense = morphology.charAt(2);

            if("a" === tense){
                $Token.addClass("hit");
            }
        });
    },

    /**
     * Initialize the restore button handler.
     */
    initRestore: function () {
        $("#restore").on("click", UI.restore);
    },

    /**
     * Initialize the restore button handler.
     */
    restore: function () {
        $("token").removeAttr("class");
        $("#restore").hide();
    },

    /**
     * The ui state at the beginning.
     */
    initialUiState: function () {
        $("#restore").hide();
        $(".menu").val("unselected");
    }
};