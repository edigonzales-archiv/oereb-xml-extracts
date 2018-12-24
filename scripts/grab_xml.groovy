import groovy.xml.*

def baseUrl = "https://geoview.bl.ch/main/oereb/extract/reduced/"

new File('bl/2765.csv').eachLine { line ->
    println line
    try {
        // get xml and pretty print it
        def xmlRequestUrl = baseUrl + "xml/geometry/" + line.trim() + "?WITHIMAGES"
        println xmlRequestUrl

        def xmlString = xmlRequestUrl.toURL().text
        Node response = new XmlParser().parseText(xmlString)
        def xmlOutput = new StringWriter()
        def xmlFile = new File("../bl/" + line.trim() + ".xml")
        def xmlNodePrinter = new XmlNodePrinter(new PrintWriter(xmlFile))
        xmlNodePrinter.print(response)

        // get pdf
        def pdfRequestUrl = baseUrl + "pdf/" + line.trim()
        println pdfRequestUrl

        def pdfFile = new File("../bl/" + line.trim() + ".pdf").newOutputStream()
        pdfFile << new URL(pdfRequestUrl).openStream()
        pdfFile.close()
    } catch (java.io.FileNotFoundException e) {
        println e.getMessage()
    }
}