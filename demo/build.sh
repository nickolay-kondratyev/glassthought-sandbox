# Not sure of the right build
eai ./gradlew build

# But I think these are requried for JS
eai ./gradlew :composeApp:jsBrowserDevelopmentWebpack
eai ./gradlew :composeApp:copyJsOutput

