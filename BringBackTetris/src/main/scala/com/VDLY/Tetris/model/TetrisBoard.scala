package com.VDLY.Tetris.model

object TetrisBoard extends Initialize with Paint {

  val rows: Int = 20
  val columns: Int = 10
  var board: Array[Array[Int]] = Array.ofDim[Int](rows, columns)

  def initBoard(): Unit = {
    for (row <- 0 until rows) {
      for (col <- 0 until columns) {
        this.board(row)(col) = 0
      }
    }
  }

  def paintBoard(currentPiece: List[Array[Int]], currentX: Int, currentY: Int): Unit = {
    for (a <- currentPiece.indices) {
      val tmpCol = currentPiece(a)(0) + currentX
      val tmpRow = currentPiece(a)(1) + currentY
      this.board(tmpRow)(tmpCol) = 1
    }
  }
}
