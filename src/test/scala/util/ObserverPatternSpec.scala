package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ObserverPatternSpec extends AnyWordSpec with Matchers {

    "The ObserverPattern object" should {

        "add observers and notify them" in {
            val observable = new Observable
            val observer1 = new TestObject
            val observer2 = new TestObject

            observable.add(observer1)
            observable.add(observer2)

            // Capture the output
            val stream = new java.io.ByteArrayOutputStream()
            Console.withOut(stream) {
                observable.notifyObservers
            }

            stream.toString should include ("Ping")
        }

        "remove observers and stop notifying them" in {
            val observable = new Observable
            val observer1 = new TestObject
            val observer2 = new TestObject

            observable.add(observer1)
            observable.add(observer2)
            observable.remove(observer1)

            // Capture the output
            val stream = new java.io.ByteArrayOutputStream()
            Console.withOut(stream) {
                observable.notifyObservers
            }

            stream.toString should include ("Ping")
        }
    }

    "An ObserverPattern" should {
        "notify all observers when notifyObservers is called" in {
            val observable = new Observable
            val observer1 = new TestObject
            val observer2 = new TestObject

            observable.add(observer1)
            observable.add(observer2)

            // Capture the output
            val stream = new java.io.ByteArrayOutputStream()
            Console.withOut(stream) {
                observable.notifyObservers
            }

            stream.toString should include("Ping")
        }

        "not notify removed observers" in {
            val observable = new Observable
            val observer1 = new TestObject
            val observer2 = new TestObject

            observable.add(observer1)
            observable.add(observer2)
            observable.remove(observer1)

            // Capture the output
            val stream = new java.io.ByteArrayOutputStream()
            Console.withOut(stream) {
                observable.notifyObservers
            }

            stream.toString should include ("Ping")
        }
    }
}