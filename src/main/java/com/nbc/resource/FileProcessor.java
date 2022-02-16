package com.nbc.resource;

import com.google.gson.Gson;
import com.nbc.model.PartnerTransaction;
import com.nbc.service.PartnerTransactionService;
import com.nbc.util.NbcUtilities;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class FileProcessor {

    Logger logger = LoggerFactory.getLogger(PartnerTransaction.class);
    
    private NbcUtilities nbcUtilities;
    private PartnerTransactionService partnerTransactionService;
    
    public FileProcessor(NbcUtilities nbcUtilities, PartnerTransactionService partnerTransactionService) {
        this.nbcUtilities = nbcUtilities;
        this.partnerTransactionService = partnerTransactionService;
    }

    public boolean addTransactions(String fileName, String xlFormat, String batch_folder, String institution) {
        boolean response = false;
        try{
            /*String batch_folder = "/home/wilbard/uploads";*/
            Workbook workbook = null;
            FileInputStream fis = new FileInputStream(new File(batch_folder + "/" + fileName + "." + xlFormat));
            File checkFileExistence = new File(batch_folder + "/" + fileName + "." + xlFormat);
            if (!checkFileExistence.exists()){
                this.nbcUtilities.deleteFile(fileName, xlFormat, batch_folder);
            }
            if (xlFormat.equalsIgnoreCase("csv")){
                workbook = this.csvToXls(fileName, xlFormat, batch_folder);
            } else if (xlFormat.equalsIgnoreCase("xlsx")){
                OPCPackage pkg = OPCPackage.open(fis);
                workbook = new XSSFWorkbook(pkg);
            }
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++){
                Sheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()){
                    PartnerTransaction partnerTransactionData = new PartnerTransaction();
                    Row row = rowIterator.next();
                    if (row.getRowNum() == 0){
                        continue;
                    }
                    Iterator<Cell> cellIterator = row.cellIterator();
                    String transactionRef = null;
                    String account = null;
                    double amount = 0;
                    while (cellIterator.hasNext()){
                        Cell cell = cellIterator.next();
                        if (cell.getColumnIndex() == 0) {
                            if (cell.getCellTypeEnum() == CellType.STRING){
                                transactionRef = cell.getStringCellValue().trim();
                            }
                        }
                        if (cell.getColumnIndex() == 1) {
                            if (cell.getCellTypeEnum() == CellType.STRING){
                                account = cell.getStringCellValue().trim();
                            }
                        }
                        if (cell.getColumnIndex() == 2) {
                            if (cell.getCellTypeEnum() == CellType.NUMERIC){
                                amount = Double.parseDouble(String.valueOf(cell.getNumericCellValue()).trim());
                            }
                        }
                    }
                    if (transactionRef == null && account == null && amount == 0) {
                        continue;
                    }
                    if (transactionRef != null) {
                        if (this.partnerTransactionService.findByTransactionRef(transactionRef) == null) {
                            partnerTransactionData.setTransactionRef(transactionRef);
                        } else {
                            continue;
                        }
                    }
                    partnerTransactionData.setAccount(account);
                    partnerTransactionData.setAmount(amount);
                    partnerTransactionData.setInstitution(institution.toUpperCase());
                    PartnerTransaction results = this.partnerTransactionService.newPartnerTransaction(partnerTransactionData);
                    logger.info(new Gson().toJson(results));
                }
            }
            fis.close();
            response = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public HSSFWorkbook csvToXls(String fName, String xlFormat, String batch_folder) throws IOException {

        HSSFWorkbook hwb = new HSSFWorkbook();
        ArrayList arList = null;
        ArrayList al = null;
        String thisLine;
        int count = 0;
        FileInputStream fis = new FileInputStream(new File(batch_folder + "/" + fName + "." + xlFormat));
        DataInputStream myInput = new DataInputStream(fis);
        int i = 0;
        arList = new ArrayList();
        while ((thisLine = myInput.readLine()) != null)
        {
            al = new ArrayList();
            String strar[] = thisLine.split(",");
            for(int j=0;j<strar.length;j++)
            {
                al.add(strar[j]);
            }
            arList.add(al);
            System.out.println();
            i++;
        }

        try
        {
            HSSFSheet sheet = hwb.createSheet("new sheet");
            for(int k=0;k<arList.size();k++)
            {
                ArrayList ardata = (ArrayList)arList.get(k);
                HSSFRow row = sheet.createRow((short) 0+k);
                for(int p=0;p<ardata.size();p++)
                {
                    HSSFCell cell = row.createCell((short) p);
                    String data = ardata.get(p).toString();
                    if(data.startsWith("=")){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        data=data.replaceAll("\"", "");
                        data=data.replaceAll("=", "");
                        cell.setCellValue(data);
                    }else if(data.startsWith("\"")){
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(data);
                    }else{
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(data);
                    }
                }
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return hwb;
    }
}
