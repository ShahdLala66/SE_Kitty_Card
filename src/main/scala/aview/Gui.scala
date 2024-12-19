package aview

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.Font

case class Card(suit: String, value: String)

object MyApp extends JFXApp3 {
  private val gridPane = new GridPane {
    layoutX = 84
    layoutY = 83
    prefHeight = 340
    prefWidth = 460
    columnConstraints = Seq(
      new ColumnConstraints {
        hgrow = Priority.Sometimes
        maxWidth = 221.2
        prefWidth = 156.2
      },
      new ColumnConstraints {
        hgrow = Priority.Sometimes
        maxWidth = 310
        prefWidth = 170.4
      },
      new ColumnConstraints {
        hgrow = Priority.Sometimes
        maxWidth = 310
        prefWidth = 144.0
      }
    )
    rowConstraints = Seq(
      new RowConstraints {
        vgrow = Priority.Sometimes
        maxHeight = 123.8
        prefHeight = 77.6
      },
      new RowConstraints {
        vgrow = Priority.Sometimes
        maxHeight = 184
        prefHeight = 133.6
      },
      new RowConstraints {
        vgrow = Priority.Sometimes
        maxHeight = 104.8
        prefHeight = 104.8
      }
    )
  }

  private var input: String = ""

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "ScalaFX Application"
      scene = new Scene {
        root = new VBox {
          prefHeight = 400
          prefWidth = 640
          children = Seq(
            new MenuBar {
              VBox.setVgrow(this, Priority.Never)
              menus = Seq(
                new Menu("Edit") {
                  items = Seq(
                    new MenuItem("Undo"),
                    new MenuItem("Redo"),
                    new SeparatorMenuItem(),
                    new MenuItem("Select All"),
                    new MenuItem("Unselect All")
                  )
                },
                new Menu("Help") {
                  items = Seq(
                    new MenuItem("About MyHelloApp")
                  )
                }
              )
            },
            new AnchorPane {
              VBox.setVgrow(this, Priority.Always)
              children = Seq(
                new ImageView {
                  disable = true
                  fitHeight = 550
                  fitWidth = 674
                  layoutX = -1
                  pickOnBounds = true
                  preserveRatio = true
                  image = new Image(getClass.getResourceAsStream("/IMG_0807.png"))
                },
                new Label("dfghjm,mgdf") {
                  alignment = scalafx.geometry.Pos.Center
                  layoutX = 32
                  layoutY = 14
                  textFill = scalafx.scene.paint.Color.web("#9f9f9f")
                  font = Font(18)
                },
                new Label("dfghjm,mgdf") {
                  alignment = scalafx.geometry.Pos.Center
                  layoutX = 517
                  layoutY = 14
                  textFill = scalafx.scene.paint.Color.web("#9f9f9f")
                  font = Font(18)
                },
                new Label("11") {
                  alignment = scalafx.geometry.Pos.Center
                  layoutX = 198
                  layoutY = 14
                  textFill = scalafx.scene.paint.Color.web("#9f9f9f")
                  font = Font(18)
                },
                new Label("11") {
                  alignment = scalafx.geometry.Pos.Center
                  layoutX = 439
                  layoutY = 14
                  textFill = scalafx.scene.paint.Color.web("#9f9f9f")
                  font = Font(18)
                },
                new TextField {
                  layoutX = 253
                  layoutY = 41
                  onAction = _ => input = text.value
                },
                new Button("SubmitInput") {
                  layoutX = 287
                  layoutY = 67
                  onAction = _ => {
                    input = text.value
                    handleInput()
                  }
                },
                gridPane
              )
            }
          )
        }
      }
    }
  }

  def getCardImageFileName(suit: String, value: String): String = {
    (suit, value) match {
      case ("yellow", "1") => "yellow_card_1.png"
      case ("red", "2") => "red_card_2.png"
      case ("blue", "3") => "blue_card_3.png"
      case ("green", "4") => "green_card_4.png"
      case ("purple", "5") => "purple_card_5.png"
      case ("brown", "6") => "brown_card_6.png"
      case _ => "default_card.png"
    }
  }

  def placeCard(card: Card, row: Int, col: Int): Unit = {
    val cardImageFileName = getCardImageFileName(card.suit, card.value)
    val cardImage = new Image(getClass.getResourceAsStream(s"/$cardImageFileName"))
    val imageView = new ImageView(cardImage) {
      fitHeight = 100
      fitWidth = 100
      preserveRatio = true
    }
    gridPane.add(imageView, col, row)
  }

  def handleInput(): Unit = {
    val parts = input.split(" ")
    if (parts.length == 4) {
      val suit = parts(0)
      val value = parts(1)
      val row = parts(2).toInt
      val col = parts(3).toInt
      val card = Card(suit, value)
      placeCard(card, row, col)
    } else {
      println("Invalid input format. Expected format: <suit> <value> <row> <col>")
    }
  }
}