package com.me.lab2;

public class Test {

	public static void main(String[] args) {
		MyList<Integer> list = new MyList<Integer>() ;
		MyList<Integer> list2 = new MyList<Integer>() ;
		list.add2Head(2);
		list.add2Head(34);
		list.add2Head(6);
		list.add2Head(-1);
		list.add2Head(14);
		list.add2Head(7);
		list.add2Head(25);
		System.out.println(list);
		
		list.kSmallest(3);
		System.out.println(list);
		
		
		
		
		Stack<Integer> stack = new Stack<Integer>();

		
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		/*
		System.out.println(stack);
		stack.reverseOrder();
		System.out.println(stack);
		
		*/
		
		Queue<Integer> queue = new Queue<Integer>();
		queue.enqueue(2);
		queue.enqueue(5);
		queue.enqueue(4);
		queue.enqueue(5);
		queue.enqueue(6);
		
		System.out.println(queue);
		queue.removeFromQueue(5);
		System.out.println(queue);
		

	}

}
