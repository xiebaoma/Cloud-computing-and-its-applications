import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.*;

public class KnowledgeGraph {
    public static void main(String[] args) {
        File f = new File("D:/Neo4jDB");
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db= dbFactory.newEmbeddedDatabase(f);

    }
}
