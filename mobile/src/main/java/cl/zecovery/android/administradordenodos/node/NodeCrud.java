package cl.zecovery.android.administradordenodos.node;

import java.util.List;

/**
 * Created by fran on 18-09-15.
 */
public interface NodeCrud {

    int getNodeCount();

    void addNode(Node node);

    Node getNode(int nodeId);

    List<Node> getAllNodes();

    int updateNode(Node node);

    void deleteNode(Node node);

    void deleteAllNode();

    boolean findNode(int nodeId);
}
