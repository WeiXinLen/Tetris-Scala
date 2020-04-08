package com.VDLY.Tetris.model

object GaiaBoard extends Initialize{

  val rows: Int = 20
  val columns: Int = 10
  var board: Array[Array[Int]] = Array.ofDim[Int](rows, columns)

  def initBoard(): Unit = {
    for (row <- 0 until rows) {
      for (col <- 0 until columns) {
        if (row >= rows - 3 && row <= rows - 1) {
          if (col >= (columns/2 -2) && col <= (columns/2 +1) )
            this.board(row)(col) = 1
          else
            this.board(row)(col) = 0
        }
        else
          this.board(row)(col) = 0
      }
    }
  }

  def paintBoard(branchBlock: List[List[Array[Int]]], currentPiece: List[Array[Int]], currentX: Int, currentY: Int): Unit = {
    for (a <- currentPiece.indices) {
      val tmpCol = currentPiece(a)(0) + currentX
      val tmpRow = currentPiece(a)(1) + currentY
      // if currentPiece is one of the orientation of branchBlock
      if (branchBlock.contains(currentPiece))
        this.board(tmpRow)(tmpCol) = 2
      else
      // for other blocks beside branchBlock
        this.board(tmpRow)(tmpCol) = 1
    }
  }

}
