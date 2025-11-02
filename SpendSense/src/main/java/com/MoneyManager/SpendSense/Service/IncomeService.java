package com.MoneyManager.SpendSense.Service;

import com.MoneyManager.SpendSense.Dto.ExpenseDTO;
import com.MoneyManager.SpendSense.Dto.IncomeDTO;
import com.MoneyManager.SpendSense.Entity.CategoryEntity;
import com.MoneyManager.SpendSense.Entity.ExpenseEntity;
import com.MoneyManager.SpendSense.Entity.IncomeEntity;
import com.MoneyManager.SpendSense.Entity.ProfileEntity;
import com.MoneyManager.SpendSense.Repositiory.CategoryRepository;
import com.MoneyManager.SpendSense.Repositiory.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;
    public IncomeDTO addExpense(IncomeDTO expenseDTO){
        ProfileEntity profile=profileService.getcurrentprofile();
        CategoryEntity category=categoryRepository.findById(expenseDTO.getId()).orElseThrow(()-> new RuntimeException("Category not found"));
        IncomeEntity newExpense=toEntity(expenseDTO,profile,category);
        newExpense=incomeRepository.save(newExpense);
        return toDTO(newExpense);
    }
    public List<IncomeDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getcurrentprofile();
        LocalDate now=LocalDate.now();
        LocalDate startDate=now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list= incomeRepository.findByProfile_IdAndDateBetween(profile.getId(),startDate,endDate);
        return list.stream().map(this::toDTO).toList();
    }
    public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
        ProfileEntity profile=profileService.getcurrentprofile();
        List<IncomeEntity> list=incomeRepository.findTop5ByProfile_IdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();

    }
    public void deleteincome(Long IncomeId){
        ProfileEntity profile=profileService.getcurrentprofile();
        IncomeEntity entity=incomeRepository.findById(IncomeId).orElseThrow(()-> new RuntimeException("Income not Found"));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorize to delete this expense");

        }
        incomeRepository.delete(entity);
    }
    public List<IncomeDTO> filteredIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile=profileService.getcurrentprofile();
        List<IncomeEntity> list=incomeRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);
        return list.stream().map(this::toDTO).toList();
    }
    public BigDecimal getTotalIncomesForCurrentUser(){
        ProfileEntity profile=profileService.getcurrentprofile();
        BigDecimal total=incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total!=null ?total:BigDecimal.ZERO;
    }
    private IncomeEntity toEntity(IncomeDTO expenseDTO, ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder().
                name(expenseDTO.getName()).
                icon(expenseDTO.getIcon()).
                amount(expenseDTO.getAmount()).
                date(expenseDTO.getDate()).
                profile(profile).
                category(category)
                .build();
    }

    private IncomeDTO toDTO(IncomeEntity expenseEntity){
        return IncomeDTO.builder().
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
