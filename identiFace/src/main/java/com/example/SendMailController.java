package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.function.SendMail;;
@Controller
public class SendMailController {
	
	@PostMapping("/LostSearching/SendMail")
	@ResponseBody
    public void sendMail(@ModelAttribute("userName") String userName,@ModelAttribute("lostName") String lostName
    					,@ModelAttribute("userEmail") String userEmail) {
		
		System.out.println("userName: "+userName+"\n"+"lostName: "+lostName+"\n"+"userEmail: "+userEmail+"\n");
		SendMail sendMail = new SendMail();
		sendMail.SendMail(userName, lostName, userEmail);
		System.out.println("寄信完成");
    }
}
