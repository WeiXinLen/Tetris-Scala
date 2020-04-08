package com.VDLY.Tetris.model

import scalafx.beans.property.StringProperty

object GaiaRules {

    val RULE_1 = new StringProperty("This goal of this game is to get many tetris blocks on the tree branch\n")
    val RULE_2 = new StringProperty("The tree branch is in the middle, lower part of the entire board\n")
    val RULE_3 = new StringProperty("Avoid placing the tetris blocks at the red box, otherwise the game will be over\n")
    val RULE_4 = new StringProperty("Special blocks are tetris blocks that are made of only 2 boxes, \n" +
      "this blocks can be placed anywhere, including in the red boxes\n")
    val RULE_5 = new StringProperty("So keep stacking the leaves, plant more tress on ground using the special blocks,\n" +
      " and get as high as you can!")
}
