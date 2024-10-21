package hanieJafari.MultithreadingProject.service;

import com.opencsv.exceptions.CsvValidationException;
import hanieJafari.MultithreadingProject.models.Account;
import hanieJafari.MultithreadingProject.models.Customer;
import hanieJafari.MultithreadingProject.models.ErrorRecord;
import hanieJafari.MultithreadingProject.repo.AccountRepository;
import hanieJafari.MultithreadingProject.repo.CustomerRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CsvProcessor {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ErrorLogger errorLogger;

    // N = 5 (5 thread)
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void processCsvFile(File file) {
        executorService.submit(() -> {
            try {
                if (file.getName().contains("Account")) {
                    processAccounts(file);
                } else if (file.getName().contains("Customer")) {
                    processCustomers(file);
                } else {
                    throw new IllegalArgumentException("Unsupported file type: " + file.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void processAccounts(File file) throws IOException, CsvValidationException {
        List<ErrorRecord> accountErrors = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Account account = mapAccount(nextLine);
                if (validateAccount(account, accountErrors, nextLine[0])) {
                    accountRepository.save(account);
                    System.out.println(accountRepository.findAll());
                }
            }
        }

        if (!accountErrors.isEmpty()) {
            errorLogger.logErrors(accountErrors);
        }
    }

    private void processCustomers(File file) throws IOException, CsvValidationException {
        List<ErrorRecord> customerErrors = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Customer customer = mapCustomer(nextLine);
                if (validateCustomer(customer, customerErrors, nextLine[0])) {
                    customerRepository.save(customer);
                }
            }
        }

        if (!customerErrors.isEmpty()) {
            errorLogger.logErrors(customerErrors);
        }
    }

    // Map CSV data to Account object
    private Account mapAccount(String[] line) {
        Account account = new Account();
        account.setAccountNumber(line[1]);
        account.setAccountType(Integer.parseInt(line[2]));
        account.setCustomerId(Long.parseLong(line[3]));
        account.setAccountLimit(Double.parseDouble(line[4]));
        account.setAccountOpenDate(LocalDate.parse(line[5], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        account.setAccountBalance(Double.parseDouble(line[6]));
        return account;
    }

    // Map CSV data to Customer object
    private Customer mapCustomer(String[] line) {
        Customer customer = new Customer();
        customer.setCustomerName(line[1]);
        customer.setCustomerSurname(line[2]);
        customer.setCustomerNationalId(line[3]);
        customer.setCustomerAddress(line[4]);
        customer.setCustomerZipCode(line[5]);
        customer.setCustomerBirthDate(LocalDate.parse(line[6], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return customer;
    }

    // Validation for Account object
    private boolean validateAccount(Account account, List<ErrorRecord> errors, String recordNumber) {
        boolean isValid = true;

        if (account.getAccountBalance() > account.getAccountLimit()) {
            errors.add(new ErrorRecord(recordNumber, "ACC001", "Account balance exceeds limit"));
            isValid = false;
        }

        if (!List.of(1, 2, 3).contains(account.getAccountType())) {
            errors.add(new ErrorRecord(recordNumber, "ACC002", "Invalid account type"));
            isValid = false;
        }

        if (account.getAccountNumber().length() != 22) {
            errors.add(new ErrorRecord(recordNumber, "ACC003", "Invalid account number length"));
            isValid = false;
        }

        return isValid;
    }

    // Validation for Customer object
    private boolean validateCustomer(Customer customer, List<ErrorRecord> errors, String recordNumber) {
        boolean isValid = true;

        if (customer.getCustomerBirthDate().isBefore(LocalDate.of(1995, 1, 1))) {
            errors.add(new ErrorRecord(recordNumber, "CUST001", "Customer birth date before 1995"));
            isValid = false;
        }

        if (customer.getCustomerNationalId().length() != 10) {
            errors.add(new ErrorRecord(recordNumber, "CUST002", "Invalid national ID length"));
            isValid = false;
        }

        return isValid;
    }
}