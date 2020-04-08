package com.VDLY.Tetris.view
import com.VDLY.Tetris.model.GaiaRules
import scalafx.event.ActionEvent
import scalafx.scene.control.Label
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class GaiaRulesController(
  private val rule1Label: Label,
  private val rule2Label: Label,
  private val rule3Label: Label,
  private val rule4Label: Label,
  private val rule5Label: Label,
 ) {

  var dialogStage: Stage = _
  var okClicked = false

  def handleOk(action :ActionEvent): Unit = {
    okClicked = true
    dialogStage.close()
  }

  private def showRules(): Unit =  {
    // Fill the labels with info from the person object.
    rule1Label.text <== GaiaRules.RULE_1
    rule2Label.text <== GaiaRules.RULE_2
    rule3Label.text <== GaiaRules.RULE_3
    rule4Label.text <== GaiaRules.RULE_4
    rule5Label.text <== GaiaRules.RULE_5
  }
  showRules()
}
