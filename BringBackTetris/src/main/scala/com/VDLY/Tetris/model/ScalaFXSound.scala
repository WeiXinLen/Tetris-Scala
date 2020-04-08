package com.VDLY.Tetris.model
import scalafx.scene.media.{Media, MediaPlayer}
import com.VDLY.Tetris.MainApp.getClass

trait ScalaFXSound extends MPlayer {

  def chooseSound(filename: String, repeat: Boolean = false): Unit = {
    val media = new Media(getClass.getResource(filename).toURI.toString)
    val player = new MediaPlayer(media)
    if (repeat)
      player.play()
    else{
      player.stop()
      player.play()
    }
  }

  def moveSoundEffect(): Unit = chooseSound("../sound/move.mp3")

  def collisionSoundEffect(): Unit = chooseSound("../sound/collision.mp3")

  def hitBorderSoundEffect(): Unit = chooseSound("../sound/hitBorder.mp3")

  def clearSoundEffect(): Unit = chooseSound("../sound/clear.mp3")

  def pauseSoundEffect(): Unit = chooseSound("../sound/pause.mp3")

  def rotateSoundEffect(): Unit = chooseSound("../sound/rotate.mp3")

  def gameOverSoundEffect(): Unit = chooseSound("../sound/gameOver.mp3")

}
