package com.VDLY.Tetris.model

import scala.util.Random

class GaiaGame extends ScalaFXSound {

  val tetraminoes = new Tetramino
  //list of tetrads
  val tetrads = List(tetraminoes.T, tetraminoes.J, tetraminoes.L, tetraminoes.O, tetraminoes.Z, tetraminoes.S, tetraminoes.T, tetraminoes.B)
  //hold the current tetrad
  var currentTetrad: List[List[Array[Int]]] = List()
  //hold the current tetrad, one of the orientation
  var currentPiece: List[Array[Int]] = List()
  // hold nextPiece data
  var nextPiece: List[List[Array[Int]]] = List()
  //score
  var scores: Int = 0
  var gameBoard = GaiaBoard
  gameBoard.initBoard()

  def randomPiece(): List[List[Array[Int]]] = tetrads(Random.nextInt(tetrads.size))

  def isRotatable(): Boolean = {
    val piece = currentTetrad((tetraminoes.currentZ + 1) % currentTetrad.size)
    for (a <- piece.indices) {
      // check whether it hits the side
      if ((piece(a)(0) + tetraminoes.currentX) >= gameBoard.columns || (piece(a)(0) + tetraminoes.currentX) < 0)
        return false
      // check whether it hits the bottom
      if ((piece(a)(1) + tetraminoes.currentY) >= gameBoard.rows)
        return false
      // check whether it is occupied
      if (gameBoard.board(piece(a)(1) + tetraminoes.currentY)(piece(a)(0) + tetraminoes.currentX) == 1)
        return false
      else if (gameBoard.board(piece(a)(1) + tetraminoes.currentY)(piece(a)(0) + tetraminoes.currentX) == 2)
        return false
    } // end for loop
    true
  }

  def checkGameOver(): Boolean = {
    for (a <- currentPiece.indices) {
      if (collisionDetection(currentPiece(a), tetraminoes.currentX, tetraminoes.currentY))
        return true
    }

    for (row <- 0 until gameBoard.rows) {
      for (col <- 0 until gameBoard.columns) {
        if (col == 0 || col == gameBoard.columns - 1) {
          if (gameBoard.board(row)(col) == 1)
            return true
        }
        else if (row == 0) {
          if (gameBoard.board(row)(col) == 1)
            return true
        }
        else if (row == gameBoard.rows - 1) {
          if (col < 3 || col > 6) {
            if (gameBoard.board(row)(col) == 1)
              return true
          }
        }
      }
    }
    false
  }

  def collisionDetection(piece: Array[Int], currentX: Int, currentY: Int): Boolean = {
    if (gameBoard.board(piece(1) + currentY)(piece(0) + currentX) == 1)
      true
    else if (gameBoard.board(piece(1) + currentY)(piece(0) + currentX) == 2)
      true
    else
      false
  }

  def addScore(point: Int): Unit = scores += point

  def moveSet(x: Int, y: Int): Unit = {
    for (a <- currentPiece.indices) {
      // check if it hits the side of the board
      if (currentPiece(a)(0) + tetraminoes.currentX + x >= gameBoard.columns || currentPiece(a)(0) + tetraminoes.currentX + x < 0) {
        hitBorderSoundEffect()
        return
      }
      // check whether it collide with other pieces when pressed left or right
      if (collisionDetection(currentPiece(a), tetraminoes.currentX + x, tetraminoes.currentY)) {
        collisionSoundEffect()
        return
      }
      // check if it hits the bottom of the board
      if ((currentPiece(a)(1) + tetraminoes.currentY + y) > gameBoard.rows - 1) {
        gameBoard.paintBoard(tetraminoes.B, currentPiece, tetraminoes.currentX + x, tetraminoes.currentY)
        currentPiece = List()
        addScore(1)
        collisionSoundEffect()
        return
      }
      // check whether it collides the other pieces
      if (collisionDetection(currentPiece(a), tetraminoes.currentX + x, tetraminoes.currentY + y)) {
        gameBoard.paintBoard(tetraminoes.B, currentPiece, tetraminoes.currentX + x, tetraminoes.currentY)
        currentPiece = List()
        addScore(1)
        collisionSoundEffect()
        return
      }
    }
    moveSoundEffect()
    tetraminoes.move(x, y)
  }
}