package com.algolia.search.reader

import com.algolia.search.model.Entities.{PopularityDTO, QueryDTO}
import PathComposer.QUERY
import java.io.File

import scala.util.{Failure, Success, Try}

object PopularityComposer {

  def getResponse(date: String, size: Int): PopularityDTO = {
    val files = PathComposer.getFiles(date, QUERY)
    val lines = readFiles(files)
    val queries = countPopularity(lines).take(size)
    PopularityDTO(queries)
  }

  private def readFiles(listOfFiles: List[File]): List[String] = {

    def getContentOfFile(file: File): List[String] =
      scala.io.Source.fromFile(file).getLines.toList

    def loop(files: List[File], buffer: List[List[String]]): List[String] = files match {
      case Nil => buffer.flatten
      case head :: tail => loop(tail, getContentOfFile(head) :: buffer)
    }

    loop(listOfFiles, Nil)
  }

  private def countPopularity(lines: List[String]): List[QueryDTO] =
    lines
      .map(line => (line, 1))
      .groupBy(_._1)
      .map(tuple => QueryDTO(tuple._1, tuple._2.size))
      .toList
      .sortWith(_.count > _.count)

}
