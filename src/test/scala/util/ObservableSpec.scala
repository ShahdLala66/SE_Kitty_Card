package util

import model.Player
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ObservableSpec extends AnyWordSpec with Matchers {

    "An Observable" should {
        "allow observers to be added and notified" in {
            val observable = new Observable {}
            var updated = false

            val observer = new Observer {
                override def update(event: GameEvent): Unit = {
                    updated = true
                }
            }
            val currentPlayer: Player = Player("Player")
            observable.add(observer)
            observable.notifyObservers(PlayerTurn(currentPlayer.name))
            updated should be(true)
        }

        "allow observers to be removed" in {
            val observable = new Observable {}
            var updated = false

            val observer = new Observer {
                override def update(event: GameEvent): Unit = {
                    updated = true
                }
            }

            val currentPlayer: Player = Player("Player")
            observable.add(observer)
            observable.remove(observer)
            observable.notifyObservers(PlayerTurn(currentPlayer.name))
            updated should be(false)
        }
    }
}