package com.me.DataStructures;




public class SLL<T> {
	public SLLNode<T> head;
	SLLNode<T> tail;

	public SLL(SLLNode<T> head) {

	}

	public SLL( ) {

	}
	
	public void addAtIndex(T data , int i)
	{
	
		if(i==0)
			addAtHead(data);
		else{
			SLLNode<T> newNode  = new SLLNode<T>(data , get(i+1));
			get(i).next = newNode ;
		}
		
	}
	
	

	
	private void addAtTail(SLLNode<T> node) {
		System.out.println(node.data);
		if(isEmpty())addAtHead(node);
		else{
		
		tail.next = node ;
		tail = node ;}
		
	}

	private void addAtHead(SLLNode<T> node) {
		node.next = head ;
		head = node ;
		
	}

	
	
	public SLLNode<T> findBefore(T el)
    {
           
		SLLNode<T> tmp = head;
            if(head == tail)
            {
                    return null;
            }
            else
            {
                    if(tmp.data.equals(el))
                            return null;
                    else
                    {
                            do{
                                    if(tmp.next.data.equals(el))
                                            return tmp;
                                    else
                                            tmp = tmp.next;
                            }while(tmp!=tail);
                            return null;
                    }
            }
           
    }
	
	
	 public boolean isEmpty()
     {
             return head == null;
     }
    
     public void clear()
     {
             head = tail = null;
     }
     
     public void addToHead(T el)
     {
             SLLNode<T> n = new SLLNode<T>(el);
             if(isEmpty()){
                     head = n;
                     tail = n;
             }
             else
             {
                     n.next = head;
                     head = n;
             }
     }
	public T delete(T el){
        
        SLLNode<T> n = findBefore(el);
        SLLNode<T> aN = find(el);
        if(aN != null)
        {
                T info = aN.data;
                if(head == tail)
                {
                        clear();
                        return info;
                }
                else if(aN == head)
                {
                        deleteHead();
                        return info;
                }
                else if(aN == tail)
                {
                        deleteTail();
                        return info;
                }
                else
                {
                        n.next = n.next.next;
                        return info;
                }
               
        }
        return null;
         
}
	
	
	
	private SLLNode<T> delete(SLLNode<T> tbd)
	{
		SLLNode<T> node = head ;
		while(node !=  tbd )
		{
			node = node.next ;
		}
			findBefore(tbd.data).next = tbd.next;
			tbd.next = null ;
		return tbd ;
	}
	
	public T deleteHead()
    {
           
            if(isEmpty())
                    return null;
            else if(head == tail)
            {
                    T el = head.data;
                    clear();
                    return el;
            }
            else
            {
                    T el = head.data;
                    head = head.next;
                    return el;
            }
           
    }
   
    public T deleteTail()
    {
           
            if(isEmpty())
                    return null;
            else if(head == tail)
            {
                    T el = head.data;
                    clear();
                    return el;
            }
            else
            {
                    T el = tail.data;
                    SLLNode<T> tmp = head;
                    while(tmp.next != tail)
                    {
                            tmp = tmp.next;
                    }
                    tail = tmp;
                    tmp.next = null;
                    return el;
            }
           
    }
	
	private void addAtHead(T data) {
		
		SLLNode<T> newNode = new SLLNode<T>(data,head);
		head = newNode ;
		
	}


   
    public void addToTail(T el)    
    {
           
            SLLNode<T> n = new SLLNode<T>(el);
            if(isEmpty())
            {
                    addToHead(el);
            }
            else
            {
                    tail.next = n;
                    tail = n;
            }
           
    }
	public SLLNode<T> find(T el)
    {
           
		SLLNode<T> tmp = head;
            if(head == tail)
            {
                    if(tmp.data == el){
                            return tmp;
                    }
                    else
                    {
                            return null;
                    }
            }
            else
            {
                    do{
                            if(tmp.data.equals(el))
                                    return tmp;
                            else
                                    tmp = tmp.next;
                    }while(tmp!=null);
                    return null;
            }
           
    }
	
	public SLLNode<T> get(int i) {
		if (head == null)
			return null;
		else {

			if (i == 0)
				return head;
			else {
				SLLNode<T> node = head ;
				for(int j = 1 ; j <=i ; j++)
				{
					node = node.next ;
				}
				return node ;
				
			}

		}

	}

	
	// for testing 
	/*
	public static void main(String[] args) {
		 SLL<Integer>  sll   = new SLL<Integer>();
		
		 sll.addAtHead(2);
		 sll.addAtHead(34);
		 sll.addAtHead(6);
		 sll.addAtHead(1);
		 sll.addAtHead(14);
		 sll.addAtHead(7);
		 sll.addAtHead(25);	

		 System.out.println(sll.toString());
	}
	
	*/
	
	
	public String toString()
    {
            if(isEmpty())
                    return "[]";
            String s = "[";
            SLLNode<T> tmp = head;
            while(tmp.next != null){
                    s = s + tmp.data + " ";
                    tmp = tmp.next;
            }
            s = s + tmp.data + " ";
            return s + "]";
    }
}


