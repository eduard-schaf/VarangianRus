const UI = {
    initTextMenuOptions: function (textNames) {
        const $TextMenu = $("#text-menu");

        for (const textKey in textNames) {
            if (Object.prototype.hasOwnProperty.call(textNames, textKey)) {
                const textName = textNames[textKey];
                $TextMenu.append($("<option>").val(textKey).text(textName))
            }
        }
    },

    initTextMenu: function () {
        $("#text-menu").on("change", UI.getText);
    },

    getText: function () {
        $("#restore").hide();
        IO.socket.emit("get text", $("#text-menu").val());
    },

    addText: function (text) {
        const $TextContent = $("#text-content");
        $TextContent.empty();
        $TextContent.append(text);
    },

    initEnhance: function () {
        $("#enhance").on("click", UI.enhance);
    },

    enhance: function () {
        const topic = $("#topic-menu").val();
        const activity = $("#activity-menu").val();

        if("verbs-aorist-tense" === topic){
            UI.markHits();

            UI[activity].run(topic);
        }
    },

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

    initRestore: function () {
        $("#restore").on("click", UI.restore);
    },

    restore: function () {
        $("token").removeAttr("class");
        $("#restore").hide();
    },

    initialUiState: function () {
        $("#restore").hide();
        $(".menu").val("unselected");
    }
};