package com.me.tree;

import java.util.Random;


public class BSTTest {

	public static void main(String[] args) {
	BST<Integer> tree = new BST<Integer>();
	Random random = new Random();
	for(int i = 0 ; i < 10 ; i++){
	tree.insert(5);	
	tree.insert(3);	
	tree.insert(8);	
	tree.insert(4);	
	tree.insert(2);	
	tree.insert(9);	
	
	}
	//System.out.println("BreadthFirst : ");
	//tree.breadthFirst();
	System.out.println("\nPre-order Depth First Traversal:");
	tree.preorder();
	System.out.println("\nIn-order Depth First Traversal:");
	tree.inorder();
	System.out.println("\nPost-order Depth First Traversal:");
	tree.postorder();
	System.out.println("\nNumber of nodes in the tree:");
	System.out.println(tree.count());
	System.out.println("Number of leaves in the tree:");
	System.out.println(tree.countLeaves());
	System.out.println("Height of the tree:");
	System.out.println(tree.height());
	}

}

	
   	
	
	

