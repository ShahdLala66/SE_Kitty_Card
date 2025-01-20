package util

import model.gameModelComp.baseImp.Player
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

        "pass the correct event to the observer" in {
            val observable = new Observable {}
            var receivedEvent: Option[GameEvent] = None

            val observer = new Observer {
                override def update(event: GameEvent): Unit = {
                    receivedEvent = Some(event)
                }
            }

            val currentPlayer: Player = Player("Player")
            val event = PlayerTurn(currentPlayer.name)
            observable.add(observer)
            observable.notifyObservers(event)

            receivedEvent should be(Some(event))
        }

        "notify all added observers" in {
            val observable = new Observable {}
            var observer1Notified = false
            var observer2Notified = false

            val observer1 = new Observer {
                override def update(event: GameEvent): Unit = {
                    observer1Notified = true
                }
            }

            val observer2 = new Observer {
                override def update(event: GameEvent): Unit = {
                    observer2Notified = true
                }
            }

            val currentPlayer: Player = Player("Player")
            val event = PlayerTurn(currentPlayer.name)
            observable.add(observer1)
            observable.add(observer2)
            observable.notifyObservers(event)

            observer2Notified should be(true)
        }

        "do nothing when notifying with no observers" in {
            val observable = new Observable {}
            noException should be thrownBy observable.notifyObservers(PlayerTurn("Player"))
        }
    }
}