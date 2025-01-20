package util.grid

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import model.gameModelComp.baseImp.{Grid, Suit}

class GridUtilsSpec extends AnyWordSpec with Matchers {
    "GridUtils" should {
        "generate a grid with the specified size" in {
            val size = 5
            val grid = GridUtils.generateRandomColors(size)

            grid.foreach(row => row.length shouldBe size)
        }

        "generate a grid with exactly 4 non-white colors" in {
            val size = 5
            val grid = GridUtils.generateRandomColors(size)

            val nonWhiteColors = grid.flatten.count(_ != Suit.White)
            nonWhiteColors shouldBe 4
        }

        "create an empty grid with the specified size" in {
            val grid: Grid = GridUtils.createEmptyGrid(3)

            for {
                x <- 0 until 3
                y <- 0 until 3
            } yield {
                grid.getColor(x, y) shouldBe Suit.White
            }
        }
    }
}