package util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ObservableSpec extends AnyWordSpec with Matchers {

    "An Observable" should {

        "allow an Observer to be added" in {
            val observable = new Observable
            val observer = new Observer {
                def update: Unit = {}
            }
            observable.add(observer)
            observable.subscribers should contain(observer)
        }

        "allow an Observer to be removed" in {
            val observable = new Observable
            val observer = new Observer {
                def update: Unit = {}
            }
            observable.add(observer)
            observable.remove(observer)
            observable.subscribers should not contain observer
        }

        "notify all Observers" in {
            var updated = false
            val observable = new Observable
            val observer = new Observer {
                def update: Unit = { updated = true }
            }
            observable.add(observer)
            observable.notifyObservers
            updated should be(true)
        }
    }
}
