rootProject.name = "gradle-playground"
include("moduleOne")
include("module1")


buildCache {
    local {
        isEnabled = true
        directory =  File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}