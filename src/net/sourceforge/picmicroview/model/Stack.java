package net.sourceforge.picmicroview.model;

//import java.util.ArrayList;

public class Stack {

	private Pic18F452 pic18;
	private int[] stack;
	private int ptr;
	
	public Stack(Pic18F452 pic18) {
		stack = new int[32];
		this.pic18 = pic18;
		ptr = 0;
		stack[ptr] = 0;//stack starts with index 1
	}

	public void push(int address){
		ptr++;
		//System.out.println("stack ptr in push is " + ptr);
		//System.out.println("address pushed onto stack is " + Integer.toHexString(address));
		if(ptr < stack.length){
			stack[ptr] = address;
		}
		else{ //set stack overflow bit and overwrite top of stack
			pic18.dataMem.stkptr.setBit(1);
			stack[stack.length -1] = address;
		}
	}
	
	public int pop(){
		//System.out.println("stack ptr in pop is " + ptr);
		//System.out.println("address popped from stack is " + Integer.toHexString(stack[ptr]));
		if(ptr == 0){
			return stack[1];
		}
		else{
			return stack[ptr--];	
		}
	}
}
