package com.tuse.tuse.services;

import com.tuse.tuse.models.*;
import com.tuse.tuse.repositories.PurchaseRepo;
import com.tuse.tuse.requests.PurchaseRequest;
import com.tuse.tuse.responses.PurchaseResponse;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepo purchaseRepo;
    private final AccountService acctService;
    private final StockService stockService;
    private final UserStockService userStockService;
    private final MessageService msgService;

    @Autowired
    public PurchaseService(PurchaseRepo purchaseRepo, StockService stockService, AccountService acctService, UserStockService userStockService, MessageService msgService) {
        this.purchaseRepo = purchaseRepo;
        this.stockService = stockService;
        this.acctService = acctService;
        this.userStockService = userStockService;
        this.msgService = msgService;
    }

    @Transactional
    public void execute(PurchaseRequest purchaseRequest, User buyingUser) throws InvalidUserInputException, ResourcePersistenceException{

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Integer> invalidQuantityInput = Objects::nonNull;
        Predicate<Double> invalidBuyingPriceInput = Objects::nonNull;

        if(!notNullOrEmpty.test(purchaseRequest.getSymbol()))
            throw new InvalidUserInputException("No symbol was found");
        if(!invalidQuantityInput.test(purchaseRequest.getQuantity()) || purchaseRequest.getQuantity() == 0)
            throw new InvalidUserInputException("No quantity was found");
        if(!invalidBuyingPriceInput.test(purchaseRequest.getBuyingPrice()))
            throw new InvalidUserInputException("No buying price was found");

        String symbol = purchaseRequest.getSymbol().toUpperCase();
        Integer quantity = purchaseRequest.getQuantity();
        Double buyingPrice = purchaseRequest.getBuyingPrice();
        List<UserStock> userStocks = userStockService.getUserStocksBySymbolQuantityPrice(symbol, quantity, buyingPrice, buyingUser.getUserId());

        Stock stock = stockService.getBySymbol(symbol);

        if(userStocks.isEmpty()){
            // this ensures that a user is able to buy a stock at a lower price than the listing price
            // if certain conditions are met
            boolean priceQuantityConditionMet= (quantity >= stock.getFavorableQuantity() && buyingPrice >= stock.getPrice() * stock.getPercentOfPriceToSell());

            if(priceQuantityConditionMet || buyingPrice >= stock.getPrice()){

                if(purchaseRequest.getQuantity() > stock.getTotalShares()) throw new UnauthorizedException("Quantity exceeds the total shares available");

                purchaseAtListingPrice(buyingUser, stock, purchaseRequest);

            } else throw new ResourceNotFoundException("No available trade matches your bid at this time");

            return;
        }

        UserStock bestAvailableStock = userStocks.get(0);       // userStocks are ordered by priceToSell ascending

        Purchase purchase = new Purchase();
        purchase.setStock(stock);
        purchase.setQuantity(purchaseRequest.getQuantity());

        Double amount = purchaseRequest.getQuantity() * bestAvailableStock.getPriceToSell();
        Account userAccount = acctService.getUserAccount(buyingUser);
        if(amount > userAccount.getBalance()) throw new UnauthorizedException("You do not have sufficient fund to execute this trade");

        purchase.setAmount(amount);
        purchase.setBuyingPrice(bestAvailableStock.getPriceToSell());
        purchase.setPurchaseDate(new Date());
        purchase.setUser(buyingUser);

        acctService.updatePurchaseUserAccount(buyingUser, purchaseRequest);
        userStockService.updateUserStock(buyingUser, purchaseRequest);
        msgToUser(buyingUser, bestAvailableStock.getPriceToSell());

        bestAvailableStock.setQuantityOnSale(bestAvailableStock.getQuantityOnSale() - quantity);
        userStockService.save(bestAvailableStock);

        User saleUser = bestAvailableStock.getUser();
        acctService.updateSaleUserAccount(saleUser, amount);
        msgToUser(saleUser, symbol);

        if(!Objects.equals(purchaseRequest.getBuyingPrice(), stock.getPrice()))
            updateMarketCap(stock, purchaseRequest.getQuantity(), purchaseRequest.getBuyingPrice());
        stock.setVolume(stock.getVolume() + purchaseRequest.getQuantity());
        stockService.save(stock);

        purchaseRepo.save(purchase);
    }

    @Transactional
    public void msgToUser(User saleUser, String symbol){
        Message message = new Message();
        message.setTitle("Sale Update");
        message.setContent("Hooray! Your sale of the stock '"+symbol+"' has been completed, and your balance has been updated.");
        message.setToUser(saleUser);
        message.setSendDate(new Date());
        msgService.save(message);
    }

    @Transactional
    public void msgToUser(User buyingUser, Double price){
        Message message = new Message();
        message.setTitle("Purchase Update");
        message.setContent("Hooray! Your purchase was executed at the best available price ($ " +price+")" );
        message.setToUser(buyingUser);
        message.setSendDate(new Date());
        msgService.save(message);
    }

    @Transactional
    public void purchaseAtListingPrice(User user, Stock stock, PurchaseRequest purchaseRequest){

        Purchase purchase = new Purchase();
        purchase.setStock(stock);
        purchase.setQuantity(purchaseRequest.getQuantity());

        Double amount = purchaseRequest.getQuantity() * purchaseRequest.getBuyingPrice();
        Account userAccount = acctService.getUserAccount(user);
        if(amount > userAccount.getBalance()) throw new UnauthorizedException("You do not have sufficient fund to execute this trade");

        purchase.setAmount(amount);
        purchase.setBuyingPrice(purchaseRequest.getBuyingPrice());
        purchase.setPurchaseDate(new Date());
        purchase.setUser(user);

        acctService.updatePurchaseUserAccount(user, purchaseRequest);
        userStockService.updateUserStock(user, purchaseRequest);

        if(!Objects.equals(purchaseRequest.getBuyingPrice(), stock.getPrice()))
            updateMarketCap(stock, purchaseRequest.getQuantity(), purchaseRequest.getBuyingPrice());
        stock.setVolume(stock.getVolume() + purchaseRequest.getQuantity());
        stockService.save(stock);

        purchaseRepo.save(purchase);
    }

    public void updateMarketCap(Stock stock, int quantity, double buyingPrice){

        if(buyingPrice == 0) throw new InvalidUserInputException("Invalid Buying Price");

        if (buyingPrice > stock.getPrice()){
            Double currentPrice = stock.getPrice();
            double excess = (buyingPrice - currentPrice)*quantity;
            double newMarketCap = stock.getMarketCap() + excess;
            Double newPrice = newMarketCap / stock.getTotalShares();
            stock.setPrice(newPrice);
            stock.setMarketCap(newMarketCap);
            stock.setTrend(( (newPrice/currentPrice) - 1)*100);
            stockService.save(stock);
        }

        if (buyingPrice < stock.getPrice()){
            Double currentPrice = stock.getPrice();
            double deficit = (currentPrice - buyingPrice)*quantity;
            double newMarketCap = stock.getMarketCap() - deficit ;
            Double newPrice = newMarketCap / stock.getTotalShares();
            stock.setPrice(newPrice);
            stock.setMarketCap(newMarketCap);
            stock.setTrend(( (newPrice/currentPrice) - 1)*100);
            stockService.save(stock);
        }

    }

    @Transactional(readOnly = true)
    public List<PurchaseResponse> findPurchasesByUserId(Long userId){
        List<Purchase> purchases = purchaseRepo.findPurchasesByUserId(userId)
                .orElseThrow(ResourceNotFoundException::new);

        List<PurchaseResponse> purchaseList = new ArrayList<>();

        for(Purchase purchase : purchases){
            purchaseList.add(new PurchaseResponse(purchase));
        }

        return purchaseList;
    }

    @Transactional
    public List<Purchase> getPurchasesByUserId(Long userId){
        return purchaseRepo.findPurchasesByUserId(userId).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public List<Purchase> getPurchasesByUserIdAndSymbol(Long userId, String symbol){
        Stock stock = stockService.getBySymbol(symbol);
        return purchaseRepo.findPurchasesByUserIdAndSymbol(userId, stock.getStockId())
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponse> findAllPurchases(){

        List<Purchase> purchases = purchaseRepo.findAll();

        List<PurchaseResponse> list = new ArrayList<>();

        for(Purchase purchase : purchases){
            list.add(new PurchaseResponse(purchase));
        }

        return list;
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponse> getPurchasesBySymbol(String symbol){
        List<PurchaseResponse> allPurchases = findAllPurchases();
       return allPurchases.stream()
               .filter(purchase -> purchase.getSymbol().equalsIgnoreCase(symbol))
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PurchaseResponse> getPurchasesByCompany(String company){
        List<PurchaseResponse> allPurchases = findAllPurchases();
        return allPurchases.stream()
                .filter(purchase -> purchase.getCompany().equalsIgnoreCase(company))
                .collect(Collectors.toList());
    }

    // save purchase from non-subscribers
    @Transactional
    public void save(PurchaseRequest purchaseRequest, User user) throws InvalidUserInputException, ResourcePersistenceException{


        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Integer> invalidQuantityInput = Objects::nonNull;
        Predicate<Double> invalidBuyingPriceInput = Objects::nonNull;

        if(!notNullOrEmpty.test(purchaseRequest.getSymbol()))
            throw new InvalidUserInputException("No symbol was found");
        if(!invalidQuantityInput.test(purchaseRequest.getQuantity()) || purchaseRequest.getQuantity() == 0)
            throw new InvalidUserInputException("No quantity was found");
        if(!invalidBuyingPriceInput.test(purchaseRequest.getBuyingPrice()))
            throw new InvalidUserInputException("No buying price was found");

        Stock stock = stockService.getStockBySymbol(purchaseRequest.getSymbol()).get(0);
        if(purchaseRequest.getQuantity() > stock.getTotalShares())
            throw new UnauthorizedException("Quantity exceeds the total shares available");

        Purchase purchase = new Purchase();
        purchase.setStock(stock);
        purchase.setQuantity(purchaseRequest.getQuantity());

        Double amount = purchaseRequest.getQuantity() * purchaseRequest.getBuyingPrice();

        purchase.setAmount(amount);
        purchase.setBuyingPrice(purchaseRequest.getBuyingPrice());
        purchase.setPurchaseDate(new Date());
        purchase.setUser(user);

        purchaseRepo.save(purchase);

    }

}
