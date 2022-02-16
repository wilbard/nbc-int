package com.nbc.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Collections;

@Component
public class NbcUtilities {

    public void sendReport(String smsApi) {
        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
        try {
            HttpPost post = new HttpPost("https://httpbin.org/post");
            post.setHeader("Content-type", "application/json");
            post.setEntity(new StringEntity(smsApi));

            HttpResponse response = httpClient.execute(post);
            InputStream inputStream = response.getEntity().getContent();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception(response.getStatusLine().getReasonPhrase());
            }

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }

            bufferedReader.close();
            inputStream.close();

            System.out.println(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpHeaders reportOutput(ByteArrayResource byteArrayResource, String fileType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            responseHeaders.setContentLength(byteArrayResource.contentLength());
            responseHeaders.add("X-Frame-Options", "");
            responseHeaders.add("print", "");
            if (fileType.equalsIgnoreCase("html")) {
                responseHeaders.setContentType(MediaType.valueOf("text/html"));
            } else if (fileType.equalsIgnoreCase("xlsx")) {
                responseHeaders.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                responseHeaders.put("Content-Disposition", Collections.singletonList("attachment; filename=PartnerTransactions.xlsx"));
            } else {
                responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
                responseHeaders.put("Content-Disposition", Collections.singletonList("filename=PartnerTransactions.pdf"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseHeaders;
    }

    public HttpHeaders reportOutputError(String fileType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        try {
            String invalidFile = "Invalid File";
            ByteArrayResource byteArrayResource = new ByteArrayResource(invalidFile.getBytes());
            responseHeaders.setContentLength(byteArrayResource.contentLength());
            responseHeaders.add("X-Frame-Options", "");
            if (fileType.equalsIgnoreCase("html")) {
                responseHeaders.setContentType(MediaType.valueOf("text/html"));
            } else if (fileType.equalsIgnoreCase("xlsx")) {
                responseHeaders.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                responseHeaders.put("Content-Disposition", Collections.singletonList("attachment; filename=invalid.xlsx"));
            } else {
                responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
                responseHeaders.put("Content-Disposition", Collections.singletonList("inline; filename=invalid.pdf"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseHeaders;
    }

    public String getRandomNumber() {
        String res = "9999";
        try {
            double randomNum = Math.random() * 999999 + 1;
            String strRandom = Double.toString(randomNum);
            int intRandom = Integer.parseInt(strRandom.substring(0, strRandom.indexOf(".")));
            res = String.format("%06d", intRandom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void storeFile(MultipartFile file, String fileName, String fileType, String fileDirectory) throws IOException {
        try {
            Path filePath = Paths.get(fileDirectory + "/" + fileName + "." + fileType);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(String file_name, String file_type, String file_folder){
        boolean resp = false;
        try{
            File the_file = new File( file_folder + file_name + "." + file_type);
            if (!the_file.exists()){
                resp = true;
            }else {
                if (the_file.delete()) {
                    resp = true;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return resp;
    }

    public String amountFormat(double amt) {
        String amount = "0";
        try {
            //double amt = Double.parseDouble(amount);
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            amount = formatter.format(amt);
            if (amt == 0) {
                amount = "0.00";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    public String formatPhoneNumber(String dialCode, String phoneNumber) {
        String formattedNumber = phoneNumber;
        try {
            String validNumber = phoneNumber.replaceAll("\\D", "");
            String validDialCode = dialCode.replaceAll("\\D", "");
            int phoneLength = validNumber.length();
            if (phoneLength <= 9) {
                formattedNumber = validDialCode + validNumber;
            } else {
                formattedNumber = validDialCode + validNumber.substring(phoneLength - 9, phoneLength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedNumber;
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }
}
