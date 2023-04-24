package jp.co.axa.apidemo.utility


import groovy.sql.Sql
import jp.co.axa.apidemo.utility.hogan.parser.TableParser

class TableSet {
  static void insert(Sql sql, tableName, rows) {
    TableParser.asTable(rows).toMapList().each {
      sql.dataSet(tableName).add(it)
    }
  }

  static def of(rows) {
    TableParser.asTable(rows).toMapList().collect { it }
  }
}
