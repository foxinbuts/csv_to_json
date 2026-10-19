fun main() {
    // Путь к файлу внутри ресурсов
    val csvResourceName = "googleplaystore.csv"
    val outputJsonPath = "googleplaystore_grouped.json"

    println("Начинаем обработку данных...")

    // 1. Чтение
    // Получаем полный путь к файлу из ресурсов
    val csvFilePath = Thread.currentThread().contextClassLoader.getResource(csvResourceName)?.path
    if (csvFilePath == null) {
        println("Ошибка: Файл $csvResourceName не найден в src/main/resources")
        return
    }

    val reader = CsvReader(csvFilePath)
    val rawData = reader.read()
    println("Прочитано строк: ${rawData.size}")

    // 2. Трансформация
    val transformer = Transformer()
    val transformedData = transformer.transform(rawData)
    println("Данные сгруппированы по категориям. Всего категорий: ${transformedData.size}")

    // 3. Запись
    val writer = JsonWriter(outputJsonPath)
    writer.write(transformedData)
}