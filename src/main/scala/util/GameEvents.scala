package util

import model.CardInterface
import model.baseImp.*
import model.baseImp.Suit.Suit
import util.command.GameState

sealed trait GameEvent


//Reactions and Intro
case class TurnReactionMeh(color: String) extends GameEvent

case class TurnReactionBad(color: String) extends GameEvent

case class ShowColoredCat(color: String) extends GameEvent


//Game Mechanics
case object AskForGameMode extends GameEvent

case object PromptForPlayerName extends GameEvent

case class TotalPoints(player1Points: Int, player2Points: Int) extends GameEvent

case class GameOver(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int) extends GameEvent

case class UpdateGrid(grid: Grid) extends GameEvent

case class CardDrawn(playerName: String, card: String) extends GameEvent

case class PlayerTurn(playerName: String) extends GameEvent

case class CardPlacementSuccess(x: Int, y: Int, card: String, points: Int, player : String) extends GameEvent

case class RemoveCardFromHand(playerName: String, card: String) extends GameEvent

case class RemoveCardFromGrid(id: NumberCards, x: Int, y: Int) extends GameEvent

case class UpdatePlayer(player1: String) extends GameEvent

case class UpdatePlayers(player1: Player, player2: Player) extends GameEvent

case class ShowCardsForPlayer(cand: List[CardInterface]) extends GameEvent

//special case
case class StartCardPhaseOne(currentPlayer: Player, cards: AssistCardInterface) extends GameEvent //Game state?, or just not allow the select for it

case class StartCardPhaseTwo(currentPlayer: Player, cards: NumberCards) extends GameEvent

case class EndCardPhaseOne(currentPlayer: Player, cards: AssistCardInterface) extends GameEvent //Game state?, or just not allow the select for it

case class EndCardPhaseTwo(currentPlayer: Player, cards: NumberCards) extends GameEvent

case class FreezeEnemy(enemy: Player, currentPlayer: Player) extends GameEvent

case class DestroyEnemyCards(enemy: Player, currentPlayer: Player) extends GameEvent //basically remove card from grid

case class StealEnemyCard(enemy: Player, currentPlayer: Player, id: NumberCards) extends GameEvent

case class StopEnemyAttack(enemy: Player, currentPlayer: Player) extends GameEvent

case object AskForLoadGame extends GameEvent

case object GameSaved extends GameEvent

case object MakeNewGame extends GameEvent

case object SaveGameError extends GameEvent

case object LoadGameError extends GameEvent

case class GameLoaded(grid: List[(Int, Int, Option[CardInterface], Suit)], currentPlayer: Player, player1: Player, player2: Player, currentPlayerHand: List[CardInterface]) extends GameEvent

case class UndoEvent(state: GameState) extends GameEvent

case class RedoEvent(state: GameState) extends GameEvent

case object InitializeGUIForLoad extends GameEvent

case class UpdateLoadedGame(grid: List[(Int, Int, Option[CardInterface], Suit)], currentPlayer: Player, player1: Player, player2: Player, currentPlayerHand: List[CardInterface]) extends GameEvent

case object InvalidPlacement extends GameEvent

case class EmptyDeckBro(deck: Deck) extends GameEvent

case object UnknownEvent extends GameEvent