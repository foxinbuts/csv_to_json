import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class Transformer {

    // Словарь версий Android и API
    private val apiMap = mapOf(
        "1.0" to 1, "1.1" to 2, "1.5" to 3, "1.6" to 4, "2.0" to 5, "2.0.1" to 6, "2.1" to 7, "2.2" to 8,
        "2.3" to 9, "2.3.3" to 10, "3.0" to 11, "3.1" to 12, "3.2" to 13, "4.0" to 14, "4.0.3" to 15,
        "4.1" to 16, "4.2" to 17, "4.3" to 18, "4.4" to 19, "4.4W" to 20, "5.0" to 21, "5.1" to 22,
        "6.0" to 23, "7.0" to 24, "7.1" to 25, "8.0" to 26, "8.1" to 27, "9" to 28, "10" to 29,
        "11" to 30, "12" to 31, "12L" to 32, "13" to 33, "14" to 34, "15" to 35, "16" to 36
    )

    fun transform(rawData: List<Map<String, String>>): Map<String, List<Map<String, Any?>>> {
        // Группируем по переведенной категории
        return rawData.groupBy { row ->
            val rawCat = row["Category"] ?: ""
            Translations.categories[rawCat] ?: rawCat
        }.mapValues { (_, rows) ->
            // Для каждой группы преобразуем каждую строку
            rows.map { row -> transformRow(row) }
        }
    }

    private fun transformRow(row: Map<String, String>): Map<String, Any?> {
        return mapOf(
            "App" to row["App"],
            "Rating" to parseRating(row["Rating"]),
            "Reviews" to parseReviews(row["Reviews"]),
            "Size" to row["Size"],
            "Installs" to parseInstalls(row["Installs"]),
            "Type" to row["Type"],
            "Price" to parsePrice(row["Price"]),
            "Content Rating" to row["Content Rating"],
            "Genres" to parseGenres(row["Genres"]),
            "Last Updated" to parseDate(row["Last Updated"]),
            "Current Ver" to row["Current Ver"],
            "Android Ver" to row["Android Ver"],
            "Min API" to parseApi(row["Android Ver"])
        )
    }

    private fun parseRating(rating: String?): Double? {
        if (rating.isNullOrBlank() || rating == "NaN") return null
        return rating.toDoubleOrNull()
    }

    private fun parseReviews(reviews: String?): Int {
        return reviews?.toIntOrNull() ?: 0
    }

    private fun parseInstalls(installs: String?): Int {
        if (installs.isNullOrBlank()) return 0
        return installs.replace(",", "").replace("+", "").toIntOrNull() ?: 0
    }

    private fun parsePrice(price: String?): Boolean {
        if (price.isNullOrBlank() || price == "0" || price.equals("Free", ignoreCase = true)) return false
        val cleanPrice = price.replace("$", "").trim()
        return cleanPrice.toDoubleOrNull()?.let { it > 0 } ?: false
    }

    private fun parseGenres(genres: String?): List<String> {
        if (genres.isNullOrBlank()) return emptyList()
        return genres.split(";").map { it.trim() }
    }

    private fun parseDate(date: String?): String? {
        if (date.isNullOrBlank()) return null
        return try {
            // Парсим "January 7, 2018"
            val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
            val localDate = LocalDate.parse(date, formatter)
            // Возвращаем в ISO 8601 (2018-01-07T00:00:00Z)
            "${localDate}T00:00:00Z"
        } catch (e: Exception) {
            null
        }
    }

    private fun parseApi(androidVer: String?): Int? {
        if (androidVer.isNullOrBlank() || androidVer == "Varies with device") return null
        // Ищем версию (например, "4.0.3" из "4.0.3 and up")
        val match = Regex("""(\d+(?:\.\d+)*)""").find(androidVer) ?: return null
        val version = match.groupValues[1]

        // Ищем точное совпадение, иначе берем мажорную часть
        return apiMap[version] ?: apiMap[version.split(".")[0]]
    }
}