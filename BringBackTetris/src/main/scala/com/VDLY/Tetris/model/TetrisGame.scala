package com.VDLY.Tetris.model

import scala.util.Random

class TetrisGame extends ScalaFXSound {

  //create an instance of Tetramino so that each player have different tetramino
  val tetraminoes = new Tetramino
  //list of tetrads
  val tetrads = List(tetraminoes.T, tetraminoes.J, tetraminoes.L, tetraminoes.O, tetraminoes.Z, tetraminoes.S, tetraminoes.T)
  //hold the current tetrad
  var currentTetrad: List[List[Array[Int]]] = List()
  //hold the current tetrad, one of the orientation
  var currentPiece: List[Array[Int]] = List()
  // hold nextPiece data
  var nextPiece: List[List[Array[Int]]] = List()
  var scores: Int = 0
  var gameBoard = TetrisBoard
  gameBoard.initBoard()

  // Taking a random Piece from tetrads
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
    }//end for loop
    true
  }

  //check the rows
  def checkRows(): Unit = {
    for (row <- gameBoard.board.indices) {
      if (isRowComplete(row)) {
        clearRow(row)
        rowDrop(row)
        clearSoundEffect()
      }
    }
  }

  //check if row complete
  def isRowComplete(row: Int): Boolean = {
    for (col <- gameBoard.board(row).indices) {
      if (gameBoard.board(row)(col) == 0)
        return false
    }//end for loop
    true
  }

  // clear the row and add score point
  def clearRow(row: Int): Unit = {
    // turn everything on that row to 0
    for (col <- gameBoard.board(row).indices) {
      gameBoard.board(row)(col) = 0
    }// end for loop once cleared
    addScore(100)
  }

  // move all the above down by 1
  def rowDrop(row: Int): Unit = {
    // from the top to the bottom by decrement 1
    for (rows <- row to 0 by -1) {
      for (col <- gameBoard.board(rows).indices) {
        // drop the occupied rectangles in board down
        if (gameBoard.board(rows)(col) == 1) {
          gameBoard.board(rows + 1)(col) = 1
          // once drop make it unoccupied
          gameBoard.board(rows)(col) = 0
        }
      }
    }
  }

  // add score
  def addScore(point: Int): Unit = scores += point

  //check GameOver
  def checkGameOver(): Boolean = {
    for (a <- currentPiece.indices) {
      if (collisionDetection(currentPiece(a), tetraminoes.currentX, tetraminoes.currentY))
        return true
    }
    // check at the very top of the board
    for (row <- 0 until 2) {
      for (col <- 0 until gameBoard.columns){
        if (gameBoard.board(row)(col) == 1)
          return true
      }
    }
    false
  }

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
      // check if it hits the very bottom of the board
      if ((currentPiece(a)(1) + tetraminoes.currentY + y) > gameBoard.rows - 1) {
        collisionSoundEffect()
        //make it occupied
        gameBoard.paintBoard(currentPiece, tetraminoes.currentX + x, tetraminoes.currentY)
        //empty the currentPiece to get the nextPiece
        currentPiece = List()
        return
      }
      // check whether it collides the other pieces
      if (collisionDetection(currentPiece(a), tetraminoes.currentX + x, tetraminoes.currentY + y)) {
        collisionSoundEffect()
        gameBoard.paintBoard(currentPiece, tetraminoes.currentX + x, tetraminoes.currentY)
        currentPiece = List()
        return
      }
    }// end for loop
    // move as usual
    moveSoundEffect()
    tetraminoes.move(x, y)
  }

  def collisionDetection(piece: Array[Int], currentX: Int, currentY: Int): Boolean = {
    if (gameBoard.board(piece(1) + currentY)(piece(0) + currentX) == 1) true else false
  }

}
