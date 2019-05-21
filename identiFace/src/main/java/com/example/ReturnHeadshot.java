package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.CmdUtil;
import com.example.entity.trainResult;
import com.example.function.CreateTxtPath;
import com.example.function.GetFileSize;
import com.example.function.delFolderTxtFUNC;
import com.example.storage.StorageFileNotFoundException;
import com.example.storage.StorageService;
import com.example.FileUploadController;

@Controller
public class ReturnHeadshot {
 // 接收faceId，去大頭貼資料夾找對應的頭貼，將它移到server資料夾，讓前端秀出來
 @RequestMapping(value = "/headshotreturn", method = RequestMethod.POST)
 @ResponseBody
 @CrossOrigin
 public String uploadingPost(@RequestBody String faceId) throws Exception {
  faceId += ".jpg";
  System.out.println("headshot api start");
  Path sourcePath = Paths.get("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + faceId);
  Path destinationPath = Paths
    .get("D:\\Git\\repository\\identiFace\\upload-dir\\" + faceId);

  File headshot = new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + faceId);
  if (headshot.exists()) {
   System.out.println(faceId + "  大頭貼存在 !");

   try {
    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
   } catch (FileAlreadyExistsException e) {
    System.out.println("檔案已經存在");
   } catch (IOException e) {
    // something else went wrong
    e.printStackTrace();
   }
   return faceId;
  } else {
   return "沒有大頭貼 !";
  }
 }
}