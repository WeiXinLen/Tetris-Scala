package com.VDLY.Tetris.model

class Tetramino {

  var currentX: Int = 4
  var currentY: Int = 0
  var currentZ: Int = 0
  // all the blocks
  val I = List(
    List(Array(0,0),Array(0,1),Array(0,2),Array(0,3)),
    List(Array(0,0),Array(1,0),Array(2,0),Array(3,0)),
  )
  val L = List(
    List(Array(0,0),Array(0,1),Array(0,2),Array(1,2)),
    List(Array(0,1),Array(0,0),Array(1,0),Array(2,0)),
    List(Array(0,0),Array(1,0),Array(1,1),Array(1,2)),
    List(Array(0,1),Array(1,1),Array(2,1),Array(2,0)),
  )
  val J = List(
    List(Array(1,0),Array(0,0),Array(0,1),Array(0,2)),
    List(Array(0,0),Array(1,0),Array(2,0),Array(2,1)),
    List(Array(0,2),Array(1,2),Array(1,1),Array(1,0)),
    List(Array(0,0),Array(0,1),Array(1,1),Array(2,1)),
  )
  val T = List(
    List(Array(1,0),Array(0,1),Array(1,1),Array(1,2)),
    List(Array(1,0),Array(0,1),Array(1,1),Array(2,1)),
    List(Array(0,0),Array(0,1),Array(1,1),Array(0,2)),
    List(Array(0,0),Array(1,0),Array(2,0),Array(1,1)),
  )
  val O = List(
    List(Array(0,0),Array(0,1),Array(1,0),Array(1,1)),
    List(Array(0,0),Array(0,1),Array(1,0),Array(1,1)),
  )
  val S = List(
    List(Array(0,0),Array(0,1),Array(1,1),Array(1,2)),
    List(Array(0,1),Array(1,1),Array(1,0),Array(2,0)),
  )
  val Z = List(
    List(Array(1,0),Array(1,1),Array(0,1),Array(0,2)),
    List(Array(0,0),Array(1,0),Array(1,1),Array(2,1)),
  )
  val B = List(
    List(Array(0,0), Array(0,1)),
    List(Array(0,1), Array(1,1)),
  )

  def rotate(currentTetrad: List[List[Array[Int]]], currentZ: Int): List[Array[Int]] = {
    if (currentZ + 1 >= currentTetrad.size)
      this.currentZ = 0
    else
      this.currentZ = currentZ + 1
    // once evaluate return the same piece but with different orientation
    currentTetrad(this.currentZ)
  }

  //move
  def move(x: Int, y: Int): Unit = {
    this.currentX += x
    this.currentY += y
  }
}




