package com.nbc.rest;

import com.nbc.model.PartnerTransaction;
import com.nbc.resource.FileProcessor;
import com.nbc.service.BTransactionService;
import com.nbc.service.NbcStaticService;
import com.nbc.service.PartnerTransactionService;
import com.nbc.util.EmailUtilities;
import com.nbc.util.NbcUtilities;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/bank-b-engine/api/v1")
public class TransactionController {
    
    private NbcStaticService nbcStaticService;
    private FileProcessor fileProcessor;
    private NbcUtilities nbcUtilities;
    private EmailUtilities emailUtilities;
    private PartnerTransactionService partnerTransactionService;
    private BTransactionService bTransactionService;
    
    public TransactionController(NbcStaticService nbcStaticService, FileProcessor fileProcessor, NbcUtilities nbcUtilities, EmailUtilities emailUtilities,
                                 PartnerTransactionService partnerTransactionService, BTransactionService bTransactionService) {
        this.nbcStaticService = nbcStaticService;
        this.fileProcessor = fileProcessor;
        this.nbcUtilities = nbcUtilities;
        this.emailUtilities = emailUtilities;
        this.partnerTransactionService = partnerTransactionService;
        this.bTransactionService = bTransactionService;
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> addStudentBatch(@RequestParam("file") MultipartFile file, @RequestHeader("institution-name") String institutionName) {
        HttpStatus httpStatus = HttpStatus.OK;
        JSONObject response = new JSONObject();
        try {
            if (institutionName.equalsIgnoreCase("X") || institutionName.equalsIgnoreCase("Y")) {
                String batch_folder = this.nbcStaticService.findByConfigName("batch_folder").getStringValue();
                String fileName = file.getOriginalFilename();
                int extensionPosition = fileName.lastIndexOf(".");
                if (fileName.substring(extensionPosition + 1).equalsIgnoreCase("xlsx") || fileName.substring(extensionPosition + 1).equalsIgnoreCase("csv")) {
                    String extension = "xlsx";
                    if (fileName.substring(extensionPosition + 1).equalsIgnoreCase("csv")) {
                        extension = "csv";
                    }

                    String newFileName = String.format("%d", System.currentTimeMillis());
                    this.nbcUtilities.deleteFile(newFileName, extension, batch_folder);
                    this.nbcUtilities.storeFile(file, newFileName, extension, batch_folder);

                    if (this.fileProcessor.addTransactions(newFileName, extension, batch_folder, institutionName)) {
                        response.put("status", "success");
                        response.put("message", "Transactions added successfully");
                    } else {
                        response.put("status", "fail");
                        response.put("message", "Failed to upload transactions, please make sure your excel file is of xlsx or csv format");
                        httpStatus = HttpStatus.EXPECTATION_FAILED;
                    }
                } else {
                    response.put("status", "fail");
                    response.put("message", "Failed! Please use xlsx or csv excel format");
                    httpStatus = HttpStatus.EXPECTATION_FAILED;
                }
            } else {
                response.put("status", "error");
                response.put("message", "Invalid Institution");
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Errors occurred, Please try again!!");
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        response.put("code", httpStatus.value());
        return new ResponseEntity<>(response.toString(), httpStatus);
    }

    @RequestMapping(value = "/send/email", method = RequestMethod.POST)
    public ResponseEntity<String> sendWebsiteEmail() {
        JSONObject response = new JSONObject();
        HttpStatus httpStatus;
        try {
            List<PartnerTransaction> Xtransactions = this.partnerTransactionService.findAll("X");
            double xSum = 0;
            for (PartnerTransaction transaction : Xtransactions) {
                if (this.bTransactionService.findByTransactionRef(transaction.getTransactionRef()) != null) {
                    xSum = xSum + transaction.getAmount();
                }
            }
            JSONObject dataJson = new JSONObject();
            String subject = "Bank B and Company X Total Mismatch Amount for February 16 is" + this.nbcUtilities.amountFormat(xSum);
            String message = dataJson.getString("message") + "\n\n" + "Phone: " + dataJson.getString("phone");
            if (this.emailUtilities.sendWebsiteEmail(dataJson.getString("from"), dataJson.getString("name"), dataJson.getString("email"), subject, message)) {
                httpStatus = HttpStatus.OK;
                response.put("status", "success");
                response.put("message", "Email sent successfully");
            } else {
                httpStatus = HttpStatus.EXPECTATION_FAILED;
                response.put("status", "failed");
                response.put("message", "Failed to send email");
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = HttpStatus.BAD_REQUEST;
            response.put("status", "error");
            response.put("message", "Errors occurred, Please try again");
        }
        response.put("code", httpStatus.value());
        return new ResponseEntity<>(response.toString(), httpStatus);
    }
}
