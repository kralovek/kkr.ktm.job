package kkr.job.file2base.domains.common.components.iterableinput.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kkr.common.errors.BaseException;
import kkr.common.errors.FileException;
import kkr.job.file2base.domains.common.components.iterableinput.IterableInput;
import kkr.job.file2base.domains.common.components.iterableinput.IterableInputException;

public class IterableInputCSVFile extends IterableInputCSVFileFwk implements IterableInput<String> {
	private static Log LOGGER = LogFactory.getLog(IterableInputCSVFile.class);

	private LineNumberReader reader;
	private String currentLine;
	private FieldComparator filedComparator;

	private static class FieldComparator implements Comparator<String> {
		Map<String, Integer> headerOrder;

		public FieldComparator(Map<String, Integer> headerOrder) {
			super();
			this.headerOrder = headerOrder;
		}

		public int compare(String o1, String o2) {
			Integer order1 = headerOrder.get(o1);
			Integer order2 = headerOrder.get(o2);
			return order1 != null && order2 != null ? order1.compareTo(order2) : order1 != null ? -1 : order2 != null ? +1 : o1.compareTo(o2);
		}
	}

	public boolean isOpened() {
		testConfigured();
		return reader != null;
	}

	public void open() throws BaseException, IterableInputException {
		// close already opened file
		close();
		try {
			LOGGER.trace("BEGIN");
			// open the file
			try {
				reader = new LineNumberReader(new FileReader(file));
			} catch (FileNotFoundException ex) {
				throw new FileException(file, "Le CSV fichier n'existe pas: " + file.getAbsolutePath(), ex);
			}
			// ignore the fist lines
			String line = null;
			try {
				for (int i = 0; i < ignoredLines && (line = reader.readLine()) != null; i++) {
				}
				if (ignoredLines > 0 && line == null) {
					IterableInputCSVFileException ex = new IterableInputCSVFileException(
							"Le fichier doit commencer par " + ignoredLines + " lignes � ignorer" + file.getAbsolutePath());
					ex.setFile(file);
					ex.setLine(reader.getLineNumber());
					throw ex;
				}
			} catch (IOException ex) {
				throw new FileException(file, "Impossible de lire le fichier: " + file.getAbsolutePath(), ex);
			}
			// read the header line
			if (hasHeader) {
				String headerLine;
				try {
					headerLine = reader.readLine();
				} catch (IOException ex) {
					throw new FileException(file, "Impossible de lire le fichier: " + file.getAbsolutePath(), ex);
				}
				if (headerLine == null) {
					IterableInputCSVFileException ex = new IterableInputCSVFileException("Le fichier doit contenir une ent�te sur la ligne '"
							+ (ignoredLines + 1) + "', mais il ne la contient pas: " + file.getAbsolutePath());
					ex.setFile(file);
					ex.setLine(reader.getLineNumber());
					throw ex;
				}
				if (headerLine.trim().isEmpty()) {
					IterableInputCSVFileException ex = new IterableInputCSVFileException("Le fichier doit contenir une ent�te sur la ligne '"
							+ (ignoredLines + 1) + "', mais cette ligne est vide: " + file.getAbsolutePath());
					ex.setFile(file);
					ex.setLine(reader.getLineNumber());
					throw ex;
				}
				headerArray = headerLine.split(String.valueOf(COLUMN_SEPARATOR));
				headerOrder = new HashMap<String, Integer>();
				for (int i = 0; i < headerArray.length; i++) {
					String trimedField = headerArray[i].trim();
					if (trimedField.isEmpty()) {
						IterableInputCSVFileException ex = new IterableInputCSVFileException("Le nom d'une champs sur la ligne de l'ent�te est vide");
						ex.setFile(file);
						ex.setLine(reader.getLineNumber());
						throw ex;
					}
					headerArray[i] = trimedField;
					headerOrder.put(trimedField, i);
				}
			}
			// Read the fist line
			line = null;
			try {
				while ((line = reader.readLine()) != null) {
					if (line.trim().isEmpty()) {
						continue;
					} else {
						currentLine = line;
						break;
					}
				}
			} catch (IOException ex) {
				throw new FileException(file, "Impossible de lire le fichier: " + file.getAbsolutePath(), ex);
			}
			filedComparator = new FieldComparator(headerOrder);
		} catch (IterableInputException ex) {
			close();
			throw ex;
		} catch (BaseException ex) {
			close();
			throw ex;
		} finally {
			LOGGER.trace("END");
		}
	}

	public void close() {
		try {
			LOGGER.trace("BEGIN");
			testConfigured();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
				reader = null;
			}
			currentLine = null;
			filedComparator = null;
			LOGGER.trace("OK");
		} finally {
			LOGGER.trace("END");
		}
	}

	public String getSource() {
		testConfigured();
		return file.getName();
	}

	public boolean hasNext() throws BaseException {
		try {
			LOGGER.trace("BEGIN");
			testConfigured();
			if (!isOpened()) {
				throw new FileException(file, "Le fichier n'est pas ouvert: " + file.getAbsolutePath());
			}
			boolean retval = currentLine != null;
			LOGGER.trace("OK");
			return retval;
		} finally {
			LOGGER.trace("END");
		}
	}

	public Map<String, String> readNext() throws BaseException {
		try {
			LOGGER.trace("BEGIN");
			testConfigured();
			if (!isOpened()) {
				throw new FileException(file, "Le fichier n'est pas ouvert: " + file.getAbsolutePath());
			}
			if (currentLine == null) {
				LOGGER.trace("OK");
				return null;
			}

			//
			// Work current line
			//
			String[] valuesArray = splitCsvLine(currentLine, COLUMN_SEPARATOR);
			Map<String, String> valuesMap = new TreeMap<String, String>(filedComparator);
			int length = valuesArray.length > headerArray.length ? valuesArray.length : headerArray.length;
			for (int i = 0; i < length; i++) {
				if (i < headerArray.length && i < valuesArray.length) {
					valuesMap.put(headerArray[i], valuesArray[i]);
				} else if (i < headerArray.length) {
					valuesMap.put(headerArray[i], null);
				} else {
					valuesMap.put("UNKNOWN_" + (i - headerArray.length), valuesArray[i]);
				}
			}

			//
			// Read next line
			//
			try {
				while ((currentLine = reader.readLine()) != null) {
					if (currentLine.trim().isEmpty()) {
						continue;
					} else {
						break;
					}
				}
			} catch (IOException ex) {
				throw new FileException(file, "Impossible de lire le fichier: " + file.getAbsolutePath(), ex);
			}

			LOGGER.trace("OK");
			return valuesMap;
		} finally {
			LOGGER.trace("END");
		}
	}

	public static String[] splitCsvLine(String parametersLine, char separator) {
		if (parametersLine == null) {
			return new String[0];
		}

		List<String> listParameters = new ArrayList<String>();
		int iPos;
		Character c = null;
		for (iPos = 0; iPos < parametersLine.length();) {
			int begin = iPos;
			Character quotation = null;

			for (; iPos < parametersLine.length(); iPos++) {
				c = parametersLine.charAt(iPos);
				if (quotation == null) {
					//if (c == '\'' || c == '"') {
					if (c == '"') {
						quotation = c;
						continue;
					}
				} else {
					if (c == quotation) {
						quotation = null;
					}
					continue;
				}
				if (c == separator) {
					break;
				}
			}
			String value = parametersLine.substring(begin, iPos);
			if (value.length() >= 2 && (value.charAt(0) == '\'' || value.charAt(0) == '"') && value.charAt(0) == value.charAt(value.length() - 1)) {
				value = value.substring(1, value.length() - 1);
			}
			listParameters.add(value);
			iPos++;
		}
		if (c == null) {
			return new String[]{""};
		}
		if (c == separator) {
			listParameters.add("");
		}
		return listParameters.toArray(new String[listParameters.size()]);
	}
}
