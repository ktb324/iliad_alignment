# The Wizard of Oz Repository

This repository contains several files that allow the user to analyze Frank L. Baum's novel, *The Wizard of Oz*, in more depth than a traditional .txt file would allow. 

## HTML

The html file contains a file called index.html that will take the user to a website of my own creation.  This website is formatted in the traditional style of the first edition of *The Wizard of Oz* in order to give it a nostalgic feel.  The website allows users to easily navigate to a desire chapter and from there allows them to scroll through each chapter in the book.  

The html file also contains a file called style-mine.css.  This file contains CSS code for the creation of my website and can be altered to change the apperance of the site.

## CEX

Within this repository is a file titled wizardofoz.cex.  This file contains a format of *The Wizard of Oz* that has been cited by chapter and paragraph in order to make the text easier to navigate.  This file contains urn citations of each paragraph.

## Webfont

The webfont file simply contains a link to a font that is not offered on all web browsers.  This allows it to be accessed and seen by anyone viewing my html file, regardless of their browser. 

## Scripts

The scripts file contains serveral important pieces of this repository.  The detangle-mine.sc creates a citable urn by seperating the text into chapters and paragraphs and assigning each the appropriate citation.  The urn citations are then written into the CEX file. 

The htmlWriterKatie.sc file contains code for creating the index.html file It assigns each piece of the text a <div> according to how it will be represented in the CSS code.  

### Analysis

Perhaps one of the most interesting features of my repository is contained within the analysis.sc file.  This file contains code that analyzes and assigns each chapter of *The Wizard of Oz* a reading score.  This score is based off of the criteria of the Flesch-Kenkaid Grade Level scoring system.  The system takes in to account the sentance length as well as the amount of syllables in each word.  Because of this grading process, the file contains code that attempts to count the syllables contained in each word.  The code returns both a list of the chapters and their reading level in chronological order, as well as in order of difficulty based on score. 

This analysis is interesteing in any setting because it is neat to understsnd what goes into determining the reading level of a novel, as well as understanding how that score is reached.  This methid could face limitations in the fact that the counting of syllables may not always be entirely accurate.

While this could pose a problem for texts that have particularly lengthy words or oddly spelled words, *The Wizard of Oz* is pretty cut and dry.  I was fairly confident with the results of the analysis showing that this novel spanned reading levels from 4th to 8th grade and I was happy to discover [here](https://www.dogobooks.com/the-wizard-of-oz/book-review/0030616611)

http://github.com - automatic!
[GitHub](http://github.com)

