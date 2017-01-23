package com.me.lab2;

public class Stack<T extends Comparable<T>> {
	MyList<T> list = new MyList<T>();
	
	public Stack()
	{
		
	}

	public void push(T el)
	{
		list.add2Head(el);
	}
	public T pop()
	{
		return list.deleteFromHead();
	}	
	
	public T peek()
	{
		return list.head.data ;
	}
	
	public String toString()
	{
		return list.toString();
		
	}
	public boolean isEmpty()
	{
		return list.isEmpty();
	}
	
	public void reverseOrder()
	{
		Queue<T> queue = new Queue<T>();
		while(!this.isEmpty())
		{
			queue.enqueue(this.pop());
		}
		while(!queue.isEmpty())
		{
			this.push(queue.dequeue());
		}
	}
	
	

}
