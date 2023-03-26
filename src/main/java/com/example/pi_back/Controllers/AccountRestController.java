package com.example.pi_back.Controllers;

import com.example.pi_back.Entities.Account;
import com.example.pi_back.Entities.InternalService;
import com.example.pi_back.Entities.User;
import com.example.pi_back.Services.AccountService;
import com.example.pi_back.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/Account")
public class AccountRestController {
    private AccountService accountService;
    private UserService userService;
    @GetMapping("/all")
    List<Account> retrieveAllAccount() {
        return accountService.retrieveAllAccount();
    }
    @PostMapping("/add")
    ResponseEntity<String> AddAccount(@RequestBody Account account){
        if(!(accountService.retrieveAccount(account.getId())==null)){
            return new ResponseEntity<>("Already Existing Id", HttpStatus.BAD_REQUEST);
        }

/*TODO inexistant associated user support

   if(userService.retrieveUser(account.getUser().getId())==null){
            return new ResponseEntity<>("Associated user dosen't exist", HttpStatus.BAD_REQUEST);
        }
        Set<Account> data = Collections.<Account>emptySet();
        User dummy1=userService.retrieveUser(account.getUser().getId());
        User dummy2=account.getUser();
        dummy2.setAccounts([]);
        dummy2.setAccounts(data);
        boolean dummy3= dummy1.equals(dummy2);
        if(!userService.retrieveUser(account.getUser().getId()).equals(account.getUser())){
            return new ResponseEntity<>("Associated user is not matching", HttpStatus.BAD_REQUEST);
        }*/
        accountService.AddAccount(account);
        return new ResponseEntity<>("Account added sucessfully", HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> removeAccount (@PathVariable("id") Integer idAccount){
        if(accountService.retrieveAccount(idAccount)==null){
            return new ResponseEntity<>("The Account to be deleted dosen't exist", HttpStatus.NOT_FOUND);
        }
        accountService.removeAccount(idAccount);
        return new ResponseEntity<>("Account was deleted sucessfully", HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    ResponseEntity<Account> retrieveAccount (@PathVariable("id") Integer idAccount){
       Account Retrieved_Account=accountService.retrieveAccount(idAccount);
        if(Retrieved_Account==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
       return new ResponseEntity<>(Retrieved_Account, HttpStatus.OK);

    }
    @PutMapping("/update")
    ResponseEntity<String> updateAccount (@RequestBody Account account){
        if(accountService.retrieveAccount(account.getId())==null){
            return new ResponseEntity<>("Account Dosen't exist", HttpStatus.BAD_REQUEST);
        }
        accountService.updateAccount(account);
        return new ResponseEntity<>("Account updated sucessfully", HttpStatus.OK);
    }
    @PutMapping("/assignAccountToevent/{idaccount}/{idEvent}")
    Account assignEventToAccount(@PathVariable("idaccount") Integer idAccount,@PathVariable("idEvent") Integer idIService){
        return accountService.assignEventToAccount(idAccount,idIService);
    }

}
