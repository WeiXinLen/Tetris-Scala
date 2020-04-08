package com.VDLY.Tetris.view
import java.util.logging.Handler

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
class GaiaGameController(
  tetris: AnchorPane,
  tetrisBoard: GridPane,
  nextPieceBoard: GridPane,
  score: Label,
  showPaused: Label,
 ) extends ScalaFXSound {

  var game = new GaiaGame
  var gameOver = false
  var rectangles: List[List[Rectangle]] = List()
  var nextPieceRectangles: List[List[Rectangle]] = List()
  // control
  var pause = false
  var leftPressed = false
  var rightPressed = false
  var upPressed = false
  var downPressed = false
  var enterPressed = false
  // for animationTimer
  var time = 0L

  // if pressed the rules, pause the game and show the rules
  def handleGaiaRules(action: ActionEvent): Unit = {
    timer.stop
    showPaused.setText("Game Paused")
    pause = true
    MainApp.showGaiaRules()
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

  // create every single rectangles for tetrisBoard
  for (row <- 0 until game.gameBoard.rows) {
    var tmpRec: List[Rectangle] = List()
    for (col <- 0 until game.gameBoard.columns) {
      if (col == 0 || col == game.gameBoard.columns - 1) {
        //sides pink
        tmpRec = tmpRec :+ new Rectangle {
          width = 27
          height = 27
          fill = "pink"
        }
      }
      else if (row >= game.gameBoard.rows - 3 && row <= game.gameBoard.rows - 1) {
        //bottom 3 rows
        if (col >= game.gameBoard.columns - 7 && col <= game.gameBoard.columns - 4) {
          //tree branch
          tmpRec = tmpRec :+ new Rectangle {
            width = 27
            height = 27
            fill= "brown"
          }
        }
        else if (row == game.gameBoard.rows - 1) {
          if (col < 3 || col > 6) {
            //bottom pink
            tmpRec = tmpRec :+ new Rectangle {
              width = 27
              height = 27
              fill = "pink"
            }
          }
        }
        else {
          tmpRec = tmpRec :+ new Rectangle {
            //white for last 3 bottom rows
            width = 27
            height = 27
            fill ="white"
          }
        }
      }
      else if (row == 0) {
        //at the top most pink
        tmpRec = tmpRec :+ new Rectangle {
          width = 27
          height = 27
          fill = "pink"
        }
      }
      else {
        tmpRec = tmpRec :+ new Rectangle {
          //white
          width = 27
          height = 27
          fill = "white"
        }
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
        opacity = 0.5
      }
      nextPieceBoard.add(tmpRec(col), col, row)
    }
    nextPieceRectangles = nextPieceRectangles ++: List(tmpRec)
  }

//  Event Handler
//  Controls
  tetris.onKeyPressed = (e: KeyEvent) => {
    if (e.code == KeyCode.Left) leftPressed = true
    if (e.code == KeyCode.Right) rightPressed = true
    if (e.code == KeyCode.Up) upPressed = true
    if (e.code == KeyCode.Down) downPressed = true
    if (e.code == KeyCode.Enter) enterPressed = true
    if (e.code == KeyCode.P) {
      if (!pause && !gameOver) {
        pauseSoundEffect()
        timer.stop
        showPaused.setText("Game Paused!")
        pause = true
      }
      else {
        if (pause && !gameOver) {
          pauseSoundEffect()
          timer.start
          showPaused.setText("")
          pause = false
        }
      }
    }
  }

  def refreshBoard(): Unit = {
    for (row <- 0 until game.gameBoard.rows) {
      for (col <- 0 until game.gameBoard.columns) {
        // if it occupy, fill the right colour, otherwise make it white
        if (game.gameBoard.board(row)(col) == 1) {
          if (row >= game.gameBoard.rows - 3 && row <= game.gameBoard.rows - 1) {
            if (col >= game.gameBoard.columns - 7 && col <= game.gameBoard.columns - 4)
              rectangles(row)(col).fill = "brown"
            else
              rectangles(row)(col).fill = "lightgreen"
          }
          else if (row > 0)
            rectangles(row)(col).fill = "lightgreen"
        }
        // for branches
        else if (game.gameBoard.board(row)(col) == 2)
          rectangles(row)(col).fill = "brown"
        else {
          if (row == 0) {
            // at the very top filled with pink
            rectangles(row)(col).fill = "pink"
          }
          else if (col == 0 || col == game.gameBoard.columns - 1) {
            // at both leftmost and rightmost filled with pink
            rectangles(row)(col).fill = "pink"
          }
          else if (row == game.gameBoard.rows - 1) {
            // at the very bottom fill the row with pink except tree branch
            if (col < game.gameBoard.columns - 7 || col > game.gameBoard.columns - 4)
              rectangles(row)(col).fill = "pink"
          }
          else
            rectangles(row)(col).fill = "white"
        }
      }
    }
  }

  def paintPieceToBoard(piece: List[Array[Int]], currentX: Int, currentY: Int): Unit = {
    //paint board black blue according to pieces
    for (a <- piece.indices) {
      rectangles(piece(a)(1)+currentY)(piece(a)(0)+currentX).fill = "blue"
    }
  }

  def clearPieceFromBoard(piece: List[Array[Int]], currentX: Int, currentY: Int): Unit = {
    // paint the board back to white according to the piece
    for (a <- piece.indices) {
      rectangles(piece(a)(1) + currentY)(piece(a)(0) + currentX).fill = "white"
    }
    refreshBoard()
  }

  //score
  def updateScore(): Unit ={
    var scores = game.scores
    score.setText(scores.toString)
  }


  //animationTimer
  val timer: AnimationTimer = AnimationTimer(t => {

    // if nextPiece is empty, get new piece from randomPiece
    if (game.nextPiece.isEmpty) {
      game.nextPiece = game.randomPiece()
      for (a <- game.nextPiece.head.indices) {
        nextPieceRectangles(game.nextPiece.head(a)(1))(game.nextPiece.head(a)(0)+1).fill = "red"
      }
    }

    // if currentPiece is empty, get new one
    if (game.currentPiece.isEmpty) {
      var tmpPiece = game.nextPiece
      game.currentTetrad = tmpPiece
      for (a <- game.nextPiece.head.indices) {
        nextPieceRectangles(game.nextPiece.head(a)(1))(game.nextPiece.head(a)(0)+1).fill = "white"
      }
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
        rectangles(game.currentPiece(a)(1)+game.tetraminoes.currentY)(game.currentPiece(a)(0)+game.tetraminoes.currentX).fill = "blue"
      }
    }

    if (leftPressed) {
      game.moveSet(-1,0)
      refreshBoard()
      paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      leftPressed = false
    }
    if (rightPressed) {
      // go right
      game.moveSet(1,0)
      refreshBoard()
      paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      rightPressed = false
    }
    if (upPressed) {
      // rotate
      if (game.isRotatable()) {
        rotateSoundEffect()
        clearPieceFromBoard(game.currentPiece,game.tetraminoes.currentX,game.tetraminoes.currentY)
        game.currentPiece = game.tetraminoes.rotate(game.currentTetrad, game.tetraminoes.currentZ)
        paintPieceToBoard(game.currentPiece, game.tetraminoes.currentX, game.tetraminoes.currentY)
      }
      else
        collisionSoundEffect()
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
      } while (game.currentPiece.nonEmpty)
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
  })

  timer.start
}
