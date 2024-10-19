package model

case class Grid() {
    val eol: String = sys.props("line.separator") // End of line character

    private def bar(cellWidth: Int = 15, cellNum: Int = 3): String =
        ("+" + "-" * cellWidth) * cellNum + "+" + eol

    private def cells(cellWidth: Int = 15, cellNum: Int = 3): String =
        (("|" + " " * cellWidth) * cellNum + "|" + eol) * 4

    def mesh(cellWidth: Int = 15, cellNum: Int = 3): String =
        (bar(cellWidth, cellNum) + cells(cellWidth, cellNum)) * cellNum + bar(cellWidth, cellNum)
}
