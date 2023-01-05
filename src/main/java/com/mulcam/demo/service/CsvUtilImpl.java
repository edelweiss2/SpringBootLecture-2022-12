package com.mulcam.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CsvUtilImpl implements CsvUtil {

	@Override
	public List<List<String>> readCsv(String filename) {
		return readCsv(filename, ",", 0);
	}

	@Override
	public List<List<String>> readCsv(String filename, String separator) {
		return readCsv(filename, separator, 0);
	}

	@Override
	public List<List<String>> readCsv(String filename, String separator, int skipLine) {
		List<List<String>> csvList = new ArrayList<>();
		File csv = new File(filename);
		BufferedReader br = null;
		String line = "";
		int lineNo = 0;
		
		try {
			br = new BufferedReader(new FileReader(csv));
			while((line = br.readLine()) != null) {
//				skipLine 동작이 행해져야 할 경우에는 continue
				if (skipLine > lineNo++)
					continue;
				String[] lineArray = line.split(separator);
//				List<String> aLine = new ArrayList<>();
//				String[] lineArray = line.split(separator);
//				for (String s: lineArray) {
//					aLine.add(s);
//				}
				List<String> aLine = Arrays.asList(lineArray);
				csvList.add(aLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return csvList;
	}

	@Override
	public void writeCsv(String filename, List<List<String>> dataList) {
		writeCsv(filename, dataList, ",");
	}

	@Override
	public void writeCsv(String filename, List<List<String>> dataList, String separator) {
		
	}
	
}
