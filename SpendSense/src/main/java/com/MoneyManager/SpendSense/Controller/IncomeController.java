package com.MoneyManager.SpendSense.Controller;

import com.MoneyManager.SpendSense.Dto.IncomeDTO;
import com.MoneyManager.SpendSense.Service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {
    private final IncomeService incomeService;
    @PostMapping
    public ResponseEntity<IncomeDTO> addexpense(IncomeDTO incomeDTO){
        IncomeDTO incomeDTo=incomeService.addExpense(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeDTo);
    }
    public ResponseEntity<List<IncomeDTO>> getincome(){
        List<IncomeDTO> incomes=incomeService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(incomes);

    }
    @DeleteMapping("/{id}")
public ResponseEntity<Void> deleteIncome(@PathVariable Long id){
        incomeService.deleteincome(id);
        return ResponseEntity.noContent().build();
}
}
