package com.me.DataStructures;

public class DLN<T> {
	protected DLN<T> next ;
	protected DLN<T> previous ;
	protected T data ;
	
	public DLN()
	{
		this.next = null ;
		this.previous = null ;
		this.data =null ;
	}
	public DLN(T data)
	{
		this.next = null ;
		this.previous = null ;
		this.data = data ;
	}
	public DLN(T data , DLN<T> next)
	{
		this.next = next ;
		this.previous = null ;
		this.data = data ;
	}
	public DLN(T data , DLN<T> next , DLN<T> previous)
	{
		this.next = next ;
		this.previous = previous ;
		this.data = data ;
	}
	
	
	public DLN<T> getNext() {
		return next;
	}
	public void setNext(DLN<T> next) {
		this.next = next;
	}
	public DLN<T> getPrevious() {
		return previous;
	}
	public void setPrevious(DLN<T> previous) {
		this.previous = previous;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String toString()
	{
		
		return "Node : " + data ;
	}
	
}
