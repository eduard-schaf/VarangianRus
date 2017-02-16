UI.utils = {
  /**
   * Detect capitalization pattern in target word
   *
   * 0 = not capitalized or weird enough to leave alone
   * 1 = all caps
   * 2 = first letter capitalized
   *
   * @param {string} word the target word
   * @returns {number} the capitalization type
   */
  detectCapitalization: function(word) {
    if(word == word.toUpperCase()) {
      return 1;
    }
    else if(word == word.substr(0, 1).toUpperCase() + word.substr(1)) {
      return 2;
    }
    else {
      return 0;
    }
  },

  /**
   * Parallel capitalization (for multiple choice drop-downs).
   *
   * @param {string} word the target word
   * @param {number} type the capitalization type
   * @returns {string} the word with equal capitalization to the correct answer
   */
  matchCapitalization: function(word, type) {
    switch(type) {
      case 0:
        return word;
      case 1:
        return word.toUpperCase();
      case 2:
        return word.slice(0, 1).toUpperCase() + word.slice(1);
      default:
        return word;
    }
  },

  /**
   * Fisher-Yates shuffle, rearrange an array.
   *
   * @param elemList the list to be shuffled
   */
  shuffleList: function(elemList) {
    let i, j, tempElem;
    for (i = elemList.length; i > 1; i--) {
      j = parseInt(Math.random() * i);
      tempElem = elemList[j];
      elemList[j] = elemList[i - 1];
      elemList[i - 1] = tempElem;
    }
  }
};