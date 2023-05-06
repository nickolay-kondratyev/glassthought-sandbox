# 'gradle clean' is added since I have ran into cases where the
# coverage report is not re-generated even though the source code
# has changed. (Instead it states that "Task :koverHtmlReport UP-TO-DATE")
gradle clean && gradle koverHtmlReport
