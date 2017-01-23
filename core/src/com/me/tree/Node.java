package com.me.tree;

public class Node<T extends Comparable<T>> {
	T data ;
	 public Node<T> next ;
	
	public Node()
	{
		data = null ;
		next = null;
	}
	
	public Node(T data,Node<T> next)
	{
		this.data = data ;
		this.next = next;
	}
	public Node(T data)
	{
		this.data = data ;
		next = null;
	}
	public String toString()
	{
		return ""+data ;
	}
	
}
