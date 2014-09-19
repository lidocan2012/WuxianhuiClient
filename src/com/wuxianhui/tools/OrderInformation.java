package com.wuxianhui.tools;

import java.util.ArrayList;


public class OrderInformation {
	private int willCommitNum=0;
	private double willCommitSum=0.0;
	private int commitedNum=0;
	private double commitedSum=0.0;
	private ArrayList<OrderGoods> willCommitOrders = new ArrayList<OrderGoods>();
	private ArrayList<OrderGoods> commitedOrders = new ArrayList<OrderGoods>();
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
	public ArrayList<OrderGoods> getWillCommitOrders() {
		return willCommitOrders;
	}
	public void setWillCommitOrders(ArrayList<OrderGoods> willCommitOrders) {
		this.willCommitOrders = willCommitOrders;
	}
	public ArrayList<OrderGoods> getCommitedOrders() {
		return commitedOrders;
	}
	public void setCommitedOrders(ArrayList<OrderGoods> commitedOrders) {
		this.commitedOrders = commitedOrders;
	}
	public double getWillCommitSum() {
		return willCommitSum;
	}
	public void setWillCommitSum(double willCommitSum) {
		this.willCommitSum = willCommitSum;
	}
	public double getCommitedSum() {
		return commitedSum;
	}
	public void setCommitedSum(double commitedSum) {
		this.commitedSum = commitedSum;
	}
	public int willCommitContains(Long goodsId){
		for(int i=0;i<willCommitOrders.size();i++){
			if(goodsId==willCommitOrders.get(i).getGoodsId())
				return i;
		}
		return -1;
	}
	public int commitedContains(Long goodsId){
		for(int i=0;i<commitedOrders.size();i++){
			if(goodsId==commitedOrders.get(i).goodsId)
				return i;
		}
		return -1;
	}
	public void setWillCommit(int index ,int num){
		int olderNum = willCommitOrders.get(index).number;
		double oldPrice = willCommitOrders.get(index).price;
		willCommitOrders.get(index).setNumber(num);
		willCommitOrders.get(index).setPrice(num*(oldPrice/olderNum));
		willCommitNum+=(num-olderNum);
		willCommitSum+=(num-olderNum)*(oldPrice/olderNum);
	}
	public void addWillCommit(int indexOfGoodsType,int position) {
		GoodsClass goodsClass = AppController.getInstance().getGoodsInfo().getGoodsClasses().get(indexOfGoodsType);
		Long goodsId = goodsClass.getGoodsIds().get(position);
		double price = goodsClass.getPrices().get(position);
		int index = willCommitContains(goodsId);
		if(index>=0){
			int olderNum = willCommitOrders.get(index).number;
			double olderprice = willCommitOrders.get(index).price;
			willCommitOrders.get(index).setNumber(olderNum+1);
			willCommitOrders.get(index).setPrice(olderprice+price);
		}else{
			OrderGoods orderGoods = new OrderGoods();
			orderGoods.setGoodsId(goodsId);
			orderGoods.setGoodsName(goodsClass.getDishNames().get(position));
			orderGoods.setNumber(1);
			orderGoods.setImageUrl(goodsClass.getImageUrls().get(position));
			orderGoods.setPrice(price);
			willCommitOrders.add(orderGoods);
		}
		willCommitNum++;
		willCommitSum+=price;
	}
	public void commit(){
		for(int i=0;i<willCommitOrders.size();i++){
			int index = commitedContains(willCommitOrders.get(i).goodsId);
			int number = willCommitOrders.get(i).number;
			double price = willCommitOrders.get(i).price;
			if(index>=0){
				int olderNum = commitedOrders.get(index).getNumber();
				commitedOrders.get(index).setNumber(number+olderNum);
				double olderPrice = commitedOrders.get(index).price;
				commitedOrders.get(index).setPrice(price+olderPrice);
			}else{
				commitedOrders.add(willCommitOrders.get(i));
			}
			willCommitNum-=number;
			willCommitSum-=price;
			commitedNum+=number;
			commitedSum+=price;
		}
	}
	public void willCommitDelete(int index) {
		int number = willCommitOrders.get(index).getNumber();
		double price = willCommitOrders.get(index).getPrice();
		willCommitOrders.remove(index);
		willCommitNum -= number;
		willCommitSum -= price;
	}
	public void willCommitClear(){
		willCommitOrders.clear();
		willCommitNum=0;
		willCommitSum=0;
	}
	public class OrderGoods{
		private Long goodsId;
		private int number;
		private double price;
		private String goodsName;
		private String imageUrl;
		public Long getGoodsId() {
			return goodsId;
		}
		public void setGoodsId(Long goodsId) {
			this.goodsId = goodsId;
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public String getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}
	}
}
