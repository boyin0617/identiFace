package com.example;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CreateTxtPath {
    // 新建文件
    // 输入：新建文件路径
    public static boolean createTxtFile(String filePath) {
        boolean flag = false;
        try {
            File newfile = new File(filePath);
            if (!newfile.exists()) {
                newfile.createNewFile();
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("文件创建失败！" + e);
        }
        return flag;
    }

    // 追加写文件
    // 输入：文件路径，内容
    public static boolean writeTxtFileAppend(String filePath, String content) throws IOException {
        boolean flag = false;
        try {
            // 构造函数中的第二个参数true表示以追加形式写文件
            FileWriter fw = new FileWriter(filePath, true);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            System.out.println("文件写入失败！" + e);
        }
        return flag;
    }

    // 覆盖写文件
    // 输入：文件路径，内容
    public static boolean writeTxtFile(String filePath, String content) throws Exception {
        boolean flag = false;
        FileOutputStream fileOutputStream = null;
        File file = new File(filePath);
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes("UTF-8"));
            fileOutputStream.close();
            flag = true;
        } catch (Exception e) {
            System.out.println("文件写入失败！" + e);
        }
        return flag;
        
        
        
        
    }

    // 读文件
    // 输入：文件路径
    // 输出：文件信息
    public static String readTxtFile(String filePath) {
        String result = "";
        File file = new File(filePath);
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String s = null;
            while ((s = br.readLine()) != null) {
                result = result + s;
                System.out.println(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String args[]) throws Exception {
        String file = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\shishi.txt";
        String content1 = "bigassfighter"  + "\r\n";
        String content2 = "second";
        createTxtFile(file);
        writeTxtFileAppend(file, content1);
        String result = readTxtFile(file);
    }
}
