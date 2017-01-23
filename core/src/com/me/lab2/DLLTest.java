package com.me.lab2;

public class DLLTest {

	
	public static void main(String[] args)
	{
		DLL<Integer> list = new DLL<Integer>();
		list.add2Tail(0);
		list.add2Tail(1);
		list.add2Tail(2);
		list.add2Tail(3);
		list.add2Tail(4);
		list.add2Tail(5);
		list.add2Tail(6);
		list.add2Tail(7);
		list.add2Tail(8);
		list.add2Tail(9);

		
		System.out.println(list);
		DLL<Integer> sub = new DLL<Integer>();
		try {
			 sub = 	list.extractList(4, 11);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list);
		System.out.println("Sub : "+ sub);
		
	}
}
