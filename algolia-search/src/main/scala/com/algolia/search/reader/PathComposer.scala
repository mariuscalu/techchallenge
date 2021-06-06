package com.algolia.search.reader

import java.io.File

object PathComposer {

  val COUNT = "algolia-datalake-count"
  val QUERY = "algolia-datalake-queries"

  def getFiles(date: String, datalake: String): List[File] = {

    def loop(folders: List[File], level: Int): List[File] =
      if (level == 4) folders
      else loop(folders.flatMap(getSubDir(_)), level + 1)

    val (path, depth) = computePath(date, datalake)
    val root = new File(path)

    val allFolders = if (depth == 5) List(root)
    else loop(getSubDir(root), depth)

    allFolders.flatMap(getAllFiles(_))
  }

  private def getAllFiles(dir: File): List[File] = dir
    .listFiles()
    .filter(_.isFile)
    .filter(!_.toString.contains(".crc"))
    .toList


  private def getSubDir(dir: File): List[File] = dir
      .listFiles()
      .filter(_.isDirectory)
      .toList

  private def computePath(date: String, datalake: String): (String, Int) = {
    val root = s"C:\\Projects\\$datalake"

    def removeZero(str: String): String =
      if (str.substring(0,1).contains("0")) str.substring(1,2)
      else str

    val pair = date.length match {
      case 4 =>
        val year = date
        (s"\\year=$year", 1)
      case 7 =>
        val year = date.substring(0, 4)
        val month = removeZero(date.substring(5, 7))
        (s"\\year=$year\\month=$month", 2)
      case 10 =>
        val year = date.substring(0, 4)
        val month = removeZero(date.substring(5, 7))
        val day = removeZero(date.substring(8, 10))
        (s"\\year=$year\\month=$month\\day=$day", 3)
      case 13 =>
        val year = date.substring(0, 4)
        val month = removeZero(date.substring(5, 7))
        val day = removeZero(date.substring(8, 10))
        val hour = removeZero(date.substring(11, 13))
        (s"\\year=$year\\month=$month\\day=$day\\hour=$hour", 4)
      case 16 =>
        val year = date.substring(0, 4)
        val month = removeZero(date.substring(5, 7))
        val day = removeZero(date.substring(8, 10))
        val hour = removeZero(date.substring(11, 13))
        val minute = removeZero(date.substring(14, 16))
        (s"\\year=$year\\month=$month\\day=$day\\hour=$hour\\minute=$minute", 5)
    }

      (root.concat(pair._1), pair._2)
    }

}
