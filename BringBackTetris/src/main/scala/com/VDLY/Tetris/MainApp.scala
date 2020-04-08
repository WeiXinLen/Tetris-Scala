package com.VDLY.Tetris

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.stage.{Modality, Stage}
import com.VDLY.Tetris.MainApp.getClass
import com.VDLY.Tetris.view.{GaiaRulesController, NormalRulesController}
import scalafx.scene.image.Image

object MainApp extends JFXApp {

  // transform path of RootLayout.fxml to InputStream for resource location.
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  // initialize the loader object.
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  // Load root layout from fxml file.
  loader.load()
  // retrieve the root component BorderPane from the FXML
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Tetris"
    icons += new Image(getClass.getResource("img/favicon.png").toURI.toString)
    scene = new Scene() {
      root = roots
    }
  }

  def showMain(): Unit = {
    val resource = getClass.getResource("view/Main.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)

  }

  def showNormalBoard(): Unit = {
    val resource = getClass.getResource("view/NormalBoard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showGaiaBoard(): Unit = {
    val resource = getClass.getResource("view/Board.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showNormalRules(): Boolean = {
    val resource = getClass.getResourceAsStream("view/NormalRules.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[NormalRulesController#Controller]

    val dialog = new Stage() {
      title = "Normal Rules"
      icons += new Image(getClass.getResource("img/favicon.png").toURI.toString)
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }

    }
    control.dialogStage = dialog
    dialog.showAndWait()
    control.okClicked
  }

  def showGaiaRules(): Boolean = {
    val resource = getClass.getResourceAsStream("view/GaiaRules.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource)
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[GaiaRulesController#Controller]

    val dialog = new Stage() {
      title = "Gaia Rules"
      icons += new Image(getClass.getResource("img/favicon.png").toURI.toString)
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    dialog.showAndWait()
    control.okClicked
  }

  stage.setResizable(false)
  showMain()
}
