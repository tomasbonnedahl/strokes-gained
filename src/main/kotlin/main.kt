fun main(args: Array<String>) {
    val entries = Entries(listOf(
        Entry("410TP"),
        Entry("200F"),
        Entry("39R")
    ))

    entries.entries.forEach { println("it = ${it}") }

    val converter = Converter.of(CommonGroundMapper.create())
    val strokes = entries.entries.map { entry ->
        converter.fromEntryToStroke(entry)
    }

    strokes.forEach { println("it = ${it}") }
}