#### Midterm Assignment
### N-Grams

## What are N-Grams?

An N-Gram is a sequence of letters, symbols, words, etc.. that appear consecutively in any given text. To fully understand an N-Gram one must first understand the relevence of its title. The letter "N" in the title "N-Gram" refers to a number that can vary as needed depending on what the user is looking for. Therefore if the user wants a sequence only one character long, n would equal 1; if the user wanted a sequence two characters long, n would be 2. For example, N-Grams can be applied to the word "hotdog". When split into sequences one character long (unigram), "hotdog" becomes "h, o, t, d, o, g". When using an n-gram to split it into sequences of two (bigram) "hotdog" is "ho, ot, td, do, og", this displays all the possible consecutive sequences containing two characters within the word. 
N-Grams are often applied through n-gram models, which are based around the idea of the Markov chain. This theory states that a given value is likely to change one value negatively or positively at the equal probabilities. There are several applications of this n-gram in all different fields of study. For example, when a molecule undergoes a chemical reaction , it changes its state in a fashion that corresponds with the Markov chain. In a similar way, n-grams can be applied to the predictive text feature found on most smart phones. By analyzing the previously typed words or letters, phone software can make a fairly accurate guess at what words or letters are most likely to be used in order to finish the phrase. 

## How to Find Them

N-Grams are found most simply by loading the desired text into a software (e.g. Python, Scala), converting the text to vectors, and then removing the whitespace and punctuation from the text. The remaining text can is stripped into a collection of characters and can then be filtered into a desired sequence. 

# Useful Methods

One of the most useful methods in Scala for finding n-grams is the command `.filter`. This command can be used to filter blank lines out of a text and removing whitespace. The `.replaceAll` method, when told what punctuation to remove (e.g. ., !, ?), can replace the unwanted punctuation with nothing by simply adding two double quotations. If a text contains both capitalized and uncapitalized characters it is effective to use the method `.toUpperCase`. When this method is paired with the method `.size`, the program will no to convert each character in the text to an upper case letter. This is imperitive because programs often distinguish upper and lower case letters as different values, even if they are the same letter. The method `.map` can be useful for assigning characters to a correlating value. For example, `.map` can assign each letter in the word "cab" a different digit to represent its position in the text. "C" would correspond to 0, "a" to 1, and "b" to 2. 
