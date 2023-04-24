package jp.co.axa.apidemo

import groovy.sql.Sql
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class EmployeeDbSpec extends Specification {

  /**
   * This class will allow tests to handle sql commands and create database records
   */

  public static Sql sql = Sql.newInstance('jdbc:h2:mem:testdb', 'sa', '', 'org.h2.Driver')

  def deleteTables(String... tableNames) {
    tableNames.each {
      deleteTable(it)
    }
  }

  def deleteTable(String tableName) {
    sql.execute 'DELETE FROM ' + tableName
  }
}
