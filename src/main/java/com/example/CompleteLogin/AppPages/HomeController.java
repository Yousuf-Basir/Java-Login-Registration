package com.example.CompleteLogin.AppPages;

import com.example.CompleteLogin.AppUser.AppUser;
import com.example.CompleteLogin.AppUser.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@AllArgsConstructor
public class HomeController {


    @GetMapping("/home")
    public String homePage(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = ((UserDetails) principal).getUsername();
        return "Hello " + userName;
    }

    private final AppUserRepository appUserRepository;
    @GetMapping("/list-all")
    public List<AppUser> listAll(){
        List<String> userList = new ArrayList<>();
        List<AppUser> appUsers = appUserRepository.findAll();

//        appUsers.stream().forEach((user)->{
//            userList.add(user.getUsername());
//        });
        return appUsers;
    }

    @GetMapping("start-progress")
    public String startProgress(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = ((UserDetails) principal).getUsername();
        StartThread startThread = new StartThread(appUserRepository, userName);
        startThread.start();
        return "progress started";
    }

    @GetMapping("get-progress")
    public int getProgress(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = ((UserDetails) principal).getUsername();
        return appUserRepository.findByEmail(userName).get().getProgress();
    }
}


class StartThread extends Thread{
    private final AppUserRepository appUserRepository;
    private final String userEmail;
    StartThread(AppUserRepository appUserRepository, String userEmail){
        this.appUserRepository = appUserRepository;
        this.userEmail = userEmail;
    }
    @SneakyThrows
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            AppUser appUser = appUserRepository.findByEmail(userEmail).get();
            appUser.setProgress(i);
            appUserRepository.save(appUser);
            Thread.sleep(1000);
        }
    }
}
