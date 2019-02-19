package com.cloudera.sa.examples.tablestats.model

import scala.collection.mutable

/**
 * Created by ted.malaska on 6/29/15. Customized By alfonso777
 */
class FirstPassStatsModel(tableName: String) extends Serializable {
  var columnStatsMap = new mutable.HashMap[String, ColumnStats]

  def +=(colIndex: String, colValue: Any, colCount: Long): Unit = {
    columnStatsMap.getOrElseUpdate(colIndex, new ColumnStats) += (colValue, colCount)
  }

  def +=(firstPassStatsModel: FirstPassStatsModel): Unit = {
    firstPassStatsModel.columnStatsMap.foreach{ e =>
      val columnStats = columnStatsMap.getOrElse(e._1, null)
      if (columnStats != null) {
        columnStats += (e._2)
      } else {
        columnStatsMap += ((e._1, e._2))
      }
    }
  }

  override def toString = s"FirstPassStatsModel(tableName=$tableName, columnStatsMap=$columnStatsMap)"
}
