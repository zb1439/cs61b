/**
 * Simple Red-Black tree implementation, where the keys are of type T.
 @ author
 */
public class RedBlackTree<T extends Comparable<T>> {

    /** Root of the tree. */
    private RBTreeNode<T> root;

    /**
     * Empty constructor.
     */
    public RedBlackTree() {
        root = null;
    }

    /**
     * Constructor that builds this from given BTree (2-3-4) tree.
     *
     * @param tree BTree (2-3-4 tree).
     */
    public RedBlackTree(BTree<T> tree) {
        BTree.Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /**
     * Builds a RedBlack tree that has isometry with given 2-3-4 tree rooted at
     * given node r, and returns the root node.
     *
     * @param r root of the 2-3-4 tree.
     * @return root of the Red-Black tree for given 2-3-4 tree.
     */
    RBTreeNode<T> buildRedBlackTree(BTree.Node<T> r) {
        // YOUR CODE HERE
        if (r == null) {
            return null;
        }
        if (r.getItemCount() == 3) {
            RBTreeNode<T> newRoot = new RBTreeNode<>(true, r.getItemAt(1));
            newRoot = insert(newRoot, r.getItemAt(0));
            newRoot = insert(newRoot, r.getItemAt(2));
            newRoot.left.left = buildRedBlackTree(r.getChildAt(0));
            newRoot.left.right = buildRedBlackTree(r.getChildAt(1));
            newRoot.right.left = buildRedBlackTree(r.getChildAt(2));
            newRoot.right.right = buildRedBlackTree(r.getChildAt(3));
            return newRoot;
        } else if (r.getItemCount() == 2) {
            RBTreeNode<T> newRoot = new RBTreeNode<>(true, r.getItemAt(1));
            newRoot = insert(newRoot, r.getItemAt(0));
            newRoot.left.left = buildRedBlackTree(r.getChildAt(0));
            newRoot.left.right = buildRedBlackTree(r.getChildAt(1));
            newRoot.right = buildRedBlackTree(r.getChildAt(2));
            return newRoot;
        } else {
            RBTreeNode<T> newRoot = new RBTreeNode<>(true, r.getItemAt(0));
            newRoot.left = buildRedBlackTree(r.getChildAt(0));
            newRoot.right = buildRedBlackTree(r.getChildAt(1));
            return newRoot;
        }
    }

    /**
     * Rotates the (sub)tree rooted at given node to the right, and returns the
     * new root of the (sub)tree. If rotation is not possible somehow,
     * immediately return the input node.
     *
     * @param node root of the given (sub)tree.
     * @return new root of the (sub)tree.
     */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        // YOUR CODE HERE
        if (node == null || node.left == null) {
            return node;
        }
        RBTreeNode temp = node.left.right;
        node.left.right = node;
        RBTreeNode rtn = node.left;
        node.left = temp;
        return rtn;
    }

    /**
     * Rotates the (sub)tree rooted at given node to the left, and returns the
     * new root of the (sub)tree. If rotation is not possible somehow,
     * immediately return the input node.
     *
     * @param node root of the given (sub)tree.
     * @return new root of the (sub)tree.
     */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        // YOUR CODE HERE
        if (node == null || node.right == null) {
            return node;
        }
        RBTreeNode temp = node.right.left;
        RBTreeNode rtn = node.right;
        rtn.left = node;
        node.right = temp;
        return rtn;
    }

    /**
     * Flips the color of the node and its children. Assume that the node has
     * both left and right children.
     *
     * @param node tree node
     */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /**
     * Returns whether a given node is red. null nodes (children of leaf) are
     * automatically considered black.
     *
     * @param node node
     * @return node is red.
     */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    /**
     * Insert given item into this tree.
     *
     * @param item item
     */
    void insert(T item) {
        root = insert(root, item);
        root.isBlack = true;
    }

    /**
     * Recursivelty insert item into this tree. returns the (new) root of the
     * subtree rooted at given node after insertion. node == null implies that
     * we are inserting a new node at the bottom.
     *
     * @param node node
     * @param item item
     * @return (new) root of the subtree rooted at given node.
     */
    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {

        // Insert (return) new red leaf node.
        if (node == null) {
            return new RBTreeNode<T>(false, item);
        }

        // Handle normal binary search tree insertion.
        int comp = item.compareTo(node.item);
        if (comp == 0) {
            return node; // do nothing.
        } else if (comp < 0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }

        // handle case C and "Right-leaning" situation.
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }

        // handle case B
        if (isRed(node.left) && isRed(node.left.left)) {
            // YOUR CODE HERE
            node = rotateRight(node);
        }

        // handle case A
        if (isRed(node.left) && isRed(node.right)) {
            // YOUR CODE HERE
            flipColors(node);
        }
        return node;
    }


    /**
     * RedBlack tree node.
     *
     * @param <T> type of item.
     */
    static class RBTreeNode<T> {

        /** Item. */
        protected final T item;

        /** True if the node is black. */
        protected boolean isBlack;

        /** Pointer to left child. */
        protected RBTreeNode<T> left;

        /** Pointer to right child. */
        protected RBTreeNode<T> right;

        /**
         * A node that is black iff BLACK, containing ITEM, with empty
         * children.
         */
        RBTreeNode(boolean black, T item) {
            this(black, item, null, null);
        }

        /**
         * Node that is black iff BLACK, contains ITEM, and has children
         * LEFT AND RIGHT.
         */
        RBTreeNode(boolean black, T item, RBTreeNode<T> left,
            RBTreeNode<T> right) {
            this.isBlack = black;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }
}
