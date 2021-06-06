package com.algolia.search.serializer

import com.algolia.search.model.Entities._
import spray.json.DefaultJsonProtocol

object JsonFormats {

  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val countJsonFormat = jsonFormat1(CountDTO)
  implicit val queryJsonFormats = jsonFormat2(QueryDTO)
  implicit val popularityJsonFormat = jsonFormat1(PopularityDTO)
}
