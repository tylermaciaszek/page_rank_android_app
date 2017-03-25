package com.example.tyler.webgraph;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class WebGraph {
	private static WebGraph graph = new WebGraph();
	public static final int MAX_PAGES = 40;
	private static ArrayList<WebPage> pages;
	private static int[][] edges;
	private static int destInt;
	private static int sourceInt;
	private static int indexOfPage;
	private static int rank = 0;
	private static boolean exists = false;
	int newArray[][] = new int[MAX_PAGES][MAX_PAGES];


	public WebGraph() {
		pages = new ArrayList<WebPage>();
		edges = new int[MAX_PAGES][MAX_PAGES];
	}

	public int getIndexOfPage() {
		return indexOfPage;
	}

	public static int getIndex(String s) {
		for (int index = 0; index < pages.size(); index++) {
			if (pages.get(index).getUrl().equals(s))
				return index;
		}
		return -1;
	}

	public static void addPage(WebPage page){
		pages.add(page);
	}

	public void addRow(WebPage newPage){
		TableRow row = new TableRow(context);
		TextView index = new TextView(context);
		index.setText(String.valueOf(newPage.getIndex()));
		index.setTextSize(30);
		index.setPadding(0, 0, 0, 10);
		TextView url = new TextView(context);
		url.setText(newPage.getUrl());
		url.setTextSize(30);
		url.setPadding(0, 0, 0, 10);
		TextView rank = new TextView(context);
		rank.setText(String.valueOf(newPage.getRank()));
		rank.setTextSize(30);
		rank.setPadding(0, 0, 0, 10);
		TextView links = new TextView(context);
		links.setText(newPage.getLinks());
		links.setTextSize(30);
		links.setPadding(0, 0, 0, 10);
		TextView keywords = new TextView(context);
		keywords.setText(newPage.getKeywords().toString());
		keywords.setTextSize(30);
		keywords.setPadding(0, 0, 0, 10);
		MainActivity.getTable().addView(row);
		row.addView(index);
		row.addView(url);
		row.addView(rank);
		row.addView(links);
		row.addView(keywords);
	}

    static ArrayList<WebPage> getPages(){
        return pages;
    }
	private static Context context;

	public WebGraph(Context context) {
		this.context = context;
	}


	public static WebGraph buildFromFiles(AssetManager am, String pagesFile, String linksFile) {


		try {
			InputStream is = am.open("pages.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;

			InputStream linksIn = am.open("links.txt");
			BufferedReader linkReader = new BufferedReader(new InputStreamReader(linksIn));
			String linksString;

			while ((line = reader.readLine()) != null) {
				String[] pageSplit = line.split(" ");
				String urlParam = pageSplit[4];
				ArrayList<String> keywordsParam = new ArrayList<>();
				for (int index = 5; index < pageSplit.length; index++)
					keywordsParam.add(pageSplit[index]);
				WebPage newPage = new WebPage(indexOfPage, urlParam, keywordsParam);
				pages.add(newPage);
                indexOfPage++;
			}
			while ((linksString = linkReader.readLine()) != null) {
				String[] linkSplit = linksString.split(" ");
				String source = linkSplit[4];
				String dest = linkSplit[5];
				sourceInt = getIndex(source);
				destInt = getIndex(dest);
				edges[sourceInt][destInt] = 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			return graph;
		}

	public void addPage(String url, ArrayList<String> keywords) {
		WebPage newPage = new WebPage(indexOfPage, url, keywords);
		indexOfPage++;
		pages.add(newPage);
	}

	public void addLink(String source, String destination) {
		sourceInt = getIndex(source);
		destInt = getIndex(destination);
		edges[sourceInt][destInt] = 1;
		System.out.println("link added between" + source + " and " + destination);
	}

	public void removePage(String url) {
		indexOfPage--;
		int remove = getIndex(url);
		for (int index = getIndex(url); index < pages.size(); index++) {
			pages.get(index).setIndex((pages.get(index).getIndex() - 1));
		}
		pages.remove(remove);
		int p = 0;
		for (int i = 0; i < edges.length; ++i) {
			if (i == remove)
				continue;
			int q = 0;
			for (int j = 0; j < edges[0].length; ++j) {
				if (j == remove)
					continue;
				newArray[p][q] = edges[i][j];
				++q;
			}
			++p;
		}
		edges = newArray;
	}

	public void removeLink(String source, String destination) {
		sourceInt = getIndex(source);
		destInt = getIndex(destination);
		edges[sourceInt][destInt] = 0;
	}

	public int getCount() {
		int count = 0;
		for (int index = 0; index < pages.size(); index++) {
			if (!pages.get(index).equals(null)) {
				count++;
			}
		}
		return count;
	}

	public void updatePageRanks() {
		this.setLinks();
		int column = 0;
		while (column < this.getCount()) {
			rank = 0;
			for (int j = 0; j < edges[0].length; ++j) {
				if (edges[j][column] == 1) {
					rank++;
				} else {

				}
			}
			pages.get(column).setRank(rank);
			++column;
		}
	}

	public ArrayList<WebPage> search(String s) {
		ArrayList<WebPage> found = new ArrayList<>();
		for (int indexSearch = 0; indexSearch < pages.size(); indexSearch++) {
			if (pages.get(indexSearch).getKeywords().contains(s)) {
				found.add(pages.get(indexSearch));
			}
		}
		return found;
	}

	public boolean alreadyExists(String s) {
		exists = false;
		for (int i = 0; i < pages.size(); i++) {
			if (pages.get(i).getUrl().equalsIgnoreCase(s)) {
				exists = true;
			}
		}
		return exists;
	}

	public class NameComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			WebPage e1 = (WebPage) o1;
			WebPage e2 = (WebPage) o2;
			return (e1.getUrl().compareTo(e2.getUrl()));
		}
	}

	public class IdComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			WebPage e1 = (WebPage) o1;
			WebPage e2 = (WebPage) o2;
			if (e1.getIndex() == e2.getIndex())
				return 0;
			else if (e1.getIndex() > e2.getIndex())
				return 1;
			else
				return -1;
		}
	}

	public class RankComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			WebPage e1 = (WebPage) o1;
			WebPage e2 = (WebPage) o2;
			if (e1.getRank() == e2.getRank())
				return 0;
			else if (e1.getRank() > e2.getRank())
				return -1;
			else
				return 1;
		}
	}

	public void sortByRank() {
		Collections.sort(pages, new RankComparator());
	}

	public void sortByIndex() {
		Collections.sort(pages, new IdComparator());
	}

	public void sortByURL() {
		Collections.sort(pages, new NameComparator());
	}

	public void setLinks() {
		int column = 0;
		String s = "";
		for (int index = 0; index < pages.size(); index++) {
			WebPage t = pages.get(index);
			while (column < this.getCount()) {
				s = "";
				for (int j = 0; j < edges[0].length; j++) {
					if (edges[column][j] == 1) {
						s += (j + " ");
					}
					pages.get(column).setLink(s);
				}
				column++;
			}
			if (t.getLinks() == null)
				t.setLink(" ");
		}
	}

	public void printTable() {
		System.out.println(" ");
		System.out.printf("%5s %5s %26s %5s %s %s", "Index", "URL", "PageRank", "Links", "       ", "Keywords");
		System.out.println("\n-------------------------------------------------------------------------------------------");
		int column = 0;
		String s = "";
		for (int index = 0; index < pages.size(); index++) {
			WebPage t = pages.get(index);
			while (column < this.getCount()) {
				s = "";
				for (int j = 0; j < edges[0].length; j++) {
					if (edges[column][j] == 1) {
						s += (j + " ");
						pages.get(column).setLink(s);
					}
				}
				column++;
			}
			if (t.getLinks() == null)
				t.setLink(" ");
			System.out.printf("%-4s %-2s %-20s %-2s %2s %4s %-12s %s %s", t.getIndex(), "|", t.getUrl(), "|", t.getRank(), "|", t.getLinks(), "|", t.getKeywords());
			System.out.println();
		}
	}
}
