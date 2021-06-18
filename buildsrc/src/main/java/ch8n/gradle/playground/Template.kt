
import javafx.beans.property.MapProperty


abstract class ProcessTemplates : DefaultTask() {
    @get:Input
    abstract val templateEngine: Property<TemplateEngineType>


    // collection of input files and so we use the @InputFiles annotation
    @get:InputFiles
    abstract val sourceFiles: ConfigurableFileCollection

    /***
     * it does not implement Serializable, so we can’t use the @Input annotation
     * We use @Nested on templateData to let Gradle know that this is a value with nested input properties.
     */
    @get:Nested
    abstract val templateData: TemplateData


    /**
     * The directory where the generated files go. There are several annotations for output files and directories.
     * single directory requires @OutputDirectory
     */
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun processTemplates() {
        // ...
    }
}

abstract class TemplateData {

    //String are serializable and can be annotated with @Input
    @get:Input
    abstract val name: Property<String>

    //HashMap are serializable and can be annotated with @Input
    @get:Input
    abstract val variables: MapProperty<String, String>
}