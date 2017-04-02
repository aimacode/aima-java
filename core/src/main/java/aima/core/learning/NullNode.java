package aima.core.learning;

import aima.core.learning.api.TreeNode;
import java.util.List;

/**
 * @author shantanusinghal
 */
public class NullNode implements TreeNode {

  @Override
  public String process(Example example) {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public Attribute getAttribute() {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public List<TreeNode> getChildren() {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public void addChild(String value, TreeNode child) {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public String toString() {
    return "NullNode{}";
  }

}
