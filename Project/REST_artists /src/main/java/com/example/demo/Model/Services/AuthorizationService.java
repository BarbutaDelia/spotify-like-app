package com.example.demo.Model.Services;

import com.example.demo.Model.Exceptions.AuthorizationFailedException;
import com.example.demo.Model.Util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBException;

@Service
public class AuthorizationService {
    @Autowired
    private Config config;

    public String AuthorizeRequest(String token){
        String result;
        try{
            result = config.soapClient(config.marshaller()).AuthorizeUser(token);
            if(result.contains("Error")){
                switch (result) {
                    case "Error! Token has been blacklisted!" -> throw new AuthorizationFailedException("Token has been blacklisted!");
                    case "Error! Signature verification failed!" -> throw new AuthorizationFailedException("Signature verification failed!");
                    case "Error! Conflicting roles!" -> throw new AuthorizationFailedException("Conflicting roles!");
                    case "Error! Expired Signature!" -> throw new AuthorizationFailedException("Expired Signature!");
                    default -> {
                    }
                }
            }
        }
        catch(JAXBException e){
            throw new AuthorizationFailedException("Please log in again!");
        }
        return result;
    }
}
