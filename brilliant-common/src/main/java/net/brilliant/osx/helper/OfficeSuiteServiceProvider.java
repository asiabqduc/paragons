/**
 * 
 */
package net.brilliant.osx.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.poifs.filesystem.FileMagic;

import lombok.Builder;
import net.brilliant.common.CommonUtility;
import net.brilliant.common.ListUtility;
import net.brilliant.exceptions.CommonException;
import net.brilliant.model.ExecutionContext;
import net.brilliant.osx.model.DataWorkbook;
import net.brilliant.osx.model.DataWorksheet;
import net.brilliant.osx.model.OSXConstants;
import net.brilliant.osx.model.OfficeDocumentType;
import net.brilliant.osx.model.OfficeMarshalType;
import net.brilliant.osx.model.OsxBucketContainer;

/**
 * @author bqduc
 *
 */
@Builder
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OfficeSuiteServiceProvider {
	protected OfficeDocumentType detectOfficeDocumentType(InputStream inputStream) throws CommonException {
		OfficeDocumentType excelSheetType = OfficeDocumentType.INVALID;
		InputStream checkInputStream = null;
		checkInputStream = FileMagic.prepareToCheckMagic(inputStream);
		try {
			if (FileMagic.OOXML.equals(FileMagic.valueOf(checkInputStream))) {
				excelSheetType = OfficeDocumentType.XSSF_WORKBOOK;
			} else {
				excelSheetType = OfficeDocumentType.HSSF_WORKBOOK;
			}
		} catch (IOException e) {
			throw new CommonException(e);
		}
		return excelSheetType;
	}

	protected DataWorkbook readXlsxByEventHandler(final Map<?, ?> params) throws CommonException {
		return XSSFEventDataHelper.instance(params).readXlsx();
	}

	protected DataWorkbook readXlsxByStreaming(final Map<?, ?> params) throws CommonException {
		return OfficeStreamingReaderHealper.builder().build().readXlsx(params);
	}

	public DataWorkbook readExcelFile(final Map<?, ?> parameters) throws CommonException {
		DataWorkbook workbookContainer = null;
		OfficeMarshalType officeMarshalType = OfficeMarshalType.STREAMING;
		if (parameters.containsKey(OSXConstants.OFFICE_EXCEL_MARSHALLING_DATA_METHOD)) {
			officeMarshalType = (OfficeMarshalType) parameters.get(OSXConstants.OFFICE_EXCEL_MARSHALLING_DATA_METHOD);
		}

		if (OfficeMarshalType.EVENT_HANDLER.equals(officeMarshalType)) {
			workbookContainer = this.readXlsxByEventHandler(parameters);
		} else if (OfficeMarshalType.STREAMING.equals(officeMarshalType)) {
			workbookContainer = this.readXlsxByStreaming(parameters);
		} else {
			// Not implemented yet
		}
		return workbookContainer;
	}

	public OsxBucketContainer readOfficeDataInZip(final ExecutionContext executionContextParams) throws CommonException {
		OsxBucketContainer bucketContainer = OsxBucketContainer.instance();
		File zipFile = null;
		Map<String, InputStream> zipInputStreams = null;
		Map<String, Object> processingParameters = ListUtility.createMap();
		OfficeDocumentType officeDocumentType = OfficeDocumentType.INVALID;
		DataWorkbook workbookContainer = null;
		InputStream zipInputStream = null;
		Map<String, List<String>> sheetIdsMap = null;
		List<String> worksheetIds = null;
		Map<String, String> passwordMap = null;
		try {
			zipFile = (File) executionContextParams.get(OSXConstants.COMPRESSED_FILE);
			zipInputStreams = CommonUtility.extractZipInputStreams(zipFile, (List<String>) executionContextParams.get(OSXConstants.ZIP_ENTRY));
			if (zipInputStreams.isEmpty()) {
				return bucketContainer;
			}

			passwordMap = (Map) executionContextParams.get(OSXConstants.ENCRYPTION_KEYS);
			sheetIdsMap = (Map) executionContextParams.get(OSXConstants.PROCESSING_DATASHEET_IDS);
			for (String zipEntry : zipInputStreams.keySet()) {
				zipInputStream = zipInputStreams.get(zipEntry);
				officeDocumentType = detectOfficeDocumentType(zipInputStream);
				if (!OfficeDocumentType.isExcelDocument(officeDocumentType)) {
					continue;
				}

				worksheetIds = (List<String>) sheetIdsMap.get(zipEntry);
				processingParameters.putAll(executionContextParams.getContextData());
				processingParameters.remove(OSXConstants.COMPRESSED_FILE);
				processingParameters.put(OSXConstants.INPUT_STREAM, zipInputStream);
				processingParameters.put(OSXConstants.PROCESSING_DATASHEET_IDS, worksheetIds);
				processingParameters.put(OSXConstants.ENCRYPTION_KEYS, (String) passwordMap.get(zipEntry));
				workbookContainer = readExcelFile(processingParameters);
				if (null != workbookContainer) {
					bucketContainer.put(zipEntry, workbookContainer);
				}
			}
		} catch (Exception e) {
			throw new CommonException(e);
		}
		return bucketContainer;
	}

	public OsxBucketContainer extractOfficeDataFromZip(final ExecutionContext executionContextParams) throws CommonException {
		OsxBucketContainer bucketContainer = OsxBucketContainer.instance();
		File zipFile = null;
		Map<String, InputStream> zipInputStreams = null;
		Map<String, Object> processingParameters = ListUtility.createMap();
		OfficeDocumentType officeDocumentType = OfficeDocumentType.INVALID;
		DataWorkbook workbookContainer = null;
		InputStream zipInputStream = null;
		Map<String, List<String>> sheetIdsMap = null;
		List<String> workbookIds = null;
		Map<String, String> passwordMap = null;
		try {
			if (executionContextParams.containsKey(OSXConstants.MASTER_BUFFER_DATA_BYTES) && Boolean.FALSE.equals(executionContextParams.get(OSXConstants.FROM_ATTACHMENT))) {
				zipFile = CommonUtility.createDataFile((String)executionContextParams.get(OSXConstants.MASTER_ARCHIVED_FILE_NAME), (byte[])executionContextParams.get(OSXConstants.MASTER_BUFFER_DATA_BYTES));
			} /*else if (executionContextParams.containKey(OSXConstants.MASTER_BUFFER_DATA_BYTES) && executionContextParams.containKey(OSXConstants.MASTER_ARCHIVED_FILE_NAME)) {
				zipFile = CommonUtility.createDataFile((String)executionContextParams.get(OSXConstants.MASTER_ARCHIVED_FILE_NAME), (byte[])executionContextParams.get(OSXConstants.MASTER_BUFFER_DATA_BYTES));
			}*/

			if (null==zipFile) {
				return bucketContainer;
			}

			if (executionContextParams.containsKey(OSXConstants.ENCRYPTION_KEYS)) {
				passwordMap = (Map) executionContextParams.get(OSXConstants.ENCRYPTION_KEYS);
			}

			if (executionContextParams.containsKey(OSXConstants.PROCESSING_DATASHEET_IDS)) {
				sheetIdsMap = (Map) executionContextParams.get(OSXConstants.PROCESSING_DATASHEET_IDS);
			} else if (executionContextParams.containsKey(OSXConstants.MAPPING_DATABOOKS_DATASHEETS)) {
				sheetIdsMap = (Map) executionContextParams.get(OSXConstants.MAPPING_DATABOOKS_DATASHEETS);
			}

			if (executionContextParams.containsKey(OSXConstants.PROCESSING_DATABOOK_IDS)) {
				workbookIds = (List<String>)executionContextParams.get(OSXConstants.PROCESSING_DATABOOK_IDS);
			}
			zipInputStreams = CommonUtility.extractZipInputStreams(zipFile, workbookIds);
			
			for (String zipEntry : zipInputStreams.keySet()) {
				zipInputStream = zipInputStreams.get(zipEntry);
				officeDocumentType = detectOfficeDocumentType(zipInputStream);
				if (!OfficeDocumentType.isExcelDocument(officeDocumentType)) {
					continue;
				}

				if (null != sheetIdsMap) {
					processingParameters.put(OSXConstants.PROCESSING_DATASHEET_IDS, sheetIdsMap.get(zipEntry));
				}
				processingParameters.putAll(executionContextParams.getContextData());
				processingParameters.remove(OSXConstants.COMPRESSED_FILE);
				processingParameters.put(OSXConstants.INPUT_STREAM, zipInputStream);
				processingParameters.put(OSXConstants.ENCRYPTION_KEYS, (String) passwordMap.get(zipEntry));
				workbookContainer = readExcelFile(processingParameters);
				if (null != workbookContainer) {
					bucketContainer.put(zipEntry, workbookContainer);
				}
			}
		} catch (Exception e) {
			throw new CommonException(e);
		}
		return bucketContainer;
	}

	public static void main(String[] args) throws Exception {
		String zipFileName = "D:\\git\\paramount\\msp-osx\\src\\main\\resources\\data\\develop_data.zip";
		OsxBucketContainer bucketContainer = OfficeSuiteServicesHelper.builder().build().loadDefaultZipConfiguredData(new File(zipFileName));
		DataWorkbook workbookContainer = null;
		Set<Object> keys = bucketContainer.getKeys();
		for (Object key : keys) {
			workbookContainer = (DataWorkbook) bucketContainer.get(key);
			System.out.println("############################### " + key + " ###############################");
			displayWorkbookContainer(workbookContainer);
		}

		if (null != keys)
			return;
	}

	protected static void displayWorkbookContainer(DataWorkbook workbookContainer) {
		for (DataWorksheet worksheetContainer : workbookContainer.datasheets()) {
			System.out.println("Sheet: " + worksheetContainer.getId());
			for (List<?> dataRow : worksheetContainer.getValues()) {
				System.out.println(dataRow);
			}
			System.out.println("============================DONE==============================");
		}
	}

	protected static void testReadXlsxDocuments() {

	}
}
