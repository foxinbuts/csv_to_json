import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.JsonArray
import java.io.File

class JsonWriter(private val filePath: String) {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    fun write(data: Map<String, List<Map<String, Any?>>>) {
        val rootObject = buildJsonObject {
            data.forEach { (category, apps) ->
                putJsonArray(category) {
                    apps.forEach { app ->
                        add(buildJsonObject {
                            app.forEach { (key, value) ->
                                putJsonElement(key, value)
                            }
                        })
                    }
                }
            }
        }

        File(filePath).writeText(json.encodeToString(JsonObject.serializer(), rootObject))
        println("JSON успешно сохранен в $filePath")
    }

    // Вспомогательная функция для конвертации стандартных типов Kotlin в JSON
    private fun kotlinx.serialization.json.JsonObjectBuilder.putJsonElement(key: String, value: Any?) {
        when (value) {
            null -> put(key, kotlinx.serialization.json.JsonNull)
            is String -> put(key, value)
            is Int -> put(key, value)
            is Double -> put(key, value)
            is Boolean -> put(key, value)
            is List<*> -> {
                val jsonArray = JsonArray(value.map {
                    if (it is String) kotlinx.serialization.json.JsonPrimitive(it)
                    else kotlinx.serialization.json.JsonNull
                })
                put(key, jsonArray)
            }
            else -> put(key, value.toString())
        }
    }
}