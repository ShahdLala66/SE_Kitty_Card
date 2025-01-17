package model.FileIO

import controller.GameControllerInterface
import model.baseImp.{Grid, NumberCards, Player, Suit, Value}
import util.command.GameState
import util.grid.GridUtils
import play.api.libs.json._
import java.io.{PrintWriter, File}
import scala.io.Source

class FileIOJSON extends FileIOInterface {

  override def save(game: GameControllerInterface): String = {
    val currentState = game.getCurrentState
    val grid = currentState.getGrid
    val players = game.getPlayers

    val gameStateJson = Json.obj(
      "players" -> players.map(player =>
        Json.obj(
          "name" -> player.name,
          "points" -> player.points,
          "hand" -> player.getHand.map(card => card.asInstanceOf[NumberCards]).map(card =>
            Json.obj(
              "suit" -> card.suit.toString,
              "value" -> card.value.toString,
              "color" -> card.getColor
            )
          )
        )
      ),
      "grid" -> {
        val cells = for {
          x <- 0 until grid.size
          y <- 0 until grid.size
        } yield {
          val (cardOpt, cellColor) = grid.toArray(x)(y)
          Json.obj(
            "x" -> x,
            "y" -> y,
            "cellColor" -> cellColor.toString,
            "card" -> (cardOpt match {
              case Some(card) => Json.obj(
                "suit" -> card.asInstanceOf[NumberCards].suit.toString,
                "value" -> card.asInstanceOf[NumberCards].value.toString,
                "color" -> card.getColor
              )
              case None => JsString("Empty")
            })
          )
        }
        Json.toJson(cells)
      },
      "currentPlayerIndex" -> currentState.getCurrentPlayerIndex,
      "points" -> currentState.getPoints
    )

    val pw = new PrintWriter(new File("game.json"))
    pw.write(Json.prettyPrint(gameStateJson))
    pw.close()
    "Game saved successfully"
  }

  override def load(game: GameControllerInterface): String = {
    try {
      val source = Source.fromFile("game.json")
      val jsonString = source.mkString
      source.close()

      val json = Json.parse(jsonString)

      // Create empty grid with your specific implementation
      val size = 3
      val grid = GridUtils.createEmptyGrid(size)

      // Load players
      val players = (json \ "players").as[JsArray].value.map { playerJson =>
        val name = (playerJson \ "name").as[String]
        val points = (playerJson \ "points").as[Int]
        val hand = (playerJson \ "hand").as[JsArray].value.map { cardJson =>
          if ((cardJson \ "suit").isDefined) createCardFromJson(cardJson.as[JsValue]) else null
        }.toList.filter(_ != null)

        val player = Player(name)
        player.setPoints(points)
        player.setHand(hand)
        player
      }.toList

      // Ensure players list is not empty
      if (players.isEmpty) {
        throw new NoSuchElementException("No players found in the saved game")
      }

      // Load grid state
      (json \ "grid").as[JsArray].value.foreach { cell =>
        val x = (cell \ "x").as[Int]
        val y = (cell \ "y").as[Int]
        val cellColor = (cell \ "cellColor").as[String]
        val cardJson = (cell \ "card").as[JsValue]

        val cellColorSuit = Suit.withName(cellColor)
        grid.setColor(x, y, cellColorSuit)

        if (cardJson != JsString("Empty")) {
          val card = createCardFromJson(cardJson)
          grid.placeCard(x, y, card)
        }
      }

      val currentPlayerIndex = (json \ "currentPlayerIndex").as[Int]
      val points = (json \ "points").as[Int]

      // Create and return new state
      val newState = new GameState(grid, players, currentPlayerIndex, points)
      game.loadGameState(newState)
      "Game loaded successfully"
    } catch {
      case e: Exception =>
        println(s"Error loading game: ${e.getMessage}")
        e.printStackTrace()
        throw e
    }
  }

  private def createCardFromJson(json: JsValue): NumberCards = {
    val suit = Suit.withName((json \ "suit").as[String])
    val value = Value.withName((json \ "value").as[String])
    NumberCards(suit, value)
  }
}