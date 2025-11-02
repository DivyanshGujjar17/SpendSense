package com.MoneyManager.SpendSense.Controller;

import com.MoneyManager.SpendSense.Dto.ExpenseDTO;
import com.MoneyManager.SpendSense.Dto.FilterDTO;
import com.MoneyManager.SpendSense.Dto.IncomeDTO;
import com.MoneyManager.SpendSense.Repositiory.IncomeRepository;
import com.MoneyManager.SpendSense.Service.ExpenseService;
import com.MoneyManager.SpendSense.Service.IncomeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filterDTO){
        LocalDate startDate=filterDTO.getStartDate()!=null ?filterDTO.getStartDate():LocalDate.MIN;
        LocalDate endDate=filterDTO.getEndDate()!=null ?filterDTO.getEndDate():LocalDate.now();
        String keyword=filterDTO.getKeyword()!=null ?filterDTO.getKeyword():"";
        String sortField=filterDTO.getSortField()!=null ?filterDTO.getSortField():"date";
        Sort.Direction direction="desc".equalsIgnoreCase(filterDTO.getKeyword())?Sort.Direction.DESC:Sort.Direction.ASC;
        Sort sort=Sort.by(direction,sortField);
        if("income".equals(filterDTO.getType())){
            List<IncomeDTO> incomes=incomeService.filteredIncomes(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(incomes);
        }
        else if("expense".equalsIgnoreCase(filterDTO.getType())){
            List<ExpenseDTO> expenses=expenseService.filteredExpenses(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(expenses);

        }
        else{
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income or 'expense");
        }
    }
}
