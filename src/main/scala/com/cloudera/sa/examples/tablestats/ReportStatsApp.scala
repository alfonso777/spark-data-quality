package com.cloudera.sa.examples.tablestats

import com.cloudera.sa.examples.tablestats.model.{FirstPassStatsModel}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
//import org.slf4j.LoggerFactory
import scala.util.{Try, Success, Failure}
import java.io.FileWriter

object ReportStatsApp {
  //val logger = LoggerFactory.getLogger(this.getClass)
  def main(args: Array[String]): Unit = {
    val database = args(0)
    val filteredTables = if(args(1) == "ALL") Array("") else args(1).split(",")

    val spark = SparkSession.builder()
      .appName("DataQuality")
      //.config("spark.sql.parquet.writeLegacyFormat", "true")
      .enableHiveSupport
      .getOrCreate()

    import spark.implicits._
    val targetTables = Try(spark.catalog.listTables(database)) match {
      case Success(ts) => if(filteredTables.isEmpty) ts else ts.filter(t => filteredTables.contains(t.name))
      case Failure(e) => throw new IllegalArgumentException("Error at getting tables",e)
    }


    val fileOutputName = s"stats-$database-${System.currentTimeMillis}.txt"
    targetTables.map(t => database + "." + t.name).foreach(t => handleStats(getTableStats(t, spark).toString, fileOutputName))
    spark.stop()
  }

  def getTableStats(fullTableName: String, spark: SparkSession) = {
    val df = spark.table(fullTableName)
    TableStatsSinglePathMain.getFirstPassStat(df)
  }

  def handleStats(stats: String, fileOutputName: String) = {
    val fw = new FileWriter(fileOutputName, true)
    try{ fw.write(stats) } finally fw.close() 
    println(stats)    
  }

}