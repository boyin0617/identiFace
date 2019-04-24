package com.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.storage.StorageFileNotFoundException;
import com.example.storage.StorageService;


@Controller
public class FileUploadController {


    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;  
    }
    
    @CrossOrigin
    @GetMapping("/file")
    @ResponseBody
    public  List<String> listUploadedFiles() throws IOException {
    	
    	//存照片到upload-dir
    	selectTenPics();

//        model.addAttribute("files", storageService.loadAll().map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                        "serveFile", path.getFileName().toString()).build().toString())
//                .collect(Collectors.toList()));
        System.out.println(storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList())   );
        
        
        return storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList());
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    //選10張照片，複製到專案裡的upload-dir
    public static void selectTenPics(){
    	  System.out.println("trainface");
    	  String path = "D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\outputface\\" ;
    	  File file = new File(path);
    	  String[] allfiles = file.list();
    	  int count = 0;
    	  
    	  for(int i = allfiles.length-1; i >= 0;i--) {
    	   System.out.println(allfiles[i]);
    	   System.out.println(path+allfiles[i]);
    	   
    	   Path sourcePath = Paths.get(path+allfiles[i]);
    	   Path destinationPath = Paths.get("D:\\Git\\repository\\identiFace\\upload-dir\\"+allfiles[i]);

    	   try {
    	    Files.copy(sourcePath, destinationPath);
    	   } catch (FileAlreadyExistsException e) {
    	    // destination file already exists
    	   } catch (IOException e) {
    	    // something else went wrong
    	    e.printStackTrace();
    	   }
    	      
    	      count++;
    	      
    	    if(count == 10) {
    	     break;
    	    }
    	  }

    	 }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}