package com.example.tyler.webgraph;

import java.util.ArrayList;
import java.util.Collection;

public class WebPage {
	private int index;
	private int rank;
	private String url;
	private ArrayList<String> keywords;
	private String links;
	
	public WebPage(int index, String url, ArrayList<String> keywords) {
		super();
		this.index = index;
		this.url = url;
		this.keywords = keywords;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WebPage [index=" + index + ", rank=" + rank + ", url=" + url
				+ ", keywords=" + keywords + "]";
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the keywords
	 */
	public ArrayList<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
	
	public void setLink(String links){
		this.links = links;
	}
	
	public String getLinks(){
		return links;
	}
	
}
