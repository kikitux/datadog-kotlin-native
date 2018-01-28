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

    val myapp1 = ddog()
    myapp1.tag("nonprod")
    myapp1.tag("myapp", "myapp1")
    myapp1.gauge("web.unique", myRandom.nextInt(100) )
    myapp1.gauge("web.hits", myRandom.nextInt(200) )

    // simulate a high hit event, when over 190 of 200
    if ( myRandom.nextInt(210) > 190 )
        myapp1.event("web.backend", "high hits on server1")

    val myapp2 = ddog()
    myapp2.sample_rate = 1.0    // sample_rate is how much we want to ddog agent push 0% to 100%
    myapp2.tag("nonprod")
    myapp2.tag("myapp", "myapp2")
    myapp2.gauge("web.unique", myRandom.nextInt(100) )
    myapp2.gauge("web.hits", myRandom.nextInt(200) )

    // simulate a high hit event, when over 190 of 200
    if ( myRandom.nextInt(210) > 190 )
        myapp2.event("web.backend", "high hits on server2")

}
