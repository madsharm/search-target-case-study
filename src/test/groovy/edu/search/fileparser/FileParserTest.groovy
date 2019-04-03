package edu.search.fileparser

import edu.search.vo.WordInFileCount
import spock.lang.Specification

class FileParserTest extends Specification {

    def "Test Line Parser" () {
        setup:
        def parser = new FileParser()
        def line = "The military history of France encompasses an immense panorama of conflicts and struggles extending for more than 2,000 years across areas including modern France, greater Europe, and French territorial possessions overseas. According to the British historian Niall Ferguson, France has participated in 168 major European wars since 387 BC, out of which they have won 109, drawn 10 and lost 49: this makes France the most successful military power in European history - in terms of number of fought and won."

        when:
        def result = parser.parseLine(line)

        then:

        noExceptionThrown()
        result.size() == 56
        result['france'] == 4
        result['-'] == null
    }

    def "Test Multi Line Parser" () {
        setup:
        def parser = new FileParser()
        def line = "The military history of France encompasses an immense panorama of conflicts and struggles extending for more than 2,000 years across areas including modern France, greater Europe, and French territorial possessions overseas. According to the British historian Niall Ferguson, France has participated in 168 major European wars since 387 BC, out of which they have won 109, drawn 10 and lost 49: this makes France the most successful military power in European history - in terms of number of fought and won.[4]\n" +
                "\n" +
                "The Gallo-Roman conflict predominated from 60 BC to 50 BC, with the Romans emerging victorious in the conquest of Gaul by Julius Caesar. After the decline of the Roman Empire, a Germanic tribe known as the Franks took control of Gaul by defeating competing tribes. The \"land of Francia,\" from which France gets its name, had high points of expansion under kings Clovis I and Charlemagne. In the Middle Ages, rivalries with England and the Holy Roman Empire prompted major conflicts such as the Norman Conquest and the Hundred Years' War. With an increasingly centralized monarchy, the first standing army since Roman times, and the use of artillery, France expelled the English from its territory and came out of the Middle Ages as the most powerful nation in Europe, only to lose that status to Spain following defeat in the Italian Wars. The Wars of Religion crippled France in the late 16th century, but a major victory over Spain in the Thirty Years' War made France the most powerful nation on the continent once more. In parallel, France developed its first colonial empire in Asia, Africa, and in the Americas. Under Louis XIV, France achieved military supremacy over its rivals, but escalating conflicts against increasingly powerful enemy coalitions checked French ambitions and left the kingdom bankrupt at the opening of the 18th century."

        when:
        def result = parser.parseLine(line)

        then:
        print(result)
        noExceptionThrown()
        result.size() == 166
        result['and'] == 11
        result['france'] == 10
    }

    def "Test Parser with Collection of files" () {
        setup:
        def parser = new FileParser()
        def classLoader = FileParser.class.getClassLoader()
        def documentFolder = new File(classLoader.getResource("examples").getFile())

        when:
        def result = parser.parseFiles(documentFolder)

        then:
        print(result)
        noExceptionThrown()
        result.size() == 12

        result["004"] == null
        result["2000"] == null
        result["2,000"] == null

        result["really"].getWordCounts().size() == 4
        result["really"].getWordCounts().contains(new WordInFileCount("example1.txt",2))
        result["really"].getWordCounts().contains(new WordInFileCount("example2.txt",2))
        result["really"].getWordCounts().contains(new WordInFileCount("example3.txt",2))
        result["really"].getWordCounts().contains(new WordInFileCount("example4.txt",2))

        result["awesome"].getWordCounts().size() == 2
        result["awesome"].getWordCounts().contains(new WordInFileCount("example2.txt",1))
        result["awesome"].getWordCounts().contains(new WordInFileCount("example4.txt",2))

    }


}
