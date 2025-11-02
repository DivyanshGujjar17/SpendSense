package com.MoneyManager.SpendSense.Controller;

import com.MoneyManager.SpendSense.Dto.ExpenseDTO;
import com.MoneyManager.SpendSense.Dto.IncomeDTO;
import com.MoneyManager.SpendSense.Service.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(ExpenseDTO expenseDTO){
    ExpenseDTO expenseDTo=expenseService.addExpense(expenseDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(expenseDTo);
    }
    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getexpense(){
        List<ExpenseDTO> expenses=expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }
@DeleteMapping("{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();

}

}
