package com.tuse.tuse.services;

import com.tuse.tuse.models.UserStock;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.UserStockRepo;
import com.tuse.tuse.requests.PurchaseRequest;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class UserStockService {

    private final UserStockRepo userStockRepo;

    @Autowired
    public UserStockService(UserStockRepo userStockRepo) {

        this.userStockRepo = userStockRepo;
    }

    @Transactional
    public void updateUserStock(User user, PurchaseRequest purchaseRequest){

        UserStock userStock = new UserStock();
        if(userStockRepo
                .findUserStockByUserIdAndSymbol(user.getUserId(), purchaseRequest.getSymbol())
                .isPresent()) {
            userStock = userStockRepo
                    .findUserStockByUserIdAndSymbol(user.getUserId(), purchaseRequest.getSymbol())
                    .orElseThrow(ResourceNotFoundException::new);
            userStock.setQuantity(userStock.getQuantity() + purchaseRequest.getQuantity());
        } else{
            userStock.setSymbol(purchaseRequest.getSymbol());
            userStock.setQuantity(purchaseRequest.getQuantity());
            userStock.setUser(user);
        }

        userStockRepo.save(userStock);
    }

    // this method is for when a user is selling a stock
    @Transactional
    public List<UserStock> getUserStocks(User user){

        if(user == null) throw new ResourceNotFoundException("No user was found");

        return userStockRepo
                .findUserStocksByUserId(user.getUserId())
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public UserStock getUserStockById(User user, Long userStockId){

        if(user == null) throw new ResourceNotFoundException("No user was found");
        List<UserStock> userStockList = getUserStocks(user);
        UserStock userStock = userStockRepo.findById(userStockId).orElseThrow(ResourceNotFoundException::new);
        if(userStockList.contains(userStock)) return userStock;
        else throw new ResourceNotFoundException("This stock was not found in user's stock list");
    }

    @Transactional
    public UserStock getUserStockByUserIdAndSymbol(User user, String symbol){

        if(!userStockRepo.findUserStockByUserIdAndSymbol(user.getUserId(), symbol).isPresent()){
            throw new ResourceNotFoundException("This stock does not exist in your portfolio");
        }

        return userStockRepo
                .findUserStockByUserIdAndSymbol(user.getUserId(), symbol)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void save(UserStock userStock){
        userStockRepo.save(userStock);
    }

    @Transactional
    public List<UserStock> getUserStocksBySymbolQuantityPrice(String symbol, Integer buyingQuantity, Double buyingPrice, Long buyingUserId){

        return userStockRepo
                .findUserStocksBySymbolQuantityPrice(symbol, buyingQuantity, buyingPrice, buyingUserId);
    }
}
