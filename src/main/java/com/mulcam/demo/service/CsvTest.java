package com.mulcam.demo.service;

import java.util.List;

public class CsvTest {

	public static void main(String[] args) {
		CsvUtil2 cu = new CsvUtil2();
		List<List<String>> list = cu.readCsv("c:/Temp/goodMindLoaction.csv", ",", 1);
		for (List<String> row : list) {
			for (String s : row) {
				System.out.print(s + "  ");
			}
			System.out.println();
		}	
		
		cu.writeCsv("c:/Temp/sample3.csv", list);	
			
	}

}
