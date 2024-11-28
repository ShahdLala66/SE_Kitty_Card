// src/test/scala/model/patterns/PreSeenDeckStrategySpec.scala
package model.patterns

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import model.Game

class PreSeenDeckStrategySpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A PreSeenDeckStrategy" should {
        "execute playTurn correctly" in {
            val mockGame = mock[Game]
            val strategy = new PreSeenDeckStrategy

            strategy.playTurn(mockGame)

            verify(mockGame).handlePlayerTurn()
            verify(mockGame).switchTurns()
        }
    }
}