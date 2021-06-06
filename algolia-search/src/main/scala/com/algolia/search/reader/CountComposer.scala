package com.algolia.search.reader

import java.io.File

import com.algolia.search.model.Entities.CountDTO
import PathComposer.COUNT

import scala.util.{Failure, Success, Try}

object CountComposer {

  def getResponse(date: String): CountDTO = {
    val files = PathComposer.getFiles(date, COUNT)
    val counts = readFiles(files)
    val count = counts.sum
    CountDTO(count)
  }

  private def readFiles(listOfFiles: List[File]): List[Long] = {

    def getContentOfFile(file: File): Long = {
      val source = scala.io.Source.fromFile(file)
      val line = try source.mkString finally source.close()
      Try(line.trim.toLong) match {
        case Success(value) => value
        case Failure(exception) => 0L
      }
    }

     def loop(files: List[File], values: List[Long]): List[Long] = files match {
       case Nil => values
       case head :: tail => loop(tail, getContentOfFile(head) :: values)
     }

    loop(listOfFiles, Nil)
  }
}
