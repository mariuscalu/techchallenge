package com.algolia.ingestion

import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object QueryIngestionJob {

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

    val partitionedQueries = queries
      .withColumn("year", year(col("timestamp")))
      .withColumn("month", month(col("timestamp")))
      .withColumn("day", dayofmonth(col("timestamp")))
      .withColumn("hour", hour(col("timestamp")))
      .withColumn("minute", minute(col("timestamp")))
      .drop("timestamp")

        partitionedQueries
          .write
          .format("com.databricks.spark.csv")
          .mode("Append")
          .partitionBy("year", "month", "day", "hour", "minute")
          .save("C:\\Projects\\algolia-datalake-queries")
  }
}
