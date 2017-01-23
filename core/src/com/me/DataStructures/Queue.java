package com.me.DataStructures;

public class Queue<T extends Comparable<T>> {
	MyList<T> list = new MyList<T>();

	public void enqueue(T el)
	{
		list.add2Tail(el);
	}
	public T dequeue()
	{
		return  list.deleteFromHead();
	}
	public T getFirst()
	{
		return  list.head.data;
	}
	public boolean isEmpty()
	{
		return list.isEmpty();
	}
	public int length()
	{
		Queue<T> tmp = new Queue<T>();
		int i = 0 ;
		while(!isEmpty()){
		tmp.enqueue(this.dequeue());
		i++ ;
		}
		while(!tmp.isEmpty())
		this.enqueue(tmp.dequeue());
		return i ;
		
		
	}

	
	public String toString()
	{
		return list.toString();
	}
}

