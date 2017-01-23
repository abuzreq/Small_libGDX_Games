package com.me.tree;


public class BSTNode<T extends Comparable<T>> implements Comparable<T>
{
    public T key;
    public BSTNode<T> left;
	public BSTNode<T> right;
    public BSTNode() {
        left = right = null;
    }
    public BSTNode(T el) {
        this(el,null,null);
    }
    public BSTNode(T el, BSTNode<T> lt, BSTNode<T> rt) {
        key = el; left = lt; right = rt;
    }
    public void visit() {
        System.out.print(key + " ");
    }
    public String toString() {
        return "" + key;
    }
	public boolean hasLeft()
	{
		return left != null ;
	}
	public boolean hasRight()
	{
		return right != null ;
	}
	
    protected boolean isLeaf()
    {
    	return left==null && right == null ;
    }
	@Override
	public int compareTo(T o) {
		// TODO Auto-generated method stub
		return 0;
	}
}

