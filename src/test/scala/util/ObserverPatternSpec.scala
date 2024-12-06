// src/test/scala/util/ObserverPatternSpec.scala
package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import util.Observer.{ConcreteObservable, ObserverPattern, PlayerTurn, TestObject}

class ObserverPatternSpec extends AnyWordSpec with Matchers {

    "ObserverPattern" should {

        "notify observers correctly when added and removed" in {
            val observable = new ConcreteObservable
            val observer1 = new TestObject
            val observer2 = new TestObject

            observable.add(observer1)
            observable.add(observer2)
            observable.notifyObservers(PlayerTurn("Player1"))
            observable.remove(observer1)
            observable.notifyObservers(PlayerTurn("Player2"))
            observable.remove(observer2)
            observable.notifyObservers(PlayerTurn("Player3"))
        }

        "execute the ObserverPattern object code" in {
            ObserverPattern
            // No assertions needed, just ensuring the code runs without exceptions
        }
    }
}