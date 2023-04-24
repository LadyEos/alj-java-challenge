package jp.co.axa.apidemo.utility.hogan.parser

import groovy.transform.ToString

@ToString
class Row {

  List values = []

  Row or(arg) {
    values.add(arg)
    this
  }
}
