import java.io.File

class CsvReader(private val filePath: String) {

    fun read(): List<Map<String, String>> {
        val file = File(filePath)
        if (!file.exists()) {
            println("Файл не найден: $filePath")
            return emptyList()
        }

        val result = mutableListOf<Map<String, String>>()
        var headers: List<String> = emptyList()

        file.bufferedReader().use { reader ->
            // Явно указываем тип String?
            var currentLine: String? = reader.readLine()
            if (currentLine == null) return emptyList()

            headers = parseLine(currentLine)

            // Читаем остальные строки
            currentLine = reader.readLine()
            while (currentLine != null) {
                if (currentLine.isNotBlank()) {
                    val values = parseLine(currentLine)
                    val row = mutableMapOf<String, String>()

                    for (i in headers.indices) {
                        row[headers[i]] = values.getOrElse(i) { "" }
                    }
                    result.add(row)
                }
                currentLine = reader.readLine()
            }
        }
        return result
    }

    private fun parseLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0

        while (i < line.length) {
            val char = line[i]
            if (char == '"') {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                    current.append('"')
                    i++
                } else {
                    inQuotes = !inQuotes
                }
            } else if (char == ',' && !inQuotes) {
                result.add(current.toString())
                current.clear()
            } else {
                current.append(char)
            }
            i++
        }
        result.add(current.toString())
        return result
    }
}