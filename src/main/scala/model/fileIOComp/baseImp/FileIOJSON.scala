// FileIOJSON.scala
package model.fileIOComp.baseImp

import controller.GameControllerInterface
import model.fileIOComp.FileIOInterface
import model.gameModelComp.baseImp.*
import util.command.GameState
import util.grid.GridUtils
import play.api.libs.json.*

import scala.util.{Failure, Success, Try}

class FileIOJSON extends FileIOInterface {

    override def save(game: GameControllerInterface, grid: Grid): String = {
        val currentState = game.getCurrentState
        val players = game.getPlayers

        val json = Json.obj(
            "players" -> players.map { player =>
                Json.obj(
                    "name" -> player.name,
                    "points" -> player.points,
                    "hand" -> player.getHand.map { card =>
                        Json.obj(
                            "suit" -> card.asInstanceOf[NumberCards].suit.toString,
                            "value" -> card.asInstanceOf[NumberCards].value.toString,
                            "color" -> card.getColor
                        )
                    }
                )
            },
            "grid" -> Json.arr(
                for {
                    x <- 0 until grid.size
                    y <- 0 until grid.size
                } yield {
                    val (cardOpt, cellColor) = grid.toArray(x)(y)
                    Json.obj(
                        "x" -> x,
                        "y" -> y,
                        "cellColor" -> cellColor.toString, 
                        "card" -> cardOpt.map { card =>
                            Json.obj(
                                "suit" -> card.suit.toString, 
                                "value" -> card.value.toString, 
                                "color" -> card.getColor
                            )
                        }.getOrElse(Json.obj("card" -> "Empty"))
                    )
                }
            ),
            "currentPlayerIndex" -> currentState.getCurrentPlayerIndex,
            "points" -> currentState.getPoints
        )

        val jsonString = Json.prettyPrint(json)
        import java.io.PrintWriter
        new PrintWriter("game.json") {
            write(jsonString)
            close()
        }
        "Game saved successfully"
    }

    
    override def load(game: GameControllerInterface): String = {
        Try {
            import scala.io.Source
            val jsonString = Source.fromFile("game.json").getLines.mkString
            val json = Json.parse(jsonString)

            val size = 3
            val grid = GridUtils.createEmptyGrid(size)

            val players = (json \ "players").as[Seq[JsValue]].map { playerJson =>
                val name = (playerJson \ "name").as[String]
                val points = (playerJson \ "points").as[Int]
                val hand = (playerJson \ "hand").as[Seq[JsValue]].flatMap { cardJson =>
                    (cardJson \ "suit").asOpt[String].map(_ => createCard(cardJson))
                }.toList

                val player = Player(name)
                player.setPoints(points)
                player.setHand(hand)
                player
            }.toList

            if (players.isEmpty) {
                throw new NoSuchElementException("No players found in the saved game")
            }

            (json \ "grid").as[Seq[JsValue]].foreach { cellJson =>
                val x = (cellJson \ "x").as[Int]
                val y = (cellJson \ "y").as[Int]
                val cellColor = (cellJson \ "cellColor").as[String]
                val cellColorSuit = Suit.withName(cellColor)
                grid.setColor(x, y, cellColorSuit)

                (cellJson \ "card").asOpt[JsValue].foreach { cardJson =>
                    if ((cardJson \ "suit").asOpt[String].isDefined) {
                        val card = createCard(cardJson)
                        grid.placeCard(x, y, card)
                    }
                }
            }

            val currentPlayerIndex = (json \ "currentPlayerIndex").as[Int]
            val points = (json \ "points").as[Int]

            val newState = new GameState(grid, players, currentPlayerIndex, points)
            game.loadGameState(newState)
            "Game loaded successfully"
        } match {
            case Success(message) => message
            case Failure(e) =>
                println(s"Error loading game: ${e.getMessage}")
                e.printStackTrace()
                "Error loading game"
        }
    }
    
    def createCard(cardJson: JsValue): NumberCards = {
        val suit = Suit.withName((cardJson \ "suit").as[String])
        val value = Value.withName((cardJson \ "value").as[String])
        NumberCards(suit, value)
    }
}
