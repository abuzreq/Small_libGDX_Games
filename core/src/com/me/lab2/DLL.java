package com.me.lab2;

//Ahmed Abuzuraiq 201263900
public class DLL<T> {
	protected DLN<T> head;
	protected DLN<T> tail;

	public DLL() {
		head = null;
		tail = null;
	}

	public DLL(DLN<T> node) {
		head = node;
		tail = node;
		tail.next = null;
		head.previous = null;
	}
	
	//extractList need taking care of if the list extracted have in it the head or tail
	public DLL<T> extractList(int n , int m) throws Exception
	{
		if(isEmpty()) throw new Exception("Empty");
		if(n <0 || m <0 )throw new Exception("Negative indices");
		if(m >=this.getLength())new Exception("m >= length  ");
		if(n>=m)return null;
		int subLength = m- n ;
		DLN<T> tmp = head ;
		int i = 0; 
		while(i!= n){
			tmp =tmp.next ;
			i++;
		
		}
		int j= 0 ; DLN<T> tmp2 = tmp ;
		while(j < subLength){
			j++;
			tmp2 = tmp2.next ;
			}
		DLL<T>  subList = new DLL<T>();
		 DLN<T> pre = tmp.previous  ;
		 while(tmp != tmp2)
		 {
			 subList.add2Tail(tmp.data);
			 tmp = tmp.next ;
		 }
		 pre.next = tmp2 ;
		 if(tmp2 != null) tmp.previous = pre ;
		 return subList;
		

	}
	
	
	
	private int getLength() {
		DLN<T>tmp = head ;
		int i = 0 ;
		while(tmp != null)
		{
			i++;
			tmp =tmp.next ;
		}
		return i;
	}

	// Question2 
	public void mergeSuccessiveDuplicates()
	{
		if(head == tail)
			return;
		DLN<T> tmp = head ;
		while(tmp.next != null)
		{
			while(tmp.data.equals(tmp.next.data))
			{
				tmp = tmp.next;
				delete(tmp.previous);
				if(head == tail)
					break;
				if(tmp != head)
					tmp = tmp.previous;
			}
			if(head == tail)
				break;
			tmp = tmp.next;
		}
	}

	public void clear() {
		head = tail = null;
	}

	public boolean isEmpty() {
		return head == null;
	}

	public void add2Head(T data) {
		DLN<T> node = new DLN<T>(data);
		if (isEmpty()) {
			head = node;
			tail = node;

		} else {
			node.next = head;
			head.previous = node;
			head = node;

		}

	}

	public void add2Tail(T data) {
		DLN<T> node = new DLN<T>(data);
		if (isEmpty()) {
			head = node;
			tail = node;
		} else {
			tail.next = node;
			node.previous = tail;
			tail = node;

		}

	}

	public void addAfter(T data, T after) {
		if (isEmpty() || after == null)
			return;

		else if (after == tail.data || (head == tail && head.data == after))
			add2Tail(data);
		else if (head == tail && head.data != after)
			return;
		else {
			DLN<T> node = new DLN<T>(data);
			DLN<T> tmp = head;

			while (tmp.next != null) {
				if (tmp.data.equals(after)) {
					node.next = tmp.next;
					tmp.next.previous = node;
					node.previous = tmp;
					tmp.next = node;
					break;
				} else
					tmp = tmp.next;

			}
		}

	}

	public void addBefore(T data, T before)  {
		if (isEmpty() || before == null)
			return;
		else if (before == head.data || (head == tail && head.data == before))
			add2Head(data);
		else if (head == tail && head.data != before)
			return;
		else {
			DLN<T> node = new DLN<T>(data);
			DLN<T> tmp = head;
			while (tmp.next != null) {
				if (tmp.next.data.equals(before)) {
					node.next = tmp.next;
					tmp.next.previous = node;
					node.previous = tmp;
					tmp.next = node;
					break;
				} else
					tmp = tmp.next;
			}

		}

	}

	public T deleteHead() {
		if (isEmpty() || tail == head) {
			clear();
			return null;
		} else {
			T data = head.data;
			head.next.previous = null;
			head = head.next;
			return data;
		}

	}

	public T deleteTail() {
		if (isEmpty() || tail == head) {
			clear();
			return null;
		} else {
			T data = tail.data;
			tail.previous.next = null;
			tail = tail.previous;
			return data;
		}

	}

	public void delete(T data) {
		if (isEmpty() || (tail == head && head.data == data)) {
			clear();
		} else if (head.data == data)
			deleteHead();
		else if (!isEmpty()) {

			DLN<T> tmp = head;
			do {
				if (tmp.data.equals(data)) {
					if(tmp != head)
					tmp.previous.next = tmp.next;
					if(tmp != tail)
					tmp.next.previous = tmp.previous;
					break;
				} else
					tmp = tmp.next;

			} while (tmp != null);

		} else if (tail.data == data)
			deleteTail();

	}
	
	
	
	// for local use only
	private void delete(DLN<T> node) {
		if (isEmpty())
			return;
		else if (node == head)
			deleteHead();
		else if (node == tail)
			deleteTail();

		else {

			DLN<T> tmp = head;

			while (tmp != node) {
				tmp = tmp.next;
				if (tmp == node) {
				
					tmp.previous.next = tmp.next;
					tmp.next.previous = tmp.previous;
					break;
				}
			}

		}

	}



	public String toString() {
		if (isEmpty())
			return "[]";
		String str = "[ ";
		DLN<T> node = head;
		while (node != null) {
			str += node.data + " ";
			node = node.next;

		}

		//str += tail.data + " ";
		return str + " ]";
	}

}
