package toddog

import kotlinx.cinterop.*
import platform.posix.*

// function to fire a message into a tcp/udp socket on localhost
fun fire(port: Int, msg: String, proto: String = "tcp"){

    val hole = port.toShort()
    val payload = msg.toUtf8()

    init_sockets()

    memScoped {
        val serverAddr = alloc<sockaddr_in>()
        var sock = SOCK_STREAM

        if (proto != "tcp")
            sock = SOCK_DGRAM

        val holeFd = socket(AF_INET, sock, 0)
                .ensureUnixCallResult("socket") { it >= 0 }

        with(serverAddr) {
            memset(this.ptr, 0, sockaddr_in.size)
            sin_family = AF_INET.narrow()
            sin_port = posix_htons(hole)
        }

        connect(holeFd, serverAddr.ptr.reinterpret(), sockaddr_in.size.toInt())
                .ensureUnixCallResult("connect") { it == 0 }

        write(holeFd, payload.refTo(0), payload.size.signExtend())
                .ensureUnixCallResult("write") { it >= 0 }

    }

}

inline fun Int.ensureUnixCallResult(op: String, predicate: (Int) -> Boolean): Int {
    if (!predicate(this))
        throw Error("$op: ${strerror(posix_errno())!!.toKString()}")
    return this
}

inline fun Long.ensureUnixCallResult(op: String, predicate: (Long) -> Boolean): Long {
    if (!predicate(this))
        throw Error("$op: ${strerror(posix_errno())!!.toKString()}")
    return this
}