package com.example.controller;


import com.example.entity.Client;
import com.example.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
//@SecurityRequirement(name = "Bearer Authentication")
public class ClientController {

    private final ClientService clientService;


//    @PostMapping("/sellers/{id}")
//    public String sendMatching(@PathVariable Long sellerId,@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return clientService.sendMatching(userDetails.getUser().getUsername().getId(),sellerId);
//    }
    @PostMapping("/sellers/{id}")
    public String sendMatching(@PathVariable Long sellerId,Long clientId){
        return clientService.sendMatching(clientId,sellerId);
    }


    @PostMapping("/buy/{sid}/{pid}")
    public String buyProduct(@PathVariable Long sid, @PathVariable Long pid, Client client){
        return clientService.buyProduct(client,sid,pid);
    }



}
