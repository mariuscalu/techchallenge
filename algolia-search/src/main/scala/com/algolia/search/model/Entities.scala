package com.algolia.search.model

import com.algolia.search.reader._
import com.algolia.search.reader.PathComposer._

object Entities {

  final case class CountDTO(count: Long)
  final case class QueryDTO(query: String, count: Long)
  final case class PopularityDTO(queries: List[QueryDTO])

  def getSampleCount(): CountDTO = {
    PathComposer.getFiles("2015-08-01 00:04", COUNT)
    CountDTO(12345L)
  }
  def getSamplePopularity(): PopularityDTO = {
    PathComposer.getFiles("2015-08-01 00:04", COUNT)
    val query1 = QueryDTO("www.test1.com", 1234L)
    val query2 = QueryDTO("www.test1.com", 1235L)
    PopularityDTO(List(query1, query2))
  }
}
