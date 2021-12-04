import java.io.BufferedReader
import java.io.File

fun main(args: Array<String>) {
    val bufferedReader:
            BufferedReader = File("./src/main/resources/android_home_json.json")
        .bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
//    println(inputString)

    val strs = inputString.split("\"icon\":").toTypedArray()
//    println(strs.size)
//    strs.forEach {
//        println(it)
//        println("====================")
//    }

    val values = mutableListOf<String>()
    strs.forEach {
        var res = ""
        var counter = 0
        run loop@{
            it.forEachIndexed { index, c ->
                if (c == '"' && counter == 0) {
                    res += it[index - 1].toString()
                    counter++
                } else {
                    if (c == '"' && counter != 0) {
                        return@loop
                    } else
                        res += it[index].toString()
                }
            }

        }
        values.add("ic_${res.trim()}")
    }

    values.removeFirst()

//    println(values)
//    values.forEach {
//        println(it)
//        println("====================")
//    }

    val start = "val listUsedCmsDrawables = mutableListOf<Int>(\n"
    var body = ""
    values.forEach {
        body += "R.drawable.$it, \n"
    }
    val end = ")"
    val finalRes = start.plus(body).plus(end)
    println(finalRes)


}

