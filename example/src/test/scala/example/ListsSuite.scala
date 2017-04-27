package example

import com.mockrunner.example.jdbc.Bank
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
  * link to mockrunner-jdbc object. test adapted from
  * com.mockrunner.example.jdbc#testWrongId
  */

 @RunWith(classOf[JUnitRunner])
  class ListsSuite extends FunSuite {
  test("test link to mockrunner-jdbc object. adapted testWrongId()"){
    MockRunnerLink.init()
    MockRunnerLink.prepareEmptyResultSet()
    // bank
    val bank: Bank = new Bank
    bank.connect()
    bank.transfer(1, 2, 5000)
    bank.disconnect()
    //
    MockRunnerLink.sVerifySQLStatementExecuted("select balance")

    MockRunnerLink.sVerifySQLStatementNotExecuted("select akkount")
    MockRunnerLink.sVerifyNotCommitted()
    MockRunnerLink.sVerifyRolledBack()
    MockRunnerLink.sVerifyAllResultSetsClosed()
    MockRunnerLink.sVerifyAllStatementsClosed()
    MockRunnerLink.sVerifyConnectionClosed()

  }


}
