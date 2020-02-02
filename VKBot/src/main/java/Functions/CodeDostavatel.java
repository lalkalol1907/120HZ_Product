package Functions;

import java.io.FileInputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class CodeDostavatel{

    public static int coded() throws IOException {
    int res = 0;
    Properties prop = new Properties();
    prop.load(new FileInputStream("/home/l/lalkalol/Bot/vkconfig.properties"));
    String codepath = prop.getProperty("codepath");
        try {
            File f = new File(codepath);
            FileInputStream ios = new FileInputStream(f);
            XSSFWorkbook workbook = new XSSFWorkbook(ios);
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);

            res = (int) Math.round(row.getCell(0).getNumericCellValue());

            ios.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}