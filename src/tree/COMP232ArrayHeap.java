package tree;

import structures.generic.MyArrayList;

/**
 * Implementation of the CS232PriorityQueue interface that uses a binary heap
 * with an array backing store.
 * 
 * @author William Goble
 * @author Dickinson College
 * @version March 8, 2016
 */
public class COMP232ArrayHeap<K extends Comparable<K>, V> implements COMP232PriorityQueue<K, V> {

	/*
	 * NOTE: We could implement this directly on top of an array. However that
	 * would just mean reimplementing much of the work already done in
	 * ArrayList. In particular, by using the CS232ArrayList class we do not
	 * have to reimplement the array resizing operations.
	 */
	private MyArrayList<HeapNode<K, V>> tree;

	/**
	 * Construct a new empty CS232ArrayHeap.
	 */
	public COMP232ArrayHeap() {
		tree = new MyArrayList<HeapNode<K, V>>();
	}

	/**
	 * Construct a new CS232ArrayHeap using the specified keys and values. The
	 * keys and values are added into the heap in level order. If the keys do
	 * not appear in an order that represents a valid heap (as determined by
	 * their compareTo method), an IllegalArgumentException is thrown.
	 * 
	 * @param keys
	 *            the keys
	 * @param values
	 *            the values
	 * @throws IllegalArgumentException
	 *             if the keys and values do not have the same length, the keys
	 *             or the values are empty, or the keys are not specified in an
	 *             order that results in a valid heap.
	 */
	public COMP232ArrayHeap(K[] keys, V[] values) {
		if (keys.length != values.length) {
			throw new IllegalArgumentException(
					"Length of keys and values must be the same.");
		}
		if (keys.length == 0) {
			throw new IllegalArgumentException(
					"keys and values must not be empty.");
		}

		// Add the key,value pairs to the tree.
		tree = new MyArrayList<HeapNode<K, V>>();
		for (int i = 0; i < keys.length; i++) {
			// add the node to the heap.
			tree.add(new HeapNode<K, V>(keys[i], values[i]));
		}
		
		// verify that we have a valid heap!
		if (!checkHeapProperty()) {
			throw new IllegalArgumentException("Heap is not valid.");
		}
	}

	/*
	 * Get the index where the left child of the node at index i is stored.
	 */
	private int getLeftChildIndex(int i) {
		return (2 * i) + 1;
	}

	/*
	 * Get the index where the right child of the node at index i is stored.
	 */
	private int getRightChildIndex(int i) {
		return (2 * i) + 2;
	}

	/*
	 * Get the index where the parent of the node at index i is stored.
	 */
	private int getParentIndex(int i) {
		return (i - 1) / 2;
	}

	/*
	 * Check if the node at index is a leaf node. A node is a leaf if both of
	 * its children are not in the tree (i.e. have an index that is >= than the
	 * number of node in the tree.
	 */
	private boolean isLeaf(int i) {
		int leftChild = getLeftChildIndex(i);
		int rightChild = getRightChildIndex(i);
		return leftChild >= tree.size() && rightChild >= tree.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public void add(K key, V value) {
		/*
		 * Place the node at the end of the heap (i.e. in the first empty spot
		 * in level order). Because the heap is always a complete tree, and we
		 * are using the array based binary tree representation this is just the
		 * first empty element in the array.
		 * 
		 * This new node may not be in a proper location (i.e. it will be larger
		 * than its parent.) To fix this we "percolate" the newly added node up
		 * the tree by swapping it with its parent node until its key is smaller
		 * than its parents key. NOTE: "smaller" is determined by using he
		 * compareTo method in the key (which we know it has because the K type
		 * parameter is bounded to be a subtype of Comparable).
		 */

		// Intentionally not implemented - see homework assignment.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	public V remove() {
		// cases to consider:
		// 1. if the root is the only node
		if (tree.size() == 1) {
			V rootValue = tree.get(0).value;
			tree.remove(0);
			return rootValue;
		// 2. If there are more than one nodes
		} else if (tree.size() > 1){
			// how to remove
			// swap the right most child with the root
			swap(0, tree.size() - 1);
			// remove the right most child
			V maxValue = tree.remove(tree.size() - 1).value;
			// trickle down the new root into it's proper place
			trickleDown(0);

			return maxValue;
		// 3. When the heap is empty
		} else {
			return null;
		}
	}

	/*
	 * Helper method that swaps two elements of the tree.
	 */
	private void swap(int indexA, int indexB) {
		// how to swap
		HeapNode<K, V> nodeA = tree.get(indexA);
		HeapNode<K, V> nodeB = tree.get(indexB);
		tree.set(indexA, nodeB);
		tree.set(indexB, nodeA);
	}

	/*
	 * Trickle the node at tricklingNodeIndex down the tree. The node trickles
	 * down if it's key is smaller than the key of either child. To trickle down
	 * a node switches places with its larger child.
	 */
	private void trickleDown(int tricklingNodeIndex) {
		if(!isLeaf(tricklingNodeIndex)) {
			int largerChildIndex = getLargerChildIndex(tricklingNodeIndex);
			HeapNode<K, V> trickleNode = tree.get(tricklingNodeIndex);
			HeapNode<K, V> childNode = tree.get(largerChildIndex);

			if(trickleNode.key.compareTo(childNode.key) < 0) {
				swap(tricklingNodeIndex, largerChildIndex);
				trickleDown(largerChildIndex);
			}
		}
	}

	/*
	 * Get the index of the child with the larger key value. We can
	 * assume that there is at least one child.
	 */
	private int getLargerChildIndex(int parentIndex) {
		int leftChildIndex = getLeftChildIndex(parentIndex);
		int rightChildIndex = getRightChildIndex(parentIndex);

		if (leftChildIndex >= tree.size()) {
			return rightChildIndex; // no left child.
		} else if (rightChildIndex >= tree.size()) {
			return leftChildIndex; // no right child
		} else {
			// two children.
			HeapNode<K, V> leftChild = tree.get(leftChildIndex);
			HeapNode<K, V> rightChild = tree.get(rightChildIndex);
			if (leftChild.key.compareTo(rightChild.key) > 0) {
				// left child has larger key.
				return leftChildIndex;
			} else {
				// right child has larger key.
				return rightChildIndex;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public V peek() {
		if (tree.size() == 0) {
			return null;
		} else {
			return tree.get(0).value;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return tree.size();
	}

	/**
	 * Helper method that checks that the heap property is preserved. That is
	 * that every parent's key is larger than its children's keys, as defined by
	 * the compareTo method. This is used by the tests to check the internal
	 * structure of the heap.
	 */
	boolean checkHeapProperty() {
		return checkHeapPropertyHelper(0);
	}

	private boolean checkHeapPropertyHelper(int nodeIndex) {
		/*
		 * Traverse the heap, checking the heap property at each node.
		 */
		if (nodeIndex >= tree.size()) {
			return true; // off tree.
		} else {
			/*
			 * Note: Works on root because (0-1)/2 = 0 so, root is compared to
			 * itself.
			 */
			HeapNode<K, V> node = tree.get(nodeIndex);
			int parentIndex = getParentIndex(nodeIndex);
			HeapNode<K, V> parentNode = tree.get(parentIndex);

			if (node.key.compareTo(parentNode.key) > 0) {
				return false; // child larger than parent
			} else {
				int lci = getLeftChildIndex(nodeIndex);
				int rci = getRightChildIndex(nodeIndex);

				return checkHeapPropertyHelper(lci)
						&& checkHeapPropertyHelper(rci);
			}
		}
	}

	/*
	 * Node used to hold the key,value pair at each location in the heap.
	 */
	private static class HeapNode<K, V> {
		public K key;
		public V value;

		public HeapNode(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}