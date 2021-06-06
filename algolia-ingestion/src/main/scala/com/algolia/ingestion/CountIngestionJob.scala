package com.algolia.ingestion

import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, count, dayofmonth, hour, minute, month, year}

object CountIngestionJob {

  @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder.appName("Ingestion").master("local[*]").getOrCreate()

    import spark.sqlContext.implicits._

    val queries = spark
      .read
      .option("inferSchema", "true")
      .option("delimiter", "\t")
      .csv("src/main/resources/queries/hn_logs.tsv")
      .withColumnRenamed("_c0", "timestamp")
      .withColumnRenamed("_c1", "query")

    val enrichedQueries = queries
      .withColumn("year", year(col("timestamp")))
      .withColumn("month", month(col("timestamp")))
      .withColumn("day", dayofmonth(col("timestamp")))
      .withColumn("hour", hour(col("timestamp")))
      .withColumn("minute", minute(col("timestamp")))
      .drop("timestamp")

    val counterWindow = Window.partitionBy("year", "month", "day", "hour", "minute")
    val partitionedQueries = enrichedQueries
      .withColumn("count", count("query").over(counterWindow))
      .drop("query")
      .distinct()

        partitionedQueries
          .write
          .format("com.databricks.spark.csv")
          .mode("Append")
          .partitionBy("year", "month", "day", "hour", "minute")
          .save("C:\\Projects\\algolia-datalake-count")
  }
}
