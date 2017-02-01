const socket = io();

$("#enhance").on("click", function () {
    socket.emit("get text", $("#text-select").val());
});

socket.on("add text options", function (textNames) {
    const $TextSelectBox = $("#text-select");

    for(const textKey in textNames) {
        if (Object.prototype.hasOwnProperty.call(textNames, textKey)) {
            const textName = textNames[textKey];
            $TextSelectBox.append($("<option>").val(textKey).text(textName))
        }
    }
});

socket.on("add text", function (text) {
    const $TextContent = $("#text-content");
    $TextContent.empty();
    $TextContent.append(text);
});