package toddog

import socket.fire

class ddog() {

    val port = 8125     // ddog agent port on localhost

    // https://help.datadoghq.com/hc/en-us/articles/208398693--dog-statsd-sample-rate-parameter-explained
    var sample_rate: Double = 0.0

    private val proto = "udp"
    private var taglist = ""

    fun tag(tag1: String, tag2: String = ""){
        taglist += tag1
        if ( tag2.isNotBlank() )
            taglist += ":${tag2},"   // comma terminated
    }

    fun event(title: String, text: String) {
        send(title, text)
    }

    fun counter(name: String, value: Int) {
        send(name, value, "c")
    }

    fun gauge(name: String, value: Int) {
        send(name, value, "g")
    }

    // gauge and counter
    private fun send(key: String, value: Int, metricType: String){
        var payload = "$key:$value|$metricType"
        if (sample_rate in 0.0..1.0) // range is 0% to 100%
            payload += "|@$sample_rate"
        if ( taglist.isNotBlank() )
            payload += "|#$taglist"
        fire(port,payload,proto)
    }

    // event
    private fun send(title: String, text: String){
        var payload = "_e{${title.length},${text.length}}:$title:$text"
        if ( taglist.isNotBlank() )
            payload += "|#$taglist"
        fire(port,payload,proto)
    }

}
