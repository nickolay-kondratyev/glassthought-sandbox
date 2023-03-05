task<Exec>("only-single-command-line-will-run"){
    commandLine("echo", "outside commandLine echo-1")
    commandLine("echo", "outside commandLine echo-2")

    println("outside-printl1")
    println("outside-printl2")

    doLast {
        commandLine("echo", "doLast commandLine echo-1")
        commandLine("echo", "doLast commandLine echo-2")

        println("doLast printl-1")
        println("doLast printl-2")
    }
}
