package com.wuxianhui.tools;

import java.util.ArrayList;
import java.util.List;

public class GoodsInfo {
	List<String> goodsTypes = new ArrayList<String>();
	List<GoodsClass> goodsClasses= new ArrayList<GoodsClass>();
	public List<String> getGoodsTypes() {
		return goodsTypes;
	}
	public void setGoodsTypes(List<String> goodsTypes) {
		this.goodsTypes = goodsTypes;
	}
	public List<GoodsClass> getGoodsClasses() {
		return goodsClasses;
	}
	public void setGoodsClasses(List<GoodsClass> goodsClasses) {
		this.goodsClasses = goodsClasses;
	}
	
}
