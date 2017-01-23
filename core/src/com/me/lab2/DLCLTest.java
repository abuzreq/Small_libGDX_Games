package com.me.lab2;

public class DLCLTest {

	public static void main(String[] args) throws Exception {
		DLCL<Integer> list = new DLCL<Integer>();				
		list.add2Tail(5);
		list.add2Tail(5);
		list.add2Tail(8);
		list.add2Tail(8);
		list.add2Tail(3);
		list.add2Tail(5);
		list.add2Tail(5);
		list.add2Tail(5);
		
		System.out.println(list);

		/*
		list.add2Tail(4);
		System.out.println(list);
		list.add2Tail(3);
		System.out.println(list);
		*/
		
	
		list.mergeSuccessiveDuplicates();
		System.out.println(list);
		
	
		//list.delete(3);
	//	list.deleteN(list.head,4);

	
		
		
	//	list.deleteN(list.head,7);
	}

}
