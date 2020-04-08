package com.VDLY.Tetris.view

import com.VDLY.Tetris.MainApp
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml

@sfxml
class MainController {

  def playNormal(action: ActionEvent): Unit = MainApp.showNormalBoard()

  def playGaia(action: ActionEvent): Unit = MainApp.showGaiaBoard()

}
