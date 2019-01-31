package com.example;

import com.example.storage.StorageFileNotFoundException;
import com.example.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;
@Controller
public class FileUploadController {
    private final StorageService storageService;


    @Autowired

    public FileUploadController(StorageService storageService) {

        this.storageService = storageService;

    }


    //After Spring 4.3, we can use @GetMapping instead of @RequestMapping(value = "/", method = RequestMethod.GET)

    //@RequestMapping(value = "/", method = RequestMethod.GET)

    @GetMapping("/upload")

    public String listUploadedFiles(Model model) throws IOException {


        model.addAttribute("files", storageService

                .loadAll()

                .map(path ->

                        MvcUriComponentsBuilder

                                .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())

                                .build().toString())

                .collect(Collectors.toList()));

        

        return "uploadForm";

    }


    @GetMapping("/files/{filename:.+}")

    @ResponseBody

    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {


        Resource file = storageService.loadAsResource(filename);

        return ResponseEntity

                .ok()

                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")

                .body(file);

    }


    //After Spring 4.3, we can use @PostMapping instead of @RequestMapping(value = "/", method = RequestMethod.POST)

    @PostMapping("/upload")

    public String handleFileUpload(@RequestParam("file") MultipartFile file,

                                   RedirectAttributes redirectAttributes) {
    	
    	String filename = file.getOriginalFilename();
    	System.out.println(filename);

        storageService.store(file);

        redirectAttributes.addFlashAttribute("message",

                "You successfully uploaded " + file.getOriginalFilename() + "!");


        return "redirect:/";

    }


    @ExceptionHandler(StorageFileNotFoundException.class)

    @ResponseBody

    public ResponseEntity<Void> handleStorageFileNotFound(StorageFileNotFoundException exc) {

     System.out.println("Error");

        return ResponseEntity.notFound().build();

    }
}
