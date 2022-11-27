package com.tuse.tuse.services;

import com.tuse.tuse.models.Company;
import com.tuse.tuse.models.User;
import com.tuse.tuse.repositories.CompanyRepo;
import com.tuse.tuse.requests.create.NewCompanyRequest;
import com.tuse.tuse.requests.update.UpdateCompanyRequest;
import com.tuse.tuse.responses.CompanyResponse;
import com.tuse.tuse.utilities.InvalidUserInputException;
import com.tuse.tuse.utilities.ResourceNotFoundException;
import com.tuse.tuse.utilities.ResourcePersistenceException;
import com.tuse.tuse.utilities.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
    public CompanyResponse save(NewCompanyRequest companyRequest, User user) throws InvalidUserInputException, ResourcePersistenceException{

        if(user == null) throw new UnauthorizedException("Registration needed");

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Double> validPriceInput = Objects::nonNull;
        Predicate<Long> validOutStandingSharesInput = Objects::nonNull;

        if(!notNullOrEmpty.test(companyRequest.getName()))
            throw new InvalidUserInputException("Name is empty");
        if(!notNullOrEmpty.test(companyRequest.getSymbol()))
            throw new InvalidUserInputException("Symbol is empty");
        if(!notNullOrEmpty.test(companyRequest.getSector()))
            throw new InvalidUserInputException("Sector is empty");

        if(!validPriceInput.test(companyRequest.getInitialPrice()))
            throw new InvalidUserInputException("Invalid initial price");
        if(!validOutStandingSharesInput.test(companyRequest.getOutstandingShares()))
            throw new InvalidUserInputException("Invalid Outstanding Shares");

        isNameAvailable(companyRequest.getName());
        isSymbolAvailable(companyRequest.getSymbol());
        isNumberOfSharesAllowed(companyRequest.getOutstandingShares());

        Company company = new Company(companyRequest);
        company.setMarketCap(companyRequest.getInitialPrice()*companyRequest.getOutstandingShares());
        company.setDateListed(new Date());
        company.setOwner(user);

        companyRepo.save(company);

        return new CompanyResponse(company);
    }

    @Transactional
    public CompanyResponse update(UpdateCompanyRequest updateRequest, User user) throws InvalidUserInputException, ResourcePersistenceException{

        if(user == null) throw new ResourceNotFoundException("User was mot found");

        Predicate<String> notNullOrEmpty = (str) -> str != null && !str.trim().equals("");
        Predicate<Long> validInput = Objects::nonNull;

        if(!validInput.test(updateRequest.getCompanyId()))
            throw new ResourceNotFoundException("Updating company is missing");

        Company foundCompany = getCompanyById(updateRequest.getCompanyId());

        List<Company> userCompanies = getCompanyByUser(user.getUserId());

        if(!userCompanies.contains(foundCompany))
            throw new ResourceNotFoundException("Company was not found in user's list of companies");

        if(notNullOrEmpty.test(updateRequest.getName())){
            if(isNameAvailable(updateRequest.getName()))
                foundCompany.setName(updateRequest.getName());
        }
        if(notNullOrEmpty.test(updateRequest.getSymbol())){
            if(isSymbolAvailable(updateRequest.getSymbol()))
                foundCompany.setSymbol(updateRequest.getSymbol());
        }
        if(notNullOrEmpty.test(updateRequest.getSector()))
            foundCompany.setSector(updateRequest.getSector());

        companyRepo.save(foundCompany);

        return new CompanyResponse(foundCompany);
    }

    public Company getCompanyById(Long companyId){
            return companyRepo.findById(companyId)
                    .orElseThrow(ResourceNotFoundException::new);
    }

    public CompanyResponse getCompanyByName(String name){
        return companyRepo.findCompanyByName(name)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<Company> getCompanyByUser(Long userId){
        return companyRepo.findCompanyByUser(userId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<CompanyResponse> filterByMarketCapGreaterThan(double marketCap){
        return companyRepo.filterByMarketCapGreaterThan(marketCap)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<CompanyResponse> filterByMarketCapLowerThan(double marketCap){
        return companyRepo.filterByMarketCapLowerThan(marketCap)
                .orElseThrow(ResourceNotFoundException::new);
    }

    // ************************************************************************************************* //

    private boolean isNameAvailable(String name) {

        if(companyRepo.findCompanyByName(name).isPresent())
            throw new ResourcePersistenceException("Company name exists");

        return true;
    }

    private boolean isSymbolAvailable(String symbol) {

        if(companyRepo.findCompanyBySymbol(symbol).isPresent())
            throw new ResourcePersistenceException("Company name exists");

        return true;
    }

    private void isNumberOfSharesAllowed(Long outstandingShares) {

        long maxOutstandingShares = 1000;

        if(outstandingShares > maxOutstandingShares)
            throw new UnsupportedOperationException("Number of outstanding shares is greater than max allowed (1000) ");

    }



}
