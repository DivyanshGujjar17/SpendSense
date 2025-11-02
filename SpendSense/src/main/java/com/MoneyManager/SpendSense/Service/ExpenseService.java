package com.MoneyManager.SpendSense.Service;

import com.MoneyManager.SpendSense.Dto.ExpenseDTO;
import com.MoneyManager.SpendSense.Entity.CategoryEntity;
import com.MoneyManager.SpendSense.Entity.ExpenseEntity;
import com.MoneyManager.SpendSense.Entity.ProfileEntity;
import com.MoneyManager.SpendSense.Repositiory.CategoryRepository;
import com.MoneyManager.SpendSense.Repositiory.ExpenseRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;
    public ExpenseDTO addExpense(ExpenseDTO expenseDTO){
ProfileEntity profile=profileService.getcurrentprofile();
CategoryEntity category=categoryRepository.findById(expenseDTO.getId()).orElseThrow(()-> new RuntimeException("Category not found"));
ExpenseEntity newExpense=toEntity(expenseDTO,profile,category);
newExpense=expenseRepository.save(newExpense);
return toDTO(newExpense);
    }
    public List<ExpenseDTO> getLatest5ExpenseForCurrentUser(){
        ProfileEntity profile=profileService.getcurrentprofile();
        List<ExpenseEntity> list=expenseRepository.findTop5ByProfile_IdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }
    public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile=profileService.getcurrentprofile();
        BigDecimal total=expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total!=null ?total:BigDecimal.ZERO;
    }
    public List<ExpenseDTO> filteredExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile=profileService.getcurrentprofile();
        List<ExpenseEntity> list=expenseRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
   return list.stream().map(this::toDTO).toList();
    }
    public List<ExpenseDTO> getExpenseForUserOnDate(Long profileId,LocalDate date){
        List<ExpenseEntity> list=expenseRepository.findByProfileIdAndDate(profileId,date);
        return list.stream().map(this::toDTO).toList();
    }
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getcurrentprofile();
        LocalDate now=LocalDate.now();
        LocalDate startDate=now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
       List<ExpenseEntity> list= expenseRepository.findByProfile_IdAndDateBetween(profile.getId(),startDate,endDate);
return list.stream().map(this::toDTO).toList();
    }
 public void deleteExpense(Long expenseId){
        ProfileEntity profile=profileService.getcurrentprofile();
        ExpenseEntity entity=expenseRepository.findById(expenseId).orElseThrow(()-> new RuntimeException("Expense not Found"));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorize to delete this expense");

        }
        expenseRepository.delete(entity);
 }
    private ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profile, CategoryEntity category){
        return ExpenseEntity.builder().
        name(expenseDTO.getName()).
                icon(expenseDTO.getIcon()).
                amount(expenseDTO.getAmount()).
                date(expenseDTO.getDate()).
                profile(profile).
                category(category)
        .build();
    }

    private ExpenseDTO toDTO(ExpenseEntity expenseEntity){
        return ExpenseDTO.builder().
             id(expenseEntity.getId()).
                name(expenseEntity.getName()).
                icon(expenseEntity.getIcon()).
                categoryId(expenseEntity.getCategory()!=null? expenseEntity.getCategory().getId():null).
                categoryName(expenseEntity.getCategory()!=null? expenseEntity.getCategory().getName():"N/A").
                amount(expenseEntity.getAmount()).updatedAt(expenseEntity.getUpdatedAt()).createdAt(expenseEntity.getCreatedAt())
                .date(expenseEntity.getDate())
        .build();
    }


}
