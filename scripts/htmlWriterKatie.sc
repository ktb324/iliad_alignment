
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

// Getting labels for a URN
//     note that these depend on the stuff defined above
val groupName:String = tr.catalog.groupName(u(""))
val workTitle:String = tr.catalog.workTitle(u(""))
val versionLabel:String = tr.catalog.versionLabel(u(""))


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
// Chunk-by-citation
def chunkByChapter(c:Corpus, level:Int = 1):Vector[Corpus] = {
	// we start with a Vector of CitableNodes from our corpus
	val v1:Vector[CitableNode] = c.nodes
	// We zipWithIndex to capture their sequence
	val v2:Vector[(CitableNode, Int)] = v1.zipWithIndex
	// We group by top-level URNs
	val v3:Vector[(CtsUrn, Vector[(CitableNode, Int)])] = {
		v2.groupBy( _._1.urn.collapsePassageTo(level) ).toVector
	}
	// GroupBy destroys order, but we have the original index for re-sorting
	val v4:Vector[(CtsUrn, Vector[(CitableNode, Int)])] = v3.sortBy(_._2.head._2)
	// Get rid of the stuff we don't need
	val v5:Vector[Vector[(CitableNode, Int)]] = v4.map(_._2)
	// Map to a Vector of Corpora
	val corpusVec:Vector[Corpus] = v5.map( v => {
		val nodes:Vector[CitableNode] = v.map(_._1)
		Corpus(nodes)
	})
	corpusVec
}


// Content to stick at the top of every page
var htmlTop:String = s"""<!DOCTYPE html>
<html>
<head>
	<title>${groupName}: ${workTitle}</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<link rel="stylesheet" type="text/css" href="style-mine.css">
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

	/* Navigation */
	val prevLink:String = {
		chaptNum match {
			case n if (n == 1) => { "" }
			case _ => { s"""<a href="chapter${chaptNum - 1}.html">previous</a>""" }
		}
	}
	val nextLink:String = {
		chaptNum match {
			case n if (n == (chapterChunks.size)) => { "" }
			case _ => { s"""<a href="chapter${chaptNum + 1}.html">next</a>""" }
		}
	}
	val nav:String = s"""<div class="nav">${prevLink} | ${nextLink}</div>"""
	/* End Navigation */

	/* Chapter Heading */
	val bookHeader:String = s"""
		<div class="bookHeader block color${((chaptNum-1) % 20) + 1}">
			<p class="textOnColor">Chapter ${chaptNum}</p>
		</div>
	"""

	// create a container with all the CitableNodes for this chunk
	val containerOpen:String = """<div class="text">"""
	val containerClose:String = """</div>"""

	val passages:Vector[String] = c.nodes.map( n => {
		s"""<p class="bookParagraph"><span class="cite">${n.urn.passageComponent}</span>${n.text}</p>"""
	})

	// save this chunk as an html file
	val htmlString:String = {
		htmlTop +
		bookHeader +
		containerOpen +
		nav +
		passages.mkString("\n") +
		nav +
		containerClose +
		htmlBottom
	}
	// Write out to a file
	saveString(htmlString, "html/", htmlName)
}
