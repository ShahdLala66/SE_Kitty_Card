package util.grid

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GridFactorySpec extends AnyWordSpec with Matchers {
    "GridFactory" should {
        "create a grid with the specified size" in {
            val size = 5
            val grid = GridFactory.createGrid(size)

            grid.rectangleColors.length shouldBe size
        }
    }
}