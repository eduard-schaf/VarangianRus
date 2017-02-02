const IO = {
    init: function () {
        IO.socket = io();
        IO.bindEvents();
    },

    bindEvents: function () {
        IO.socket.on("init ui", IO.onInitUi);
        IO.socket.on("add text", IO.onAddText);
    },

    onInitUi: function (textNames) {
        UI.initTextMenuOptions(textNames);

        UI.initTextMenu();

        UI.initEnhance();

        UI.initRestore();

        UI.initialUiState();
    },

    onAddText: function (text) {
        UI.addText(text);
    }
};

IO.init();