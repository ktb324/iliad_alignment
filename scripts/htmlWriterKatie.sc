
import scala.io.Source
import java.io._
import scala.collection.mutable.LinkedHashMap
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.seqcomp._
import edu.furman.classics.citealign._
import java.util.Calendar

/* Utilities */

def loadLibrary(fp:String):CiteLibrary = {
	val library = CiteLibrary(Source.fromFile(fp).getLines.mkString("\n"),"#",",")
	library
}

def loadFile(fp:String):Vector[String] = {
	Source.fromFile(fp).getLines.toVector
}

def saveString(s:String, filePath:String = "html/", fileName:String = "temp.txt"):Unit = {
	val pw = new PrintWriter(new File(filePath + fileName))
	for (line <- s.lines){
		pw.append(line)
		pw.append("\n")
	}
	pw.close
}

/* Project-specific CEX Stuff */

val myCexFile:String = "wizardofoz.cex"

lazy val lib = loadLibrary(myCexFile)
lazy val tr = lib.textRepository.get
lazy val ozCorpus = tr.corpus

// Avoid typing lengthy URNs all the time
def u(passage:String):CtsUrn = {
	val baseUrl:String = "urn:cts:fuTexts:baum.wizardofoz.fu2019:"
	CtsUrn(s"${baseUrl}${passage}")
}



def printCorpus(c:Corpus):Unit = {
	println("------")
	for (n <- c.nodes) {
		// Use either this line:
		val thisCitation:String = n.urn.toString
		// or this line:
		//val thisCitation:String = n.urn.passageComponent.toString
		val thisText:String = n.text
		println(s"${thisCitation} :: ${thisText}")
	}
	println("------")
}

// A method for dividing the big Corpus into smaller Corpuses ("Corpora"),
// one for each chapter

/* def chunkByChapter(c:Corpus):Vector[Corpus] = {} */



// Content to stick at the top of every page
var htmlTop:String = s"""<!DOCTYPE html>
<html>
<head>
	<title>${groupName}: ${workTitle}</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>

<body>"""

// Content to stick at the bottom of every page
var htmlBottom:String = """</body></html>"""

// Actually divide the WoOz into a Vector of chapter-Corpuses
val chapterChunks:Vector[Corpus] = chunkByChapter(ozCorpus)

// Do something to each chapter-Corpus, grabbing its sequence-number (.zipWithIndex)
// (The sequence-number will be useful for next- and previous-links)
for ( chapt <- chapterChunks.zipWithIndex) {
	val chaptNum:Int = chapt._2 + 1 // add one because we don't count from zero
	val c:Corpus = chapt._1

	// create a filename
	val htmlName:String = s"chapter${chaptNum}.html"


	// create a container with all the CitableNodes for this chunk
	val containerOpen:String = """<div class="text">"""
	val containerClose:String = """</div>"""

	val passages:Vector[String] = c.nodes.map( n => {
		s"""<p class="poetryLine"><span class="cite">${n.urn}</span>${n.text}</p>"""
	})

	// save this chunk as an html file
	val htmlString:String = {
		htmlTop +
		containerOpen +
		passages.mkString("\n") +
		containerClose +
		htmlBottom
	}
	// Write out to a file
	saveString(htmlString, "html/", htmlName)
}
