package com.wuxianhui.tools;

import java.util.ArrayList;
import java.util.List;

public class GoodsClass{
	private String goodsType;
	private List<String> imageUrls= new ArrayList<String>();
	private List<Double> prices=new ArrayList<Double>();
	private List<String> dishNames=new ArrayList<String>();
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public List<String> getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}
	public List<Double> getPrices() {
		return prices;
	}
	public void setPrices(List<Double> prices) {
		this.prices = prices;
	}
	public List<String> getDishNames() {
		return dishNames;
	}
	public void setDishNames(List<String> dishNames) {
		this.dishNames = dishNames;
	}
	
}
