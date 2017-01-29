package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.repository.AccountRepository;
import sec.project.repository.MessageRepository;

@Controller
public class WallController {

    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/wall";
    }

    @RequestMapping(value = "/wall", method = RequestMethod.GET)
    public String loadWall(Authentication authentication, Model model) {
        model.addAttribute("currentUser", authentication.getName());
        model.addAttribute("messages", messageRepository.findAll());
        return "wall";
    }

    @RequestMapping(value = "/wall", method = RequestMethod.POST)
    public String submitMessage(Authentication authentication, @RequestParam String message) {
        if(!message.isEmpty()){
            Account account = accountRepository.findByUsername(authentication.getName());
            Message m = new Message(message);
            m.setAccount(account);
            messageRepository.save(m);
        }
        return "redirect:/wall";
    }
    
    @RequestMapping(value = "/wall/{id}", method = RequestMethod.DELETE)
    public String deleteMessage(Authentication authentication, @PathVariable Long id){
        messageRepository.delete(id);
        return "redirect:/wall";
    }
}
