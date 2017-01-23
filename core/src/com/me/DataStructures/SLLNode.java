package com.me.DataStructures;
public class SLLNode<T> {
	public SLLNode<T> next;
	public T data;

	public SLLNode(T data, SLLNode<T> next) {
		this.data = data;
		this.next = next;
	}

	public SLLNode(T data) {
		this.data = data;
		this.next = null;
	}

	public String toString() {
		return data + "\n";

	}

}