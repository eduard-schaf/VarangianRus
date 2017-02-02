const IO = {
    /**
     * Initialize the socket and bind events.
     */
    init: function () {
        IO.socket = io();
        IO.bindEvents();
    },

    /**
     * Handle messages from the server.
     */
    bindEvents: function () {
        IO.socket.on("init ui", IO.onInitUi);
        IO.socket.on("add text", IO.onAddText);
    },

    /**
     * The server requested to initialize the ui.
     *
     * @param {object} textNames contains all text names
     */
    onInitUi: function (textNames) {
        UI.initTextMenuOptions(textNames);

        UI.initTextMenu();

        UI.initEnhance();

        UI.initRestore();

        UI.initialUiState();
    },

    /**
     * The server requested to add the text
     * to the html page.
     *
     * @param {object} text the text with all data
     */
    onAddText: function (text) {
        UI.addText(text);
    }
};

IO.init();