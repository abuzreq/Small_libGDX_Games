package com.me.tree;



/*
 * IntBST.java ************************** binary search tree of integers
 */
public class BST<T extends Comparable<T>> {
	public BSTNode<T> root;
	protected int count = 0;


	protected void visit(BSTNode<T> p) {
		System.out.print(p.key + " ");
	}

	protected void add2Count() {
		count++;
	}

	protected int count2() {
		if (isEmpty())
			count = 0;
		else
			updateCount(root);
		return count;

	}
	
	public int count(BSTNode<T> node)
	{
		if(node == null)
			return 0 ;
		return 1 + count(node.left) + count(node.right);
	}
	public int count()
	{		
		return count(root);
	}
	protected void updateCount(BSTNode<T> p) {
		if (p != null) {
			
			updateCount(p.left);
			updateCount(p.right);
			add2Count();
		}
	}
	
	
	public int countLeaves()
	{
		return countLeaves(root);
	}


	private int countLeaves(BSTNode<T> node) {
		if(node == null)
			return 0 ;
		if(node.isLeaf()){
			
			return 1 + countLeaves(node.left) + countLeaves(node.right);}
		else 
			return countLeaves(node.left) + countLeaves(node.right);
	}
	public int height()
	{
		if(isEmpty())return 0;
		else if (root.isLeaf()) return 1 ;
		else {
		
		int left = (count(root.left) - countLeaves(root.left)) +1 ;
		int right = (count(root.right) - countLeaves(root.right)) +1 ;
		return Math.max(left, right)+1;
		
		}
	}
	/*
	public void breadthFirst()
	{
		Queue<IntBSTNode> queue = new Queue<IntBSTNode>();
		queue.enqueue(root);
		while(!queue.isEmpty())
		{				
			queue.enqueue(queue.getFirst().left);
			queue.enqueue(queue.getFirst().right);
			System.out.print(" " + queue.dequeue());
		}
	}*/
	
	

	public BST() {
		root = null;
	}

	public void insert(T el) {
		if (isEmpty())
			root = new BSTNode<T>(el);
		else
			insert(el, root);
	}

	private void insert(T el, BSTNode<T> node) {
		/*
		if(node == null)
		{
			node = new IntBSTNode(el);
		}*/
		BSTNode<T> tmp = new BSTNode<T>(el);
		if (node.isLeaf()) 
		{
			
			if (el.compareTo(node.key)< 0)
				node.left = tmp;
			else if (el.compareTo(node.key) > 0)
				node.right = tmp;
		}
		else if (!node.isLeaf())
		{
			if (el.compareTo(node.key)< 0){
				if(node.left == null)
					node.left = tmp;
				else insert(el, node.left);
				}
			else if (el.compareTo(node.key) > 0){
				if(node.right == null)
					node.right = tmp;
				else insert(el, node.right);
			}
		}

	}

	public void clear() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public boolean isInTree(T el) {
		return search(root, el) != null;
	}

	public BSTNode<T> search(T el) {
		return search(root, el);
	}

	protected BSTNode<T> search(BSTNode<T> p, T el) {
		while (p != null)
			if (el == p.key)
				return p;
			else if (el.compareTo(p.key)< 0)
				p = p.left;
			else
				p = p.right;
		return null;
	}

	

	public void preorder() {
		preorder(root);
	}

	protected void preorder(BSTNode<T> p) {
		if (p != null) {
			visit(p);
			preorder(p.left);
			preorder(p.right);
		}
	}

	public void inorder() {
		inorder(root);
	}

	protected void inorder(BSTNode<T> p) {
		if (p != null) {
			inorder(p.left);
			visit(p);
			inorder(p.right);
		}
	}

	public void postorder() {
		postorder(root);
	}

	protected void postorder(BSTNode<T> p) {
		if (p != null) {
			postorder(p.left);
			postorder(p.right);
			visit(p);
		}
	}

	public void delete(T el) { // delete by Copying
		BSTNode<T> node, p = root, prev = null;
		while (p != null && p.key.compareTo( el)!= 0 ){ // find the node p
			prev = p; // with element el;
			if (p.key.compareTo(el)< 0)
				p = p.right;
			else
				p = p.left;
		}
		node = p;
		if (p != null && p.key.compareTo(el)==0) {
			if (node.right == null) // node has no right child;
				node = node.left;
			else if (node.left == null) // no left child for node;
				node = node.right;
			else {
				BSTNode<T> tmp = node.left; // node has both children;
				BSTNode<T> previous = node; // 1.
				while (tmp.right != null) { // 2. find the rightmost
					previous = tmp; // position in the
					tmp = tmp.right; // left subtree of node;
				}
				node.key = tmp.key; // 3. overwrite the reference
				if (previous == node) // of the key being deleted;
					previous.left = tmp.left; // 4.
				else
					previous.right = tmp.left; // 5.
			}
			if (p == root)
				root = node;
			else if (prev.left == p)
				prev.left = node;
			else
				prev.right = node;
		} else if (root != null)
			System.out.println("key " + el + " is not in the tree");
		else
			System.out.println("the tree is empty");
	}

	public void deleteByMerging(T el) {
		BSTNode<T> tmp, node, p = root, prev = null;
		while (p != null && p.key != el) { // find the node p
			prev = p; // with element el;
			if (p.key.compareTo(el)< 0)
				p = p.right;
			else
				p = p.left;
		}
		node = p;
		if (p != null && p.key.compareTo(el)==0) {
			if (node.right == null) // node has no right child: its left
				node = node.left; // child (if any) is attached to its parent;
			else if (node.left == null) // node has no left child: its right
				node = node.right; // child is attached to its parent;
			else { // be ready for merging subtrees;
				tmp = node.left; // 1. move left
				while (tmp.right != null)
					// 2. and then right as far as
					tmp = tmp.right; // possible;
				tmp.right = // 3. establish the link between the
				node.right; // the rightmost node of the left
							// subtree and the right subtree;
				node = node.left; // 4.
			}
			if (p == root)
				root = node;
			else if (prev.left == p)
				prev.left = node;
			else
				prev.right = node;
		} else if (root != null)
			System.out.println("key " + el + " is not in the tree");
		else
			System.out.println("the tree is empty");
	}
	

	
	public MyList<T> putInArray()
	{
		 MyList<T> list = new MyList<T>();
		 postorderFill(list);
		return list;
		
	}
	public void postorderFill(MyList<T>  list) {
		postorderFill(root,list);
	}

	protected void postorderFill(BSTNode<T> p,MyList<T> list) {
		if (p != null) {
			list.add2Tail(p.key);
			postorderFill(p.left,list);
			postorderFill(p.right,list);
			
			
		}
	}	

	
/*
	public void balance(int data[], int first, int last) {
		if (first <= last) {
			int middle = (first + last) / 2;
			System.out.print(data[middle] + " ");
			insert(data[middle]);
			balance(data, first, middle - 1);
			balance(data, middle + 1, last);
		}
	}*/

}
