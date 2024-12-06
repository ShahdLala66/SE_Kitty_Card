// src/test/scala/model/patterns/RandomStrategySpec.scala
package model.patterns

import model.Logik.Game
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._

class RandomStrategySpec extends AnyWordSpec with Matchers with MockitoSugar {

    "A RandomStrategy" should {
        "execute playTurn correctly" in {
            val mockGame = mock[Game]
            val strategy = new RandomStrategy

            strategy.playTurn(mockGame)

            verify(mockGame).handlePlayerTurn()
            verify(mockGame).switchTurns()
        }
    }
}