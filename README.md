# Конвертация данных Google Play (CSV -> JSON)

## Исходные данные
* `src/main/resources/googleplaystore.csv` — исходный файл.

## Исходный код (Kotlin)
* `src/main/kotlin/Main.kt` — точка входа (запуск программы).
* `src/main/kotlin/CsvReader.kt` — чтение и парсинг CSV.
* `src/main/kotlin/Transformer.kt` — преобразование полей и группировка.
* `src/main/kotlin/JsonWriter.kt` — запись итогового JSON.
* `src/main/kotlin/Translations.kt` — словарь переводов категорий.

## Результат
* `googleplaystore_grouped.json` — итоговый обработанный файл (лежит в корне проекта).
