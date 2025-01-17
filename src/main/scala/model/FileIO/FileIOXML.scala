package model.FileIO

import controller.GameControllerInterface
import model.baseImp.{Grid, NumberCards, Player, Suit, Value}
import util.command.GameState
import util.grid.GridUtils
import scala.xml._

class FileIOXML extends FileIOInterface {

    override def save(game: GameControllerInterface, grid: Grid): String = {
        val currentState = game.getCurrentState
        val players = game.getPlayers

        val xml =
            <gameState>
                <players>
                    {players.map(player =>
                    <player>
                        <name>{player.name}</name>
                        <points>{player.points}</points>
                        <hand>
                            {player.getHand.map(card =>
                            <card>
                                <suit>{card.asInstanceOf[NumberCards].suit}</suit>
                                <value>{card.asInstanceOf[NumberCards].value}</value>
                                <color>{card.getColor}</color>
                            </card>
                        )}
                        </hand>
                    </player>
                )}
                </players>
                <grid>
                    {for {
                    x <- 0 until grid.size
                    y <- 0 until grid.size
                } yield {
                    val (cardOpt, cellColor) = grid.toArray(x)(y)
                    <cell>
                        <x>{x}</x>
                        <y>{y}</y>
                        <cellColor>{cellColor}</cellColor>
                        {cardOpt.map(card =>
                        <card>
                            <suit>{card.asInstanceOf[NumberCards].suit}</suit>
                            <value>{card.asInstanceOf[NumberCards].value}</value>
                            <color>{card.getColor}</color>
                        </card>
                    ).getOrElse(<card>Empty</card>)}
                    </cell>
                }}
                </grid>
                <currentPlayerIndex>{currentState.getCurrentPlayerIndex}</currentPlayerIndex>
                <points>{currentState.getPoints}</points>
            </gameState>

        scala.xml.XML.save("game.xml", xml, "UTF-8", xmlDecl = true, null)
        "Game saved successfully"
    }

    override def load(game: GameControllerInterface): String = {
        try {
            val file = scala.xml.XML.loadFile("game.xml")

            // Create empty grid with your specific implementation
            val size = 3
            val grid = GridUtils.createEmptyGrid(size)

            // Load players first
            val players = (file \\ "players" \\ "player").map { playerNode =>
                val name = (playerNode \ "name").text
                val points = (playerNode \ "points").text.toInt
                val hand = (playerNode \\ "hand" \\ "card").map(cardNode =>
                    if ((cardNode \ "suit").nonEmpty) createCard(cardNode) else null
                ).toList.filter(_ != null)

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
            (file \\ "grid" \\ "cell").foreach { cell =>
                val x = (cell \ "x").text.toInt
                val y = (cell \ "y").text.toInt
                val cellColor = (cell \ "cellColor").text
                val cardNode = (cell \ "card").head

                val cellColorSuit = Suit.withName(cellColor)
                grid.setColor(x, y, cellColorSuit)

                if ((cardNode \ "suit").nonEmpty) {
                    val card = createCard(cardNode)
                    grid.placeCard(x, y, card)
                }
            }

            val currentPlayerIndex = (file \\ "currentPlayerIndex").text.toInt
            val points = (file \\ "points").text.toInt

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

    private def createCard(cardNode: Node): NumberCards = {
        val suit = Suit.withName((cardNode \ "suit").text)
        val value = Value.withName((cardNode \ "value").text)
        val color = (cardNode \ "color").text
        NumberCards(suit, value)
    }
}