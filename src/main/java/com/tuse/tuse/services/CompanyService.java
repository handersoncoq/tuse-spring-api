package com.tuse.tuse.services;

import com.tuse.tuse.models.Company;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.CompanyRepo;
import com.tuse.tuse.requests.NewCompanyRequest;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

@Service
public class CompanyService {

    private final CompanyRepo companyRepo;

    @Autowired
    public CompanyService(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    @Transactional
    public Company save(NewCompanyRequest companyRequest, User user)throws InvalidUserInputException, ResourcePersistenceException{

        if(user == null) throw new UnauthorizedException("Registration needed");

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Double> invalidPriceInput = Objects::nonNull;
        Predicate<Long> invalidOutStandingSharesInput = Objects::nonNull;

        if(!notNullOrEmpty.test(companyRequest.getName()))
            throw new InvalidUserInputException("Name is empty");
        if(!notNullOrEmpty.test(companyRequest.getSymbol()))
            throw new InvalidUserInputException("Symbol is empty");
        if(!notNullOrEmpty.test(companyRequest.getSector()))
            throw new InvalidUserInputException("Sector is empty");

        if(!invalidPriceInput.test(companyRequest.getInitialPrice()))
            throw new InvalidUserInputException("Invalid initial price");
        if(!invalidOutStandingSharesInput.test(companyRequest.getOutstandingShares()))
            throw new InvalidUserInputException("Invalid Outstanding Shares");

        isNameValid(companyRequest.getName());
        isOutstandingShareValid(companyRequest.getOutstandingShares());

        Company company = new Company(companyRequest);
        company.setMarketCap(companyRequest.getInitialPrice()*companyRequest.getOutstandingShares());
        company.setDateListed(new Date());

        return companyRepo.save(company);
    }

    private void isNameValid(String name) {

        if(companyRepo.findCompanyByName(name).isPresent())
            throw new ResourcePersistenceException("Company name exists");

    }
    private void isOutstandingShareValid(Long outstandingShares) {

        long maxOutstandingShares = 1000;

        if(outstandingShares > maxOutstandingShares)
            throw new UnsupportedOperationException("Number of outstanding shares is greater than max allowed");

    }



}
