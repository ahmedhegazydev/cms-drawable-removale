import java.io.File
import java.io.BufferedReader

fun main(args: Array<String>) {
    val bufferedReader:
            BufferedReader = File("example.txt")
        .bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
    println(inputString)


}