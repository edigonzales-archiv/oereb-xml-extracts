import groovy.xml.*

def baseUrl = "https://geoview.bl.ch/main/oereb/extract/reduced/"

new File('bl/2765.csv').eachLine { line ->
    println line
    try {
        def startDate = new Date().getTime()

        // get xml and pretty print it
        def xmlRequestUrl = baseUrl + "xml/geometry/" + line.trim() + "?WITHIMAGES"
        println xmlRequestUrl

        def xmlString = xmlRequestUrl.toURL().text
        Node response = new XmlParser().parseText(xmlString)
        def xmlOutput = new StringWriter()
        def xmlFile = new File("../bl/" + line.trim() + ".xml")
        def xmlNodePrinter = new XmlNodePrinter(new PrintWriter(xmlFile))
        xmlNodePrinter.print(response)
        def xmlDate = new Date().getTime()
        println "xml (sec): " + (xmlDate - startDate) / 1000.0


        // get pdf
        def pdfRequestUrl = baseUrl + "pdf/" + line.trim()
        println pdfRequestUrl

        def pdfFile = new File("../bl/" + line.trim() + ".pdf").newOutputStream()
        pdfFile << new URL(pdfRequestUrl).openStream()
        pdfFile.close()
        def pdfDate = new Date().getTime()
        println "pdf (sec): " + (pdfDate - xmlDate) / 1000.0
    } catch (java.io.FileNotFoundException | Exception e) {
        println e.getMessage()
    }
}