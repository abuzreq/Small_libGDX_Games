package com.me.lab2;

public class DLCL<T> {
	protected DLN<T> head;
	protected DLN<T> tail;

	public DLCL() {
		head = null;
		tail = null;
	}

	public DLCL(DLN<T> node) {
		head = node;
		tail = node;
		tail.next = node;
		head.previous = node;
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
			node.previous = tail;
			tail.next = node;
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
			node.next = head;
			node.previous = tail;
			head.previous = node;
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

			while (tmp.next != head) {
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
			while (tmp.next != head) {
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
			head.next.previous = tail;
			tail.next = head.next;
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
			tail.previous.next = head;
			head.previous = tail.previous;
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
				if (tmp.next.data.equals(data)) {

					tmp.next = tmp.next.next;
					tmp.next.next.previous = tmp;
					break;
				} else
					tmp = tmp.next;

			} while (tmp.next != head.next);

		} else if (tail.data == data)
			deleteTail();

	}
	
	
	//DLL not DLCL , later change to != null
	public void mergeSuccessiveDuplicates()
	{
		DLN<T> tmp = head ;
		while(tmp.next != head)
		{
			while(tmp.data.equals(tmp.next.data)&&tmp.next != head)
				delete(tmp.next);
			tmp = tmp.next;
		}
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

	public void deleteN(DLN<T> node, int n) {

		int i = n;
		if (head == tail) {
			clear();
			System.out.println(this.toString());
		} else {
			i = n;

			while (i != 1) {
				node = node.next;
				i--;

			}
			DLN<T> newNode = node.next;
			delete(node);
			System.out.println(this.toString());
			deleteN(newNode, n);
		}

	}

	public String toString() {
		if (isEmpty())
			return "[]";
		String str = "[ ";
		DLN<T> node = head;
		while (node != tail) {
			str += node.data + " ";
			node = node.next;

		}

		str += tail.data + " ";
		return str + " ]";
	}

}
