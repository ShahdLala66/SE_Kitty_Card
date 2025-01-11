// src/main/scala/util/GameEvents.scala
package util

import model.baseImp.{AssistCardInterface, Deck, Grid, NumberCards, Player}
import model.CardInterface
import util.command.GameState

sealed trait GameEvent


//Reactions and Intro
case class TurnReactionMeh(color: String) extends GameEvent
case class TurnReactionBad(color: String) extends GameEvent
case class ShowColoredCat(color: String) extends GameEvent


//Game Mode setten , single und multi 
case object AskForGameMode extends GameEvent


//Game Starten
//case object GameStart extends GameEvent
case object PromptForPlayerName extends GameEvent
//case object StrategySelection extends GameEvent


//Game Over
case class  TotalPoints(player1Points: Int, player2Points: Int) extends GameEvent
case class GameOver(player1Name: String, player1Points: Int, player2Name: String, player2Points: Int) extends GameEvent

//debate whether we should use strings and ints or actual objects

//Game Events
case class UpdateGrid(grid: Grid) extends GameEvent
case class CardDrawn(playerName: String, card: String) extends GameEvent
case class PlayerTurn(playerName: String) extends GameEvent
case class CardPlacementSuccess(x: Int, y: Int, card: String, points: Int) extends GameEvent
case class RemoveCardFromHand(playerName: String, card: String) extends GameEvent
case class RemoveCardFromGrid(id: NumberCards, x: Int, y: Int) extends GameEvent
case class UpdatePlayer(player1: String) extends GameEvent
case class UpdatePlayers(player1: Player, player2: Player) extends GameEvent

case class ShowCardsForPlayer(cand : List[CardInterface]) extends GameEvent
//case class UpdateHand ( card : CardInterface) extends GameEvent

//special case
case class StartCardPhaseOne(currentPlayer: Player, cards: AssistCardInterface) extends GameEvent //Game state?, or just not allow the select for it
case class StartCardPhaseTwo(currentPlayer: Player, cards: NumberCards) extends GameEvent
case class EndCardPhaseOne(currentPlayer : Player, cards : AssistCardInterface) extends GameEvent //Game state?, or just not allow the select for it
case class EndCardPhaseTwo(currentPlayer : Player, cards : NumberCards) extends GameEvent
case class FreezeEnemy(enemy : Player, currentPlayer : Player) extends GameEvent
case class DestroyEnemyCards(enemy : Player, currentPlayer : Player) extends GameEvent //basically remove card from grid
case class StealEnemyCard(enemy : Player, currentPlayer : Player, id : NumberCards) extends GameEvent
case class StopEnemyAttack(enemy : Player, currentPlayer : Player) extends GameEvent


//Undo/Redo
case class UndoEvent(state: GameState) extends GameEvent
case class RedoEvent(state: GameState) extends GameEvent


//Error Stuff
case object InvalidPlacement extends GameEvent              // bei richtiger Platzierung, allerdings Grid Place schon belegt
case class EmptyDeckBro(deck : Deck) extends GameEvent