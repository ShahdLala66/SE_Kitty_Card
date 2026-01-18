package controller.baseImp

import controller.{GameControllerInterface, SessionManager}
import model.*
import model.fileIOComp.FileIOInterface
import model.gameModelComp.CardInterface
import model.gameModelComp.baseImp.Suit.Suit
import model.gameModelComp.baseImp.*
import util.*
import util.command.{CommandManager, GameState, PlaceCardCommand}
import util.grid.GridFactory

import scala.compiletime.uninitialized
import scala.util.{Failure, Success, Try}

class GameController(deck: Deck = new Deck(), hand: Hand = new Hand(), fileIOInterface: FileIOInterface = model.fileIOComp.baseImp.FileIOJSON()) extends GameControllerInterface(deck: Deck, hand: Hand, fileIOInterface: FileIOInterface) {
  var gameMode: GameMode = uninitialized
  var grid: Grid = uninitialized
  var observers: List[Observer] = List()
  var playerIsAtTurn = true
  var currentPlayer: Player = uninitialized
  var player1: Player = uninitialized
  var player2: Player = uninitialized
  val commandManager = new CommandManager()
  var player1String: String = ""
  var player2String: String = ""
  var counter = 0
  
  // Session management
  val sessionManager = new SessionManager()
  var currentSessionId: Option[String] = None
  var currentPlayerId: Option[String] = None
  var lastPlayedCardWasAce: Boolean = false
  
  def getState: String = {
    if (currentPlayer == null) {
      "Game not started"
    } else {
      s"Current player: ${currentPlayer.name}, Player 1: ${player1.name}, Player 2: ${player2.name}"
    }
    
  }

  def getStateElements: Seq[String] = {
    // Try to use session-specific state if available
    getCurrentSession match {
      case Some(session) =>
        val sessionPlayer = session.currentPlayer.getOrElse(currentPlayer)
        val p1 = session.player1.getOrElse(player1)
        val p2 = session.player2.getOrElse(player2)
        if (sessionPlayer == null) {
          Seq("Game not started")
        } else {
          Seq(sessionPlayer.name, p1.name, p2.name)
        }
      case None =>
        // Fallback to global state
        if (currentPlayer == null) {
          Seq("Game not started")
        } else {
          Seq(currentPlayer.name, player1.name, player2.name)
        }
    }
  }

  def startGame(): Unit = {
    //notifyObservers(AskForGameMode)
    
  }


  // GAME INTRO ________________________________________________
  def setGameMode(mode: String): Unit = {
    mode.toLowerCase match {
      case "s" => gameMode = new SinglePlayerMode()
        notifyObservers(AskForLoadGame)

      case "m" => gameMode = new MultiPlayerMode()
        notifyObservers(AskForLoadGame)

      case _ =>
        println(s"Invalid game mode: $mode. Defaulting to Multi Player mode.")
        gameMode = new MultiPlayerMode()
    }
  }

  def startMultiPlayerGame(): Unit = {
    val (p1, p2) = addPlayers(player1String, player2String)
    player1 = p1
    player2 = p2
    currentPlayer = player1
    notifyObservers(UpdatePlayer(currentPlayer.name))
    distributeInitialCards()
    notifyObservers(UpdateGrid(grid))
    //notifyObservers(ShowCardsForPlayer(currentPlayer.getHand))
    startGameLoop()
  }

  def promptForPlayerName(player1String: String, player2String: String): Unit = {
    if (counter == 0) {
      this.player1String = player1String
      this.player2String = player2String
      counter += 1
      notifyObservers(UpdatePlayers(Player(player1String), Player(player2String)))
    } else {
      println("Game already started")
    }
    gameMode.startGame(this)
  }

  // LOGIC FOR THE GAME ______________________________________

  def startGameLoop(): Unit = {
    while (playerIsAtTurn) {
      Option(getCurrentplayer) match {
        case Some(player) =>
          playerIsAtTurn = false
          notifyObservers(PlayerTurn(getCurrentplayer.name))
        case None =>
      }
    }
  }

  private def addPlayers(player1Name: String, player2Name: String): (Player, Player) = {
    val player1 = Player(player1Name)
    val player2 = Player(player2Name)
    (player1, player2)
  }

  // for game start

  private def distributeInitialCards(): Unit = {
    for (_ <- 1 to 3) {
      player1.drawCard(deck)
      player2.drawCard(deck)
    }
  }

  def handleCommand(command: String): Unit = {
    command match {
      case "save" =>
        fileIOInterface.save(this, grid)
        notifyObservers(GameSaved)
      case "load" =>
        Try(fileIOInterface.load(this)) match {
          case Success(_) =>
          case Failure(e) => println(s"Error loading game: ${e.getMessage}")
        }
      case "start" =>
        grid = GridFactory.createGrid(3)
        currentState = new GameState(grid, List(player1, player2), 0, 0)
        notifyObservers(PromptForPlayerName)
      case "undo" => processCardPlacement("undo")
      case "redo" => processCardPlacement("redo")
      case "draw" => drawCardForCurrentPlayer()
    }
  }

  def drawCardForCurrentPlayer(): Unit = {
    // Check if we're in a session
    getCurrentSession match {
      case Some(session) =>
        // Use session-specific deck and player
        val sessionDeck = session.deck.getOrElse(deck)
        val sessionPlayer = session.currentPlayer.getOrElse(currentPlayer)
        sessionPlayer.drawCard(sessionDeck) match {
          case Some(card) =>
            notifyObservers(CardDrawn(sessionPlayer.name, card.toString))
            // Switch to other player in session
            session.currentPlayer = if (session.currentPlayer == session.player1) session.player2 else session.player1
            sessionManager.switchTurn(session)
            notifyObservers(UpdatePlayer(session.currentPlayer.get.name))
          case None =>
            notifyObservers(InvalidPlacement)
        }
      case None =>
        // Fallback to old behavior
        currentPlayer.drawCard(deck) match {
          case Some(card) =>
            notifyObservers(CardDrawn(currentPlayer.name, card.toString))
            switchTurns()
          case None =>
            notifyObservers(InvalidPlacement)
        }
    }
  }

  def handleCardPlacement(cardIndex: Int, x: Int, y: Int): Boolean = {
    // Check if we're in a session
    getCurrentSession match {
      case Some(session) =>
        // Use session-specific state
        val sessionGrid = session.grid.getOrElse(grid)
        val sessionPlayer = session.currentPlayer.getOrElse(currentPlayer)
        
        println(s"[GameController] [Session ${session.sessionId}] handleCardPlacement: cardIndex=$cardIndex, x=$x, y=$y")
        println(s"[GameController] [Session ${session.sessionId}] Current player: ${sessionPlayer.name}, hand size: ${sessionPlayer.getHand.size}")
        
        sessionPlayer.getHand.lift(cardIndex) match {
          case Some(card: NumberCards) =>
            println(s"[GameController] [Session ${session.sessionId}] Found card at index $cardIndex: $card")
            val gridPlaceSuccess = sessionGrid.placeCard(x, y, card)
            println(s"[GameController] [Session ${session.sessionId}] Grid placement result: $gridPlaceSuccess")
            
            if (gridPlaceSuccess) {
              val pointsEarned = sessionGrid.calculatePoints(x, y)
              sessionPlayer.addPoints(pointsEarned)
              notifyObservers(CardPlacementSuccess(x, y, card.toString, pointsEarned))
              sessionPlayer.removeCard(card)
              
              // Track placement in session
              session.gridPlacements((x, y)) = if (session.currentPlayer == session.player1) "player1" else "player2"
              
              // Check for special cards
              val isAce = card.value.equals(Value.One)
              val isSeven = card.value.equals(Value.Seven)
              
              if (isAce) {
                // Ace: Player keeps turn (don't switch)
                println(s"[GameController] [Session ${session.sessionId}] Ace played - player keeps turn")
                notifyObservers(FreezeEnemy)
              } else if (isSeven) {
                // Seven (Bombe): Remove card from opponent's hand
                val opponent = if (session.currentPlayer == session.player1) session.player2 else session.player1
                opponent.foreach { opp =>
                  if (opp.getHand.nonEmpty) {
                    opp.removeCard(opp.getHand.head)
                    println(s"[GameController] [Session ${session.sessionId}] Seven played - removed card from opponent")
                  }
                }
                // Switch to other player
                session.currentPlayer = if (session.currentPlayer == session.player1) session.player2 else session.player1
                sessionManager.switchTurn(session)
              } else {
                // Normal card: Switch to other player in session
                session.currentPlayer = if (session.currentPlayer == session.player1) session.player2 else session.player1
                sessionManager.switchTurn(session)
              }
              
              notifyObservers(UpdatePlayer(session.currentPlayer.get.name))
              notifyObservers(UpdateGrid(sessionGrid))
              
              if (isSessionGameOver(session)) {
                displaySessionFinalScores(session)
              }
              true
            } else {
              notifyObservers(InvalidPlacement)
              sessionPlayer.removeCard(card)
              
              // Switch to other player on invalid placement
              session.currentPlayer = if (session.currentPlayer == session.player1) session.player2 else session.player1
              sessionManager.switchTurn(session)
              
              notifyObservers(UpdatePlayer(session.currentPlayer.get.name))
              notifyObservers(UpdateGrid(sessionGrid))
              false
            }
          case _ =>
            println(s"[GameController] [Session ${session.sessionId}] No card found at index $cardIndex")
            notifyObservers(InvalidPlacement)
            false
        }
        
      case None =>
        // Fallback to old behavior for non-session games
        lastPlayedCardWasAce = false // Reset flag
        val success = processCardPlacement(s"$cardIndex $x $y")
        if (success) {
          // Only switch turns if it wasn't an Ace (Ace allows player to go again)
          if (!lastPlayedCardWasAce) {
            switchTurns()
          } else {
            println("[GameController] Ace played - player keeps turn")
          }
          playerIsAtTurn = true
        }
        if (!isGameOver) {
          playerIsAtTurn = true
        } else {
          displayFinalScores()
        }
        success
    }
  }

  private def processCardPlacement(input: String): Boolean = {
    input.trim.toLowerCase match {
      case "undo" =>
        switchTurns()
        executeUndoRedo(commandManager.undo, UndoEvent(currentState))
      case "redo" =>
        switchTurns()
        executeUndoRedo(commandManager.redo, RedoEvent(currentState))

      case _ =>
        Try {
          val parts = input.split(" ")
          val cardIndex = parts(0).toInt
          val x = parts(1).toInt
          val y = parts(2).toInt

          println(s"[GameController] processCardPlacement: cardIndex=$cardIndex, x=$x, y=$y")
          println(s"[GameController] Current player hand size: ${currentPlayer.getHand.size}")

          currentPlayer.getHand.lift(cardIndex) match {
            case Some(card: NumberCards) =>
              println(s"[GameController] Found card at index $cardIndex: $card")
              val gridPlaceSuccess = grid.placeCard(x, y, card)
              println(s"[GameController] Grid placement result: $gridPlaceSuccess")
              if (gridPlaceSuccess) {
                val pointsEarned = grid.calculatePoints(x, y)
                val command = new PlaceCardCommand(grid, card, currentPlayer, pointsEarned, (x, y))
                currentState = commandManager.executeCommand(command, currentState)
                currentPlayer.addPoints(pointsEarned)
                notifyObservers(CardPlacementSuccess(x, y, card.toString, pointsEarned))
                currentPlayer.removeCard(card)
                // Set flag if Ace is played (player keeps turn)
                lastPlayedCardWasAce = card.value.equals(Value.One)
                if (lastPlayedCardWasAce) {
                  notifyObservers(FreezeEnemy)
                } else if (card.value.equals(Value.Seven)) {
                  if (player2.getHand.nonEmpty) {
                    if (currentPlayer == player1) {
                      player2.removeCard(player2.getHand.head)
                    }
                    else {
                      player1.removeCard(player1.getHand.head)
                    }
                  }
                  currentPlayer.removeCard(card)
                }
                true
              } else {
                notifyObservers(InvalidPlacement)
                currentPlayer.removeCard(card)
                switchTurns()

                false
              }
            case _ =>
              println(s"[GameController] No card found at index $cardIndex")
              notifyObservers(InvalidPlacement)
              false
          }
        } match {
          case Success(result) => 
            println(s"[GameController] Final result: $result")
            result
          case Failure(_) =>
            notifyObservers(InvalidPlacement)
            false
        }
    }
  }

  private def switchTurns(): Unit = {
    currentPlayer = if (currentPlayer == player1) player2 else player1
    
    // Update session turn tracking if in session mode
    currentSessionId.foreach { sessionId =>
      sessionManager.getSession(sessionId).foreach { session =>
        sessionManager.switchTurn(session)
        currentPlayerId = session.currentPlayerId
        println(s"[GameController] Switched turn in session $sessionId to playerId $currentPlayerId")
      }
    }
    
    notifyObservers(UpdatePlayer(currentPlayer.name))
    notifyObservers(UpdateGrid(grid))
  }

  private def displayFinalScores(): Unit = {
    notifyObservers(GameOver(player1.name, player1.points, player2.name, player2.points))

  }

  def isGameOver: Boolean = {
    // Try to use session-specific state if available
    getCurrentSession match {
      case Some(session) => isSessionGameOver(session)
      case None => deck.size <= 0 || grid.isFull
    }
  }

  def getWinner(): Option[String] = {
    // Try to use session-specific state if available
    getCurrentSession match {
      case Some(session) =>
        (session.player1, session.player2) match {
          case (Some(p1), Some(p2)) =>
            if (p1.points > p2.points) Some(p1.name)
            else if (p2.points > p1.points) Some(p2.name)
            else None // Draw
          case _ => None
        }
      case None =>
        if (player1.points > player2.points) Some(player1.name)
        else if (player2.points > player1.points) Some(player2.name)
        else None // Draw
    }
  }


  // OBSERVER PATTERN AND HELPER METHODS  __________________________________________
  def askForInputAgain(): Unit = {
    // For session-based games, this flag is not used
    // Sessions manage turn state via SessionManager
    playerIsAtTurn = true
  }

  def askForGameLoad(): Unit = {
    notifyObservers(AskForLoadGame)
  }


  // INPUT OUTPUT; SAVE GAME LOAD GAME : REDO UNDO __________________________________________

  def loadGameState(state: GameState): Unit = {
    Try {
      println("Starting load process...")
      currentState = state
      gameMode.loadGame(this, state)
      println("Game loaded successfully")
    } match {
      case Success(_) =>
      case Failure(e) =>
        println(s"Error in loadGameState: ${e.getMessage}")
        e.printStackTrace()
        throw e
    }
  }

  private def executeUndoRedo(action: () => Option[GameState], event: GameEvent): Boolean = {
    action() match {
      case Some(state) =>
        currentState = state
        notifyObservers(event)

        notifyObservers(UpdateGrid(grid))
        //notifyObservers(ShowCardsForPlayer(currentPlayer.getHand))
        true
      case None =>
        false
    }
  }



  // GETTER METHODS ____________________________________________

  def getGridColors: List[(Int, Int, Option[CardInterface], Suit)] = {
    // Try to use session-specific grid if available
    getCurrentSession match {
      case Some(session) =>
        val sessionGrid = session.grid.getOrElse(grid)
        getGridColorsFromGrid(sessionGrid)
      case None =>
        // Fallback to global grid
        getGridColorsFromGrid(grid)
    }
  }

  def getGridColorsFromGrid(grid: Grid): List[(Int, Int, Option[CardInterface], Suit)] = {
    grid.toArray.zipWithIndex.flatMap { case (row, x) =>
      row.zipWithIndex.map { case ((card, color), y) =>
        (x, y, card, color)
      }
    }.toList
  }

  def getPlayer1: String = {
    getCurrentSession match {
      case Some(session) => session.player1.map(_.name).getOrElse(player1.name)
      case None => player1.name
    }
  }

  def getPlayer2: String = {
    getCurrentSession match {
      case Some(session) => session.player2.map(_.name).getOrElse(player2.name)
      case None => player2.name
    }
  }

  def getPlayers: List[Player] = {
    getCurrentSession match {
      case Some(session) => List(session.player1, session.player2).flatten
      case None => List(player1, player2)
    }
  }

  def getCurrentState: GameState = currentState

  def getObserversString : String = observers.map(_.toString).mkString(", ")

  def getCurrentplayer: Player = {
    getCurrentSession match {
      case Some(session) => session.currentPlayer.getOrElse(currentPlayer)
      case None => currentPlayer
    }
  }

  def getCurrentPlayerString: String = currentPlayer.name

  def getGridColor(x: Int, y: Int): String = {
    grid.toArray(x)(y)._2.toString.toLowerCase
  }

  // Event buffer accessors (delegates to Observable's buffer)
  def peekBufferedEvents(): List[util.GameEvent] = peekEvents()

  def drainBufferedEvents(): List[util.GameEvent] = drainEvents()

  // SESSION MANAGEMENT METHODS ____________________________________________
  
  def createGameSession(): String = {
    val session = sessionManager.createSession()
    currentSessionId = Some(session.sessionId)
    println(s"[GameController] Created session: ${session.sessionId}")
    session.sessionId
  }
  
  def joinGameSession(sessionId: String, playerName: String, playerId: String): Option[Int] = {
    val playerNumber = sessionManager.joinSession(sessionId, playerName, playerId)
    playerNumber.foreach { pNum =>
      println(s"[GameController] Player $playerName (ID: $playerId) joined session $sessionId as Player $pNum")
      
      // Set the current session if not already set
      if (currentSessionId.isEmpty) {
        currentSessionId = Some(sessionId)
      }
      
      // Assign to player1 or player2
      sessionManager.getSession(sessionId).foreach { session =>
        session.player1.foreach { p => player1 = p }
        session.player2.foreach { p => player2 = p }
        
        // Start game if both players joined
        if (session.isReady && !session.isStarted) {
          startGameSession(sessionId)
        }
      }
    }
    playerNumber
  }
  
  def startGameSession(sessionId: String): Boolean = {
    sessionManager.getSession(sessionId) match {
      case Some(session) if session.isReady && !session.isStarted =>
        sessionManager.startGameSession(session)
        currentSessionId = Some(sessionId)
        currentPlayerId = session.currentPlayerId
        
        // Initialize game with NEW instances for THIS session
        val sessionGrid = GridFactory.createGrid(3)
        val sessionDeck = new Deck()
        session.grid = Some(sessionGrid)
        session.deck = Some(sessionDeck)
        session.gameState = Some(new GameState(sessionGrid, List(session.player1.get, session.player2.get), 0, 0))
        session.currentPlayer = session.player1
        
        // Distribute cards using session-specific deck and players
        for (_ <- 1 to 3) {
          session.player1.get.drawCard(sessionDeck)
          session.player2.get.drawCard(sessionDeck)
        }
        
        println(s"[GameController] Started game session $sessionId with isolated state")
        notifyObservers(UpdateGrid(sessionGrid))
        true
        
      case Some(session) if session.isStarted =>
        println(s"[GameController] Session $sessionId already started")
        false
        
      case Some(_) =>
        println(s"[GameController] Session $sessionId not ready (need 2 players)")
        false
        
      case None =>
        println(s"[GameController] Session $sessionId not found")
        false
    }
  }
  
  def isPlayerTurn(sessionId: String, playerId: String): Boolean = {
    sessionManager.getSession(sessionId) match {
      case Some(session) => 
        sessionManager.isPlayerTurn(session, playerId)
      case None => 
        println(s"[GameController] Session $sessionId not found for turn check")
        false
    }
  }
  
  def getSessionPlayer(sessionId: String, playerNumber: Int): Option[Player] = {
    sessionManager.getSession(sessionId).flatMap { session =>
      playerNumber match {
        case 1 => session.player1
        case 2 => session.player2
        case _ => None
      }
    }
  }
  
  def getPlayerNumberForSession(sessionId: String, playerId: String): Option[Int] = {
    sessionManager.getSession(sessionId).flatMap { session =>
      sessionManager.getPlayerNumber(session, playerId)
    }
  }
  
  // Helper methods to access current session info
  def getCurrentSession: Option[GameSession] = {
    currentSessionId.flatMap(sessionManager.getSession)
  }
  
  def getCurrentSessionId: Option[String] = currentSessionId
  
  def getCurrentPlayerId: Option[String] = currentPlayerId
  
  def getPlayerNumberById(playerId: String): Option[Int] = {
    getCurrentSession.flatMap(session => sessionManager.getPlayerNumber(session, playerId))
  }
  
  // Session-aware game state methods
  def isSessionGameOver(session: GameSession): Boolean = {
    session.deck.map(_.size <= 0).getOrElse(false) || session.grid.map(_.isFull).getOrElse(false)
  }
  
  def displaySessionFinalScores(session: GameSession): Unit = {
    (session.player1, session.player2) match {
      case (Some(p1), Some(p2)) =>
        notifyObservers(GameOver(p1.name, p1.points, p2.name, p2.points))
      case _ => // No-op if players not set
    }
  }
  
  def getSession(sessionId: String): Option[GameSession] = {
    sessionManager.getSession(sessionId)
  }
  
  // Set the active session for operations
  def setActiveSession(sessionId: String): Unit = {
    currentSessionId = Some(sessionId)
    println(s"[GameController] Set active session to $sessionId")
  }
  
  // Clear active session
  def clearActiveSession(): Unit = {
    currentSessionId = None
    println(s"[GameController] Cleared active session")
  }

}