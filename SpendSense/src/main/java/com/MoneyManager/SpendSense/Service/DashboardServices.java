package com.MoneyManager.SpendSense.Service;

import com.MoneyManager.SpendSense.Dto.ExpenseDTO;
import com.MoneyManager.SpendSense.Dto.IncomeDTO;
import com.MoneyManager.SpendSense.Dto.RecentTransactionDTO;
import com.MoneyManager.SpendSense.Entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardServices {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getcurrentprofile();
        Map<String, Object> returnValue = new LinkedHashMap<>();

        // Fetch latest income and expense records
        List<IncomeDTO> latestIncome = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpense = expenseService.getLatest5ExpenseForCurrentUser();

        // Combine both as recent transactions
        List<RecentTransactionDTO> recentTransactions = Stream.concat(
                        latestIncome.stream().map(income -> RecentTransactionDTO.builder()
                                .id(income.getId())
                                .profileId(profile.getId())
                                .name(income.getName())
                                .icon(income.getIcon())
                                .amount(income.getAmount())
                                .date(income.getDate())
                                .updatedAt(income.getUpdatedAt())
                                .createdAt(income.getCreatedAt())
                                .type("income")
                                .build()),
                        latestExpense.stream().map(expense -> RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(profile.getId())
                                .name(expense.getName())
                                .icon(expense.getIcon())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .updatedAt(expense.getUpdatedAt())
                                .createdAt(expense.getCreatedAt())
                                .type("expense")
                                .build())
                )
                // Sort by date (latest first)
                .sorted((a,b)->{
                    int cmp=b.getDate().compareTo(a.getDate());
                    if(cmp==0&&a.getCreatedAt()!=null&&b.getCreatedAt()!=null){
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
    }).collect(Collectors.toList());
        returnValue.put("totalBalance",
                incomeService.getTotalIncomesForCurrentUser().subtract(expenseService.getTotalExpenseForCurrentUser()));
        returnValue.put("totalExpense", expenseService.getTotalExpenseForCurrentUser());
        returnValue.put("totalIncome",incomeService.getTotalIncomesForCurrentUser());
        returnValue.put("recent5Expense",latestExpense);
        returnValue.put("recent5Incomes",latestIncome);
        returnValue.put("recentTransaction",recentTransactions);
        return returnValue;
    }
}
