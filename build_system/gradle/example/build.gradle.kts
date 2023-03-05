task<Exec>("only-single-command-line-will-run"){
    // These two commandLine are meaningless since they will be overwritten, within
    // doFirst, by the commandLine in doFirst.
    //
    // The Exec task holds a single command line to run. commandLine does not
    // run the command, it sets the command line variable on the Exec instance.
    commandLine("echo", "configuration set echo-1")
    commandLine("echo", "configuration set echo-2")

    println("configuration-printl1")
    println("congiguration-printl2")

    doFirst {
        println("doFirst-printl")

        // This is the only commandLine that matters.
        commandLine("echo", "doFirst commandLine echo-1")
    }

    doLast {
        // doLast is executed after the task action has executed, so setting the
        // command line in here won't do anything useful.
        commandLine("echo", "doLast commandLine echo-1")

        println("doLast printl-1")
        println("doLast printl-2")
    }
}
