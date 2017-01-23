package com.me.tree;

// 201263900 Ahmed Abuzuraiq hw2 ics202 
//I had wrote this data structure and left the other methods for the sake of testing
public class MyList<T extends Comparable<T>> {
	protected Node<T> head, tail;

	public MyList() {
		head = null;
		tail = null;
	}
	



	//Question 1 -1
	public void appendList(MyList<T> list)
	{
		if(list.isEmpty())return ;
		if(isEmpty())
		{
			head = list.head ;
		}else{
		tail.next = list.head ;
		}
		tail = list.tail ;
	}
	//Question 1 -2 helper method
	public void addListBeforeHead(MyList<T> list)
	{
		if(list.isEmpty())return ;
		if(isEmpty())
		{
			head = list.head ;
			tail = list.tail ;
		}else{
		list.tail.next = head ;
		head = list.head ;
		}
		
	}
	public boolean isEmpty() {
		return head == null;
	}

	void clear() {
		head = null;
		tail = null;
	}

	public void add2Head(T ele) {

		Node<T> n = new Node<T>(ele, head);
		head = n;
		if (tail == null)
			tail = n;
	}

	public T deleteFromHead() {
		if (isEmpty()) {
			System.out.println("Error: Cannot delete from empty List");
			return null;
		}
		Node<T> temp = head;
		if (head == tail) {
			this.clear();
			return temp.data;
		}
		head = head.next;
		return temp.data;

	}
	
	public void add2Tail(T data) {
		if(data == null)return ;
		if (this.isEmpty()) {
			add2Head(data);
		} else {
			Node<T> node = new Node<T>(data, null);
			tail.next = node;
			tail = node;
		}

	}

	T deleteFromTail() {
		if (isEmpty()) {
			System.out.println("Error: Cannot delete from empty List");
			return null;
		} else if (head == tail) {// only one node
			T ele = tail.data;
			this.clear();
			return ele;
		} else {
			Node<T> tmp = head;
			while (tmp.next != tail)
				tmp = tmp.next;
			T ele = tail.data;
			tail = tmp;
			tail.next = null;
			return ele;
		}// end of else
	}// end of method deleteFromTail

	public String toString() {
		if (this.isEmpty())
			return "[]";
		else {
			String str = "[";
			Node<T> node = head;
			while (node != null) {
				str += " " + node.data;
				node = node.next;
			}
			str += " ]";
			return str;

		}

	}

	public void addBefore(T before, T added) {
		if (this.isEmpty())
			add2Head(added);
		else {
			Node<T> node = new Node<T>(added);
			Node<T> tmp = head;
			while (tmp.next.data != before && tmp.next != null) {
				tmp = tmp.next;
			}
			if (tmp.next == null)
				System.out
						.println("the element you wat to add efore is not found");
			node.next = tmp.next;
			tmp.next = node;

		}
	}

	public void addAfter(T after, T added) {
		if (this.isEmpty())
			System.out
			.println("the element you want to add after is not found");
		else {
			Node<T> node = new Node<T>(added);
			Node<T> tmp = head;

			while (tmp.next != null) {
				if (tmp.next.data == after) {
					tmp = tmp.next;
					System.out.println(tmp+"");
					break;
				} else
					tmp = tmp.next;
			}
			node.next = tmp.next ;
			tmp.next = node ;

		}
	}
	
	

	

	public void delete(T data) {
		if (isEmpty() || (tail == head && head.data == data)) {
			clear();
		} else if (head.data == data)
			deleteFromHead();
		else if (!isEmpty()) {

			Node<T> tmp = head;
			do {
				if (tmp.next.data.equals(data)) {
					
					tmp.next = tmp.next.next;
					
					break;
				} else
					tmp = tmp.next;

			} while (tmp.next != null);

		} else if (tail.data == data)
			deleteFromTail();

	}

	private void delete(Node<T> node) {
		if (isEmpty())
			return;
		else if (node == head)
			deleteFromHead();
		else if (node == tail)
			deleteFromTail();
		else {

			Node<T> tmp = head;

			while (tmp.next != node) {
				tmp = tmp.next;
				if (tmp.next == node) {
					tmp.next = tmp.next.next;
					break;
				}
				}
			}

		}

	
	
	
	/*
	public Node<T> delete(T data)
	{
	
	}
*/
}
