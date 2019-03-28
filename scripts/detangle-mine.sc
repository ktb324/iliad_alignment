import scala.io.Source
import edu.holycross.shot.cite._
import java.io._

def saveString(s:String, filePath:String = "", fileName:String = "temp.txt"):Unit = {
	val pw = new PrintWriter(new File(filePath + fileName))
	for (line <- s.lines){
		pw.append(line)
		pw.append("\n")
	}
	pw.close
}

val urnBase:String = "urn:cts:fufolio:baum.wizardofoz.fu2019:"

case class IndexedLine(text:String, index:Int)
case class ChapterHeading(title:String, index:Int)
case class BookPara(chapterName:String, text:String, index:Int)


val filepath:String = "wizardofoz.txt"
val myLines:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter( _.size > 0 )

// Grab line numbers
val indexedFileLines:Vector[IndexedLine] = myLines.zipWithIndex.map(ln => {
  new IndexedLine(ln._1, ln._2)
})

// Filter out chapter headings

val chapters:Vector[ChapterHeading] = {
  indexedFileLines.filter(_.text.startsWith("Chapter")).map( c =>{
    val index:Int = c.index
    val newTitle:String = c.text.replaceAll("Chapter ","").replaceAll("\\.","")
    new ChapterHeading(newTitle, index)
  })
}

val realParagraphs:Vector[IndexedLine] = {
  indexedFileLines.filter(_.text.startsWith("Chapter") == false )
}

// find where each chapter begins and ends!
val chapterRanges:Vector[Vector[ChapterHeading]] = chapters.sliding(2,1).toVector

//grab line numbers
val allButTheLastChapter:Vector[BookPara] = chapterRanges.map(cr => {
  val thisChapt:ChapterHeading = cr.head
  // the line-number in the original file where this chapter begins
  val thisChaptLineNum:Int = thisChapt.index

  val nextChapt:ChapterHeading = cr.last
  // the line-number in the original file where the next chapter begins
  val nextChaptLineNum:Int = nextChapt.index

  // the paragraphs of my text that belong to this chapter
  val chapterParas:Vector[IndexedLine] = {
    realParagraphs.filter( il => {
      (( il.index > thisChaptLineNum) & (il.index < nextChaptLineNum ) )
    })
  }

  val bookParas:Vector[BookPara] = chapterParas.zipWithIndex.map (cp => {
    val thisIndex:Int = cp._2 + 1
    new BookPara( thisChapt.title, cp._1.text, thisIndex)
  })
  // return that value
  bookParas
}).flatten

val theLastChapter:Vector[BookPara] = {
  val lastChaptHeading:String = chapterRanges.last.last.title
  val lastChaptLineNum:Int = chapterRanges.last.last.index

  val chapterParas:Vector[IndexedLine] = {
    realParagraphs.filter( il => {
      (il.index > lastChaptLineNum)
    })
  }
  val bookParas:Vector[BookPara] = chapterParas.map(cp => {
    new BookPara( lastChaptHeading, cp.text, cp.index)
  })
  bookParas
}

val betterTLC:Vector[BookPara] = theLastChapter.zipWithIndex.map( a => {
  // each "a" is a (BookPara, Int)
  val thisIndex:Int = a._2 + 1
  val oldPara:BookPara = a._1
  val oldChap:String = oldPara.chapterName
  val oldText:String = oldPara.text
  BookPara(oldChap, oldText, thisIndex)
})

val allChapterLines:Vector[BookPara] = {
  allButTheLastChapter ++ betterTLC
}

val savableLines:Vector[String] = {
  allChapterLines.map( cl => {
    urnBase + cl.chapterName + "." + cl.index + "#" + cl.text
  })
}

val stringToSave:String = savableLines.mkString("\n")

saveString(stringToSave)
