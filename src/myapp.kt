import platform.posix.abs
import platform.posix.getpid
import toddog.ddog

class myRandom() {
    // copied from here:
    // https://github.com/JetBrains/kotlin-native/blob/91fc291bfad30d7ad3740612cb55050a3769dab6/performance/src/main/kotlin/org/jetbrains/ring/Utils.kt

    // added getpid() and abs()
    companion object {
        private var seedInt = getpid()
        fun nextInt(boundary: Int = 100): Int {
            seedInt = ( getpid() * 3 * seedInt + 11 ) % boundary
            return abs(seedInt)
        }
    }
}


fun main(args: Array<String>) {

    val ddog = ddog()

    ddog.tag("myapp", "myapp1")
    ddog.tag("nonprod")

    ddog.gauge("web.unique", myRandom.nextInt(100) )
    ddog.gauge("web.hits", myRandom.nextInt(200) )

    val myapp2 = ddog()

    myapp2.sample_rate = 1.0

    myapp2.tag("myapp", "myapp2")
    myapp2.tag("nonprod")

    myapp2.gauge("web.unique", myRandom.nextInt(100) )
    myapp2.gauge("web.hits", myRandom.nextInt(200) )

    // simulate a high cpu event, when over 96 of 100
    if ( myRandom.nextInt(100) > 96 ) {
        val myapp3 = ddog()

        myapp3.tag("myapp", "myapp3")
        myapp3.tag("nonprod")

        myapp3.event("web.backend", "high cpu on server3")
    }

}
