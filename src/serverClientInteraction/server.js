//file system tools
const fs = require("fs");
//jquery to parse HTML
const jquery = require("jquery");
//create server side HTML so we can use jquery
const cheerio = require("cheerio"),
  $ = cheerio.load("<h2 class='title'>Server Side</h2>");
$.html();

const texts = {};
const textNames = {};

getTexts();

/**
 * Reads preprocessed HTML texts, converts them into DOM objects
 * and saves them globally on the server.
 * Called directly on server start.
 */
function getTexts() {
  const textsDirectory = "texts/";
  const textFiles = fs.readdirSync(textsDirectory);
  for(const text in textFiles) {
    if(Object.prototype.hasOwnProperty.call(textFiles, text)) {
      const filename = textFiles[text];
      const parsedText = fs.readFileSync(textsDirectory + filename, "utf8");
      const name = filename.replace(".html", "");
      texts[name] = parsedText;
      textNames[name] = $("#text-title", parsedText).text();
    }
  }
}

/**
 * This function is called by index.js to initialize a new instance.
 *
 * @param sio The Socket.IO library
 * @param socket The socket object for the connected client.
 */
exports.init = function(sio, socket) {
  socket.emit("init ui", textNames);

  socket.on("get text", function(textName) {
    socket.emit("add text", texts[textName]);
  });
};