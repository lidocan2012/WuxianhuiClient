package com.wuxianhui.tools;

import java.util.ArrayList;


public class OrderInformation {
	private int willCommitNum=0;
	private int commitedNum=0;
	private ArrayList<OrderMap> willCommitOrders = new ArrayList<OrderMap>();
	private ArrayList<OrderMap> commitedOrders = new ArrayList<OrderMap>();
	public int getWillCommitNum() {
		return willCommitNum;
	}
	public void setWillCommitNum(int willCommitNum) {
		this.willCommitNum = willCommitNum;
	}
	public int getCommitedNum() {
		return commitedNum;
	}
	public void setCommitedNum(int commitedNum) {
		this.commitedNum = commitedNum;
	}
	public ArrayList<OrderMap> getWillCommitOrders() {
		return willCommitOrders;
	}
	public void setWillCommitOrders(ArrayList<OrderMap> willCommitOrders) {
		this.willCommitOrders = willCommitOrders;
	}
	public ArrayList<OrderMap> getCommitedOrders() {
		return commitedOrders;
	}
	public void setCommitedOrders(ArrayList<OrderMap> commitedOrders) {
		this.commitedOrders = commitedOrders;
	}
	public int willCommitContains(int position){
		for(int i=0;i<willCommitOrders.size();i++){
			if(position==willCommitOrders.get(i).position)
				return i;
		}
		return -1;
	}
	public int commitedContains(int position){
		for(int i=0;i<commitedOrders.size();i++){
			if(position==commitedOrders.get(i).position)
				return i;
		}
		return -1;
	}
	public void setWillCommit(int position ,int number){
		int index = willCommitContains(position);
		int olderNum = willCommitOrders.get(index).number;
		willCommitOrders.get(index).setNumber(number);
		willCommitNum+=(number-olderNum);
	}
	public void addWillCommit(int position) {
		int index = willCommitContains(position);
		if(index>=0){
			int olderNum = willCommitOrders.get(index).number;
			willCommitOrders.get(index).setNumber(olderNum+1);
		}else{
			willCommitOrders.add(new OrderMap(position,1));
		}
		willCommitNum++;
	}
	public void commit(){
		for(int i=0;i<willCommitOrders.size();i++){
			int index = commitedContains(willCommitOrders.get(i).position);
			int number = willCommitOrders.get(i).number;
			if(index>=0){
				int olderNum = commitedOrders.get(index).getNumber();
				commitedOrders.get(index).setNumber(number+olderNum);
			}else{
				commitedOrders.add(willCommitOrders.get(i));
			}
			willCommitOrders.remove(i);
			willCommitNum-=number;
			commitedNum+=number;
		}
	}
	public class OrderMap{
		int position;
		int number;
		public OrderMap(int position, int number) {
			super();
			this.position = position;
			this.number = number;
		}
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
		}
	}
}
