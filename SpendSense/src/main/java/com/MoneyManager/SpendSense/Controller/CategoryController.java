package com.MoneyManager.SpendSense.Controller;

import com.MoneyManager.SpendSense.Dto.CategoryDTO;
import com.MoneyManager.SpendSense.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    public ResponseEntity<CategoryDTO> savethecategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedcategory=categoryService.savecategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedcategory);
    }
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(){
        List<CategoryDTO> AllCategories=categoryService.getallcategoriesforCurrentUser();
        return ResponseEntity.ok(AllCategories);
    }
    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByTypeForCurrentUser(@PathVariable String type){
List<CategoryDTO> list=categoryService.getcategoriesbyTypeforCurrentuser(type);
return ResponseEntity.ok(list);
    }
    @PostMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> UpdatetheCategoryId(@PathVariable Long categoryId,@RequestBody CategoryDTO categoryDTO){
CategoryDTO currentDTO=categoryService.updateCategory(categoryId,categoryDTO);
return ResponseEntity.ok(categoryDTO);
    }
}
