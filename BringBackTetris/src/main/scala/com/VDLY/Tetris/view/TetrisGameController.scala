package com.VDLY.Tetris.view

import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{AnchorPane, GridPane}
import com.VDLY.Tetris.MainApp
import scalafxml.core.macros.sfxml
import scalafx.scene.shape.Rectangle
import scalafx.animation._
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.application.Platform
import scalafx.scene.input.{KeyCode, KeyEvent}
import com.VDLY.Tetris.model._

@sfxml
class TetrisGameController(
  tetris: AnchorPane,
  tetrisBoard: GridPane,
  nextPieceBoard: GridPane,
  score: Label,
  showPaused: Label,
 ) extends ScalaFXSound {

  var game = new TetrisGame
  var gameOver = false
  var rectangles: List[List[Rectangle]] = List()
  var nextPieceRectangles: List[List[Rectangle]] = List()
  var pause = false
  var leftPressed = false
  var rightPressed = false
  var upPressed = false
  var downPressed = false
  var enterPressed = false
  // for animationTimer
  var time = 0L

  // if pressed the rules, pause the game and show the rules
  def handleNormalRules(action: ActionEvent): Unit = {
    timer.stop
    showPaused.setText("Game Paused")
    pause = true
    MainApp.showNormalRules()
    timer.start
    showPaused.setText("")
    pause = false
  }

  // if press back button, go back to Main
  def handleBack(action: ActionEvent): Unit = {
    timer.stop
    pause = true
    MainApp.showMain()
  }

  def refreshBoard(): Unit = {
    for (row <- 0 until game.gameBoard.rows) {
      for (col <- 0 until game.gameBoard.columns) {
        //if occupied, fill blue; else white
        if (game.gameBoard.board(row)(col)==1)
          rectangles(row)(col).fill = "blue"
        else
          rectangles(row)(col).fill = "white"
      }
    }
  }

  def paintPieceToBoard(piece: List[Array[Int]], currentX: Int, currentY: Int): Unit = {
    //paint board black blue according to pieces
    for (a <- piece.indices) {
      rectangles(piece(a)(1) + currentY)(piece(a)(0) + currentX).fill = "blue"
    }
  }

  def clearPieceFromBoard(piece: List[Array[Int]], currentX: Int, currentY: Int): Unit = {
    // paint the board back to white according to the piece
    for (a <- piece.indices) {
      rectangles(piece(a)(1) + currentY)(piece(a)(0) + currentX).fill = "white"
    }
  }

  def updateScore(): Unit = {
    var scores = game.scores
    score.setText(scores.toString)
  }

  // create every single rectangles for tetrisBoard
  for (row <- 0 until game.gameBoard.rows) {
    var tmpRec: List[Rectangle] = List()
    for (col <- 0 until game.gameBoard.columns) {
      tmpRec = tmpRec :+ new Rectangle {
        width = 27
        height = 27
        fill = "white"
      }
      tetrisBoard.add(tmpRec(col), col, row)
    }
    rectangles = rectangles ++: List(tmpRec)
  }

  // create every single rectangles for nextPieceBoard
  for (row <- 0 until 4) {
    var tmpRec: List[Rectangle] = List()
    for (col <- 0 until 4) {
      tmpRec = tmpRec :+ new Rectangle {
        width = 27
        height = 27
        fill = "white"
      }
      nextPieceBoard.add(tmpRec(col), col, row)
    }
    nextPieceRectangles = nextPieceRectangles ++: List(tmpRec)
  }

  // controls
  tetris.onKeyPressed = (e: KeyEvent) => {
    if (e.code == KeyCode.Left) leftPressed = true
    if (e.code == KeyCode.Right) rightPressed = true
    if (e.code == KeyCode.Up) upPressed = true
    if (e.code == KeyCode.Down) downPressed = true
    if (e.code == KeyCode.Enter) enterPressed = true
    if (e.code == KeyCode.P) {
      // if not pause and game is not over, proceed with pausing
      if (!pause && !gameOver) {
        pauseSoundEffect()
        timer.stop
        showPaused.setText("Game Paused!")
        pause = true
      }
      else {
        // else if game is paused but the game is not over yet, proceed with resuming
        if (pause && !gameOver) {
          pauseSoundEffect()
          timer.start
          showPaused.setText("")
          pause = false
        }
      }
    }
  }

  // variable function for animationTimer
  val timer: AnimationTimer = AnimationTimer(t => {

    // if nextPiece is empty, get new piece from randomPiece
    if (game.nextPiece.isEmpty) {
      game.nextPiece = game.randomPiece()
      for (a <- game.nextPiece.head.indices) {
        nextPieceRectangles(game.nextPiece.head(a)(1))(game.nextPiece.head(a)(0) + 1).fill = "red"
      }
    }

    if (game.currentPiece.isEmpty) {
      game.checkRows()
      val tmpPiece = game.nextPiece
      game.currentTetrad = tmpPiece
      for (a <- game.nextPiece.head.indices) {
        nextPieceRectangles(game.nextPiece.head(a)(1))(game.nextPiece.head(a)(0) + 1).fill = "white"
      }
      //for every new pieces
      game.nextPiece = List()
      game.tetraminoes.currentZ = 0
      game.tetraminoes.currentX = 4
      game.tetraminoes.currentY = 0
      game.currentPiece = game.currentTetrad.head

      if (game.checkGameOver()) {
        gameOver = true
        timer.stop
        val alert = new Alert(AlertType.Information) {
          title = "Game Over"
          headerText = "You Lose!"
          contentText = "You have scored: " + game.scores.toString
        }
        showPaused.setText("Game Over!")
        Platform.runLater(alert.showAndWait())
        gameOverSoundEffect()
      }

      for (a <- game.currentPiece.indices) {
        rectangles(game.currentPiece(a)(1) + game.tetraminoes.currentY)(game.currentPiece(a)(0) + game.tetraminoes.currentX).fill = "blue"
      }
    }

    if (leftPressed) {
      game.moveSet(-1,0)
      refreshBoard()
      paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      leftPressed = false
    }

    if (rightPressed) {
      game.moveSet(1,0)
      refreshBoard()
      paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      rightPressed = false
    }

    if (upPressed) {
      // check whether the piece is rotatable
      if (game.isRotatable()) {
        rotateSoundEffect()
        clearPieceFromBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
        game.currentPiece = game.tetraminoes.rotate(game.currentTetrad, game.tetraminoes.currentZ)
        paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      }
      else collisionSoundEffect()
      upPressed = false
    }

    if (downPressed) {
      game.moveSet(0,1)
      refreshBoard()
      paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      downPressed = false
    }

    if (enterPressed) {
      do {
        game.moveSet(0,1)
        refreshBoard()
        paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      } // loop until the current piece is empty
      while (game.currentPiece.nonEmpty)
      enterPressed = false
    }

    // make the body of this if statement to run every second
    if ((t - time) > 1e+9) {
      game.moveSet(0,1)
      updateScore()
      refreshBoard()
      paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      time = t
    }
  }) // end timer function

  // main
  timer.start
}
